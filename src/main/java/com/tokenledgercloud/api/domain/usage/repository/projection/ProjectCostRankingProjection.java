package com.tokenledgercloud.api.domain.usage.repository.projection;

import java.math.BigDecimal;

public interface ProjectCostRankingProjection {

	Long getProjectId();

	BigDecimal getTotalCost();

	Long getTotalTokens();
}
