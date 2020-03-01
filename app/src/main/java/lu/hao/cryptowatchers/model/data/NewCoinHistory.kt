package lu.hao.cryptowatchers.model.data

data class HistoryPriceResponse(
    val data: List<Price>,
    val timestamp: Long
)

data class Price(
    val priceUsd: Double,
    val time: Double
)