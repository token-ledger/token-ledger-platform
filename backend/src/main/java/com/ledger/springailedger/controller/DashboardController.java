package com.ledger.springailedger.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ledger.springailedger.dto.dashboard.DashboardKpiResponse;
import com.ledger.springailedger.dto.dashboard.ModelCostSummaryResponse;
import com.ledger.springailedger.dto.dashboard.ProjectCostRankingResponse;
import com.ledger.springailedger.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/kpi")
	public ResponseEntity<DashboardKpiResponse> kpi(
		@RequestParam(required = false) Long projectId,
		@RequestParam(defaultValue = "today") String period
	) {
		return ResponseEntity.ok(dashboardService.getKpi(projectId, period));
	}

	@GetMapping("/model-cost-summary")
	public ResponseEntity<List<ModelCostSummaryResponse>> modelCostSummary(
		@RequestParam(required = false) Long projectId,
		@RequestParam(defaultValue = "week") String period
	) {
		return ResponseEntity.ok(dashboardService.getModelCostSummary(projectId, period));
	}

	@GetMapping("/project-ranking")
	public ResponseEntity<List<ProjectCostRankingResponse>> projectRanking(
		@RequestParam(defaultValue = "month") String period
	) {
		return ResponseEntity.ok(dashboardService.getProjectCostRanking(period));
	}
}