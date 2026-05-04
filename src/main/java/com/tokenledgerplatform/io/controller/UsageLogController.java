package com.tokenledgerplatform.io.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tokenledgerplatform.io.dto.usage.UsageLogCreateRequest;
import com.tokenledgerplatform.io.dto.usage.UsageLogResponse;
import com.tokenledgerplatform.io.service.UsageLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsageLogController {

	private final UsageLogService usageLogService;

	@PostMapping("/internal/usage-logs")
	public ResponseEntity<UsageLogResponse> create(@Valid @RequestBody UsageLogCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usageLogService.create(request));
	}
}