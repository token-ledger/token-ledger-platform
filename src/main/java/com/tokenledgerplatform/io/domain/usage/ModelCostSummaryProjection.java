package com.tokenledgerplatform.io.domain.usage;

import java.math.BigDecimal;

public interface ModelCostSummaryProjection {

	String getModelId();

	BigDecimal getTotalCost();

	Long getTotalTokens();
}