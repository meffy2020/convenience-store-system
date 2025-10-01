package store

import java.time.LocalDate

class InventoryManager(
    private val products: List<Product>,
    private val todaySales: Map<String, Int>,
    private val stockThreshold: Double,
    private val expiryWarningDays: Long,
    private val discountPolicy: Map<Int, Double>
) {

    fun generateReports() {
        println("=== 24시간 학교 편의점 스마트 재고 관리 시스템 ===")
        generateUrgentStockReport()
        generateExpirationDateReport()
        generateBestsellersReport()
        generateSalesReport()
        generateManagementAnalysisReport()
        generateOverallStatusReport()
    }

    private fun generateUrgentStockReport() {
        println("\n🚨 긴급 재고 알림 (재고율 30% 이하)")
        products.filter { it.isStockLow(stockThreshold) }.forEach { p ->
            val needed = p.stock - p.safetyStock
            val stockRate = p.safetyStock.toDouble() / p.stock * 100
            val category = when (p) {
                is Food -> "food"
                is Beverage -> "beverage"
                is Snack -> "snack"
                else -> "unknown"
            }
            println("- ${p.name}($category): 현재 ${p.safetyStock}개 -> 적정재고 ${p.stock}개 (${needed}개 발주 필요) [재고율: ${String.format("%.1f", stockRate)}%]")
        }
    }

    private fun generateExpirationDateReport() {
        println("\n📆 유통기한 관리 (3일 이내 임박 상품)")
        products.filterIsInstance<Food>()
            .filter { java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), it.expirationDate) <= expiryWarningDays }
            .sortedBy { it.expirationDate }
            .forEach { p ->
                val daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), p.expirationDate)
                val discountRate = discountPolicy[daysUntilExpiry.toInt()] ?: 0.0
                val discountedPrice = p.price * (1 - discountRate)
                println("- ${p.name}: ${daysUntilExpiry}일 남음 -> 할인률 ${String.format("%.0f", discountRate * 100)}% 적용 (₩${p.price} -> ₩${discountedPrice.toInt()})")
            }
    }

    private fun generateBestsellersReport() {
        println("\n🏆 오늘의 베스트셀러 TOP 5")
        todaySales.entries.sortedByDescending { it.value }.take(5).forEachIndexed { index, entry ->
            val product = products.find { it.name == entry.key }!!
            val revenue = product.price * entry.value
            println("${index + 1}위: ${product.name} (${entry.value}개 판매, 매출 ₩${revenue})")
        }
    }

    private fun generateSalesReport() {
        println("\n📈 매출 현황")
        val totalRevenue = todaySales.entries.sumOf { entry ->
            val product = products.find { it.name == entry.key }!!
            product.price * entry.value
        }
        val totalItemsSold = todaySales.values.sum()
        println("- 오늘 총 매출: ₩${totalRevenue} (${totalItemsSold}개 판매)")
        todaySales.entries.sortedByDescending { it.value }.forEach { entry ->
            val product = products.find { it.name == entry.key }!!
            val revenue = product.price * entry.value
            println("    * ${product.name}: ₩${revenue} (${entry.value}개 × ₩${product.price})")
        }
    }

    private fun generateManagementAnalysisReport() {
        println("\n📊 경영 분석 리포트")
        val turnoverRates = products.map { p -> p.name to p.getInventoryTurnoverRate(todaySales[p.name] ?: 0) }.toMap()
        val salesEfficiencies = products.map { p -> p.name to p.getSalesEfficiency(todaySales[p.name] ?: 0) }.toMap()

        val maxTurnoverProduct = turnoverRates.maxByOrNull { it.value }!!
        val minTurnoverProduct = turnoverRates.filter { it.value > 0 }.minByOrNull { it.value }!!
        val maxEfficiencyProduct = salesEfficiencies.maxByOrNull { it.value }!!

        println("- 재고 회전율 최고: ${maxTurnoverProduct.key} (${String.format("%.2f", maxTurnoverProduct.value)})")
        println("- 재고 회전율 최저: ${minTurnoverProduct.key} (${String.format("%.2f", minTurnoverProduct.value)})")
        println("- 판매 효율 1위: ${maxEfficiencyProduct.key} (${String.format("%.0f%%", maxEfficiencyProduct.value * 100)})")

        val overstockedProducts = products.filter { (todaySales[it.name] ?: 0) == 0 && it.safetyStock > it.stock * 0.5 }
        println("- 재고 과다 품목: ${overstockedProducts.joinToString { "${it.name} (${it.safetyStock}개)" }}")

        val recommendedOrders = products.filter { it.isStockLow(stockThreshold) }
        println("- 발주 권장: 총 ${recommendedOrders.size}개 품목, ${recommendedOrders.sumOf { it.stock - it.safetyStock }}개 수량")
    }

    private fun generateOverallStatusReport() {
        println("\n📋 종합 운영 현황")
        val updatedProducts = products.map { p ->
            val sold = todaySales[p.name] ?: 0
            when (p) {
                is Food -> p.copy(safetyStock = p.safetyStock - sold)
                is Beverage -> p.copy(safetyStock = p.safetyStock - sold)
                is Snack -> p.copy(safetyStock = p.safetyStock - sold)
                else -> p
            }
        }
        println("- 전체 등록 상품: ${products.size}종")
        println("- 현재 총 재고: ${updatedProducts.sumOf { it.safetyStock }}개")
        println("- 현재 재고 가치: ₩${updatedProducts.sumOf { it.price * it.safetyStock }}")
        println("- 재고 부족 상품: ${updatedProducts.filter { it.isStockLow(stockThreshold) }.size}종")
        println("- 유통기한 임박: ${products.filterIsInstance<Food>().filter { java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), it.expirationDate) <= expiryWarningDays }.size}종")
        println("- 오늘 총 판매: ${todaySales.values.sum()}개")
        println("- 시스템 처리 완료: 100%")
    }
}
