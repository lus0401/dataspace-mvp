package com.example.dataspace.clearinghouse.event

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "transfer_events")
class TransferEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "event_time", nullable = false)
    var eventTime: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "consumer_id", nullable = false, length = 100)
    var consumerId: String = "",

    @Column(name = "provider_url", nullable = false, length = 500)
    var providerUrl: String = "",

    @Column(name = "offer_id")
    var offerId: Long? = null,

    @Column(name = "data_endpoint", nullable = false, length = 200)
    var dataEndpoint: String = "/data",

    @Column(name = "status", nullable = false, length = 30)
    var status: String = "SUCCESS",

    @Column(name = "message", length = 2000)
    var message: String? = null
)
