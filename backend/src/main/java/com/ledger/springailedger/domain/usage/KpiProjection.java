package com.ledger.springailedger.domain.usage;

import java.math.BigDecimal;

public interface KpiProjection {

	BigDecimal getTotalCost();

	Long getTotalTokens();

	Long getBlockedRequests();
}