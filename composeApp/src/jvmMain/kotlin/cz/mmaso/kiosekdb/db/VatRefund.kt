package cz.mmaso.kiosekdb.db

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.uuid.ExperimentalUuidApi

object VatRefund : Table("VATRefund") {
    val id = integer("Id").autoIncrement()
    val IdMessageSender = varchar("IdMessageSender", 250 )
    @OptIn(ExperimentalUuidApi::class)
    val IdMessageReceiver =  uuid("IdMessageReceiver")
    val VatRefundId = varchar("VatRefundId", 16 )
    val AirportCode = varchar("AirportCode", 128 )
    val WorkplaceCode = varchar("WorkplaceCode", 128 )
    val VatRefundTime = datetime("VatRefundTime" )
}