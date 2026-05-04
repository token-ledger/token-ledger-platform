package com.tokenledgerplatform.io.domain.usage;

import java.math.BigDecimal;

public interface KpiProjection {

	BigDecimal getTotalCost();

	Long getTotalTokens();

	Long getBlockedRequests();
}