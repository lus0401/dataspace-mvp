package com.example.dataspace.broker.offer

import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<OfferEntity, Long>
