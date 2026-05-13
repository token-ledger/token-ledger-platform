package com.tokenledgercloud.api.domain.dashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tokenledgercloud.api.domain.dashboard.dto.DashboardKpiResponse;
import com.tokenledgercloud.api.domain.dashboard.dto.ModelCostSummaryResponse;
import com.tokenledgercloud.api.domain.dashboard.dto.ProjectCostRankingResponse;
import com.tokenledgercloud.api.domain.dashboard.service.DashboardService;
import com.tokenledgercloud.api.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/kpi")
	public ResponseEntity<ApiResponse<DashboardKpiResponse>> kpi(
		@RequestParam(required = false) Long projectId,
		@RequestParam(defaultValue = "today") String period
	) {
		return ResponseEntity.ok(ApiResponse.success(dashboardService.getKpi(projectId, period)));
	}

	@GetMapping("/model-cost-summary")
	public ResponseEntity<ApiResponse<List<ModelCostSummaryResponse>>> modelCostSummary(
		@RequestParam(required = false) Long projectId,
		@RequestParam(defaultValue = "week") String period
	) {
		return ResponseEntity.ok(ApiResponse.success(dashboardService.getModelCostSummary(projectId, period)));
	}

	@GetMapping("/project-ranking")
	public ResponseEntity<ApiResponse<List<ProjectCostRankingResponse>>> projectRanking(
		@RequestParam(defaultValue = "month") String period
	) {
		return ResponseEntity.ok(ApiResponse.success(dashboardService.getProjectCostRanking(period)));
	}
}
