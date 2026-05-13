package com.tokenledgercloud.api.domain.usage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tokenledgercloud.api.domain.usage.dto.UsageLogCreateRequest;
import com.tokenledgercloud.api.domain.usage.dto.UsageLogResponse;
import com.tokenledgercloud.api.domain.usage.service.UsageLogService;
import com.tokenledgercloud.api.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsageLogController {

	private final UsageLogService usageLogService;

	@PostMapping("/internal/usage-logs")
	public ResponseEntity<ApiResponse<UsageLogResponse>> create(@Valid @RequestBody UsageLogCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("Usage log created successfully.", usageLogService.create(request)));
	}
}
