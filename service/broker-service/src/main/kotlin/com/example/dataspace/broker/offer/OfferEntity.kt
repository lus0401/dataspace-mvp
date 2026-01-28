package com.example.dataspace.broker.offer

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "offers")
class OfferEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offers_seq")
    @SequenceGenerator(name = "offers_seq", sequenceName = "offers_seq", allocationSize = 1)
    var id: Long? = null,

    @Column(nullable = false, length = 200)
    var title: String,

    @Column(nullable = false, length = 2000)
    var description: String,

    @Column(nullable = false, length = 500)
    var providerUrl: String,

    @Column(nullable = false, length = 100)
    var ownerId: String,          // 예: "B-company"

    @Column(nullable = false, length = 100)
    var dataType: String,         // 예: "application/json"

    @Column(nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)
