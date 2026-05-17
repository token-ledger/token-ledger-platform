package com.tokenledgercloud.api.domain.budget.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tokenledgercloud.api.global.exception.ApiException;
import com.tokenledgercloud.api.global.exception.ErrorCode;
import com.tokenledgercloud.api.domain.budget.dto.BudgetSummaryResponse;
import com.tokenledgercloud.api.domain.budget.dto.BudgetUpsertRequest;
import com.tokenledgercloud.api.domain.budget.dto.BudgetUpsertResponse;
import com.tokenledgercloud.api.domain.budget.entity.MonthlyBudgetSetting;
import com.tokenledgercloud.api.domain.budget.repository.MonthlyBudgetSettingRepository;
import com.tokenledgercloud.api.domain.usage.repository.UsageLogRepository;

import com.tokenledgercloud.api.global.exception.BudgetNotFoundException;
import com.tokenledgercloud.api.global.exception.InvalidMonthException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

	private static final String DEFAULT_ORGANIZATION_ID = "default-org";

	private final MonthlyBudgetSettingRepository budgetRepository;
	private final UsageLogRepository usageLogRepository;

	@Transactional
	public BudgetUpsertResponse upsert(BudgetUpsertRequest request) {
		if (!"MONTHLY".equals(request.periodType())) {
			throw new ApiException(ErrorCode.INVALID_PERIOD_TYPE);
		}

		MonthlyBudgetSetting budget = findBudget(request.projectId(), request.environment())
			.orElseGet(() -> MonthlyBudgetSetting.builder()
				.organizationId(DEFAULT_ORGANIZATION_ID)
				.projectId(request.projectId())
				.environment(request.environment())
				.build()
			);

		budget.setLimitUsd(request.limitUsd());
		applyThresholds(budget, request.thresholds());

		MonthlyBudgetSetting saved = budgetRepository.save(budget);

		return new BudgetUpsertResponse(
			saved.getId(),
			saved.getProjectId(),
			saved.getEnvironment(),
			saved.getLimitUsd(),
			toThresholds(saved)
		);
	}

	@Transactional(readOnly = true)
	public BudgetSummaryResponse getSummary(String projectId, String environment, String month) {
		YearMonth yearMonth = parseMonth(month);

		MonthlyBudgetSetting budget = findBudget(projectId, environment)
			.orElseThrow(BudgetNotFoundException::new);

		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.plusMonths(1).atDay(1);

		LocalDateTime from = startDate.atStartOfDay();
		LocalDateTime to = endDate.atStartOfDay();

		BigDecimal spentUsd = usageLogRepository.sumTotalCostUsd(projectId, environment, from, to);
		BigDecimal limitUsd = budget.getLimitUsd();

		BigDecimal remainingUsd = limitUsd.subtract(spentUsd);
		if (remainingUsd.compareTo(BigDecimal.ZERO) < 0) {
			remainingUsd = BigDecimal.ZERO;
		}

		BigDecimal usagePercent = BigDecimal.ZERO;
		if (limitUsd.compareTo(BigDecimal.ZERO) > 0) {
			usagePercent = spentUsd
				.multiply(BigDecimal.valueOf(100))
				.divide(limitUsd, 2, RoundingMode.HALF_UP);
		}

		return new BudgetSummaryResponse(
			projectId,
			environment,
			month,
			limitUsd,
			spentUsd,
			remainingUsd,
			usagePercent,
			toThresholds(budget)
		);
	}

	private java.util.Optional<MonthlyBudgetSetting> findBudget(String projectId, String environment) {
		if (projectId == null && environment == null) {
			return budgetRepository.findFirstByOrganizationIdAndProjectIdIsNullAndEnvironmentIsNull(
				DEFAULT_ORGANIZATION_ID
			);
		}

		return budgetRepository.findFirstByOrganizationIdAndProjectIdAndEnvironment(
			DEFAULT_ORGANIZATION_ID,
			projectId,
			environment
		);
	}

	private YearMonth parseMonth(String month) {
		try {
			return YearMonth.parse(month);
		} catch (Exception e) {
			throw new InvalidMonthException();
		}
	}

	private void applyThresholds(MonthlyBudgetSetting budget, List<Integer> thresholds) {
		List<Integer> safeThresholds = thresholds == null ? List.of(50, 80, 100) : thresholds;

		budget.setThreshold50Enabled(safeThresholds.contains(50));
		budget.setThreshold80Enabled(safeThresholds.contains(80));
		budget.setThreshold100Enabled(safeThresholds.contains(100));
	}

	private List<Integer> toThresholds(MonthlyBudgetSetting budget) {
		List<Integer> thresholds = new ArrayList<>();

		if (Boolean.TRUE.equals(budget.getThreshold50Enabled())) thresholds.add(50);
		if (Boolean.TRUE.equals(budget.getThreshold80Enabled())) thresholds.add(80);
		if (Boolean.TRUE.equals(budget.getThreshold100Enabled())) thresholds.add(100);

		return thresholds;
	}
}