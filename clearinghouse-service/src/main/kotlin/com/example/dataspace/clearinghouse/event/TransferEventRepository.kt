package com.example.dataspace.clearinghouse.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TransferEventRepository : JpaRepository<TransferEventEntity, Long> {

    @Query(
        """
        select e from TransferEventEntity e
        where (:consumerId is null or e.consumerId = :consumerId)
          and (:status is null or e.status = :status)
        order by e.eventTime desc
        """
    )
    fun findFiltered(
        @Param("consumerId") consumerId: String?,
        @Param("status") status: String?
    ): List<TransferEventEntity>
}
