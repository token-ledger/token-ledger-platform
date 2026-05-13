package com.tokenledgercloud.api.domain.usage.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tokenledgercloud.api.domain.usage.entity.UsageLog;
import com.tokenledgercloud.api.domain.usage.repository.projection.KpiProjection;
import com.tokenledgercloud.api.domain.usage.repository.projection.ModelCostSummaryProjection;
import com.tokenledgercloud.api.domain.usage.repository.projection.ProjectCostRankingProjection;

public interface UsageLogRepository extends JpaRepository<UsageLog, String> {

	Optional<UsageLog> findByProjectIdAndEnvironmentAndRequestId(String projectId, String environment, String requestId);

	@Query("""
	select coalesce(sum(u.totalCostUsd), 0) as totalCost,
	       coalesce(sum(u.totalTokens), 0) as totalTokens,
	       0 as blockedRequests
	from UsageLog u
	where (:projectId is null or u.projectId = :projectId)
	  and u.occurredAt >= :from
	  and u.occurredAt < :to
    """)
	KpiProjection getKpi(String projectId, LocalDateTime from, LocalDateTime to);

	@Query("""
		select u.model as modelId,
		       coalesce(sum(u.totalCostUsd), 0) as totalCost,
		       coalesce(sum(u.totalTokens), 0) as totalTokens
		from UsageLog u
		where (:projectId is null or u.projectId = :projectId)
		  and u.occurredAt >= :from
		  and u.occurredAt < :to
		group by u.model
		order by sum(u.totalCostUsd) desc
	""")
	List<ModelCostSummaryProjection> findModelCostSummary(
		String projectId,
		LocalDateTime from,
		LocalDateTime to
	);

	@Query("""
		select u.projectId as projectId,
		       coalesce(sum(u.totalCostUsd), 0) as totalCost,
		       coalesce(sum(u.totalTokens), 0) as totalTokens
		from UsageLog u
		where u.occurredAt >= :from
		  and u.occurredAt < :to
		group by u.projectId
		order by sum(u.totalCostUsd) desc
	""")
	List<ProjectCostRankingProjection> findProjectCostRanking(
		LocalDateTime from,
		LocalDateTime to
	);
}
