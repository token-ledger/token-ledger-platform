package com.ledger.springailedger.domain.usage;

import java.math.BigDecimal;

public interface ProjectCostRankingProjection {

	Long getProjectId();

	BigDecimal getTotalCost();

	Long getTotalTokens();
}