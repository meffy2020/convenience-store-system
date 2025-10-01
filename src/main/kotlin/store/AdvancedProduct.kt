package store

import java.time.LocalDate

interface Product {
    val name: String
    val price: Int
    val stock: Int
    val safetyStock: Int

    fun isStockLow(threshold: Double): Boolean = safetyStock.toDouble() / stock < threshold

    fun getInventoryTurnoverRate(sold: Int): Double {
        if (sold == 0) return 0.0
        val averageInventory = (safetyStock + (safetyStock - sold)) / 2.0
        if (averageInventory == 0.0) return Double.POSITIVE_INFINITY
        return sold / averageInventory
    }

    fun getSalesEfficiency(sold: Int): Double {
        if (safetyStock == 0) return Double.POSITIVE_INFINITY
        return sold.toDouble() / safetyStock
    }
}

data class Food(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int,
    val expirationDate: LocalDate
) : Product

data class Beverage(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int,
    val volume: Int
) : Product

data class Snack(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int
) : Product
