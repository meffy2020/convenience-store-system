package store

import java.time.LocalDate

/**
 * 편의점 재고 관리를 위한 핵심 로직을 담당하는 클래스입니다.
 * 상품 목록, 판매 데이터, 재고 정책을 바탕으로 다양한 분석 리포트를 생성합니다.
 *
 * @property products 전체 상품 목록.
 * @property todaySales 오늘의 상품별 판매량.
 * @property stockThreshold 재고 부족으로 간주할 재고율 임계치.
 * @property expiryWarningDays 유통기한 임박으로 간주할 남은 일수.
 * @property discountPolicy 유통기한에 따른 할인율 정책.
 */
class InventoryManager(
    private val products: List<Product>,
    private val todaySales: Map<String, Int>,
    private val stockThreshold: Double,
    private val expiryWarningDays: Long,
    private val discountPolicy: Map<Int, Double>
) {

    /** 리포트 제목을 표준 형식으로 출력하는 내부 함수 */
    private fun printReportHeader(title: String) {
        println("\n+--------------------------------------------------+")
        println("  ${title}")
        println("+--------------------------------------------------+")
    }

    /**
     * 모든 재고 관련 리포트를 순서대로 생성하여 출력합니다.
     */
    fun generateReports() {
        println("\n+==================================================+")
        println("  종합 리포트 (전체)")
        println("+==================================================+")
        generateUrgentStockReport()
        generateExpirationDateReport()
        generateBestsellersReport()
        generateSalesReport()
        generateManagementAnalysisReport()
        generateOverallStatusReport()
    }

    /**
     * 재고가 부족한 상품 목록을 찾아 긴급 재고 알림 리포트를 생성합니다.
     */
    fun generateUrgentStockReport() {
        printReportHeader("🚨 긴급 재고 알림 (재고율 30% 이하)")
        val lowStockProducts = products.filter { it.isStockLow(stockThreshold) }
        if (lowStockProducts.isEmpty()) {
            println("  >> 재고 부족 상품이 없습니다.")
        } else {
            lowStockProducts.forEach { p ->
                val needed = p.stock - p.safetyStock
                val stockRate = p.safetyStock.toDouble() / p.stock * 100
                val category = when (p) {
                    is Food -> "food"
                    is Beverage -> "beverage"
                    is Snack -> "snack"
                    is Household -> "household"
                    else -> "unknown"
                }
                val productName = p.name.padEnd(15)
                val categoryName = "(${category})".padEnd(10)
                println("  - ${productName} ${categoryName} : ${p.safetyStock.toString().padStart(3)}개 -> ${p.stock.toString().padStart(3)}개 (${needed.toString().padStart(3)}개 필요) [재고율: ${String.format("%.1f", stockRate)}%]")
            }
        }
        println("+--------------------------------------------------+")
    }

    /**
     * 유통기한이 임박한 상품에 대한 할인 정책을 적용하고 리포트를 생성합니다.
     */
    fun generateExpirationDateReport() {
        printReportHeader("📆 유통기한 관리 (3일 이내 임박 상품)")
        val expiringProducts = products.filterIsInstance<Food>()
            .filter { java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), it.expirationDate) <= expiryWarningDays }
            .sortedBy { it.expirationDate }

        if (expiringProducts.isEmpty()) {
            println("  >> 유통기한 임박 상품이 없습니다.")
        } else {
            expiringProducts.forEach { p ->
                val daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), p.expirationDate)
                val discountRate = discountPolicy[daysUntilExpiry.toInt()] ?: 0.0
                val discountedPrice = p.price * (1 - discountRate)
                val productName = p.name.padEnd(18)
                println("  - ${productName} : ${daysUntilExpiry}일 남음 -> ${String.format("%.0f", discountRate * 100)}% 할인 (₩${p.price} -> ₩${discountedPrice.toInt()})")
            }
        }
        println("+--------------------------------------------------+")
    }

    /**
     * 오늘 가장 많이 팔린 상품 5개를 선정하여 베스트셀러 리포트를 생성합니다.
     */
    fun generateBestsellersReport() {
        printReportHeader("🏆 오늘의 베스트셀러 TOP 5")
        val bestsellers = todaySales.entries.sortedByDescending { it.value }.take(5)
        if (bestsellers.isEmpty()) {
            println("  >> 판매 기록이 없습니다.")
        } else {
            bestsellers.forEachIndexed { index, entry ->
                val product = products.find { it.name == entry.key }
                if (product != null) {
                    val revenue = product.price * entry.value
                    val productName = product.name.padEnd(18)
                    println("  ${index + 1}위. ${productName} : ${entry.value.toString().padStart(2)}개 판매 (매출 ₩${revenue})")
                } else {
                    println("  경고: '${entry.key}' 상품을 찾을 수 없습니다.")
                }
            }
        }
        println("+--------------------------------------------------+")
    }

    /**
     * 오늘의 총 매출 및 상품별 매출 상세 내역 리포트를 생성합니다.
     */
    fun generateSalesReport() {
        printReportHeader("📈 매출 현황")
        val validSales = todaySales.entries.mapNotNull { entry ->
            products.find { it.name == entry.key }?.let { product ->
                product to entry.value
            }
        }

        todaySales.keys.minus(validSales.map { it.first.name }.toSet()).forEach {
            println("  경고: '${it}' 상품을 상품 목록에서 찾을 수 없습니다.")
        }

        val totalRevenue = validSales.sumOf { (product, quantity) -> product.price * quantity }
        val totalItemsSold = validSales.sumOf { (_, quantity) -> quantity }

        println("  - 오늘 총 매출: ₩${totalRevenue} (${totalItemsSold}개 판매)")
        println("  ------------------------------------------------")
        validSales.sortedByDescending { it.second }.forEach { (product, quantity) ->
            val revenue = product.price * quantity
            val productName = product.name.padEnd(18)
            val revenueStr = ("₩" + revenue).padEnd(7)
            println("    * ${productName}: ${revenueStr} (${quantity.toString().padStart(2)}개 × ₩${product.price})")
        }
        println("+--------------------------------------------------+")
    }

    /**
     * 재고 회전율, 판매 효율 등 경영 분석 지표 리포트를 생성합니다.
     */
    fun generateManagementAnalysisReport() {
        printReportHeader("📊 경영 분석 리포트")
        val validProductsWithSales = products.mapNotNull { p ->
            val sold = todaySales[p.name]
            if (sold != null) {
                Triple(p, p.getInventoryTurnoverRate(sold), p.getSalesEfficiency(sold))
            } else {
                null
            }
        }

        if (validProductsWithSales.isNotEmpty()) {
            val maxTurnoverProduct = validProductsWithSales.maxByOrNull { it.second }!!
            val minTurnoverProduct = validProductsWithSales.filter { it.second > 0 }.minByOrNull { it.second }
            val maxEfficiencyProduct = validProductsWithSales.maxByOrNull { it.third }!!

            println("  [효율 분석]")
            println("  - 재고 회전율 최고: ${maxTurnoverProduct.first.name} (${String.format("%.2f", maxTurnoverProduct.second)})")
            minTurnoverProduct?.let {
                println("  - 재고 회전율 최저: ${it.first.name} (${String.format("%.2f", it.second)})")
            }
            println("  - 판매 효율 1위   : ${maxEfficiencyProduct.first.name} (${String.format("%.0f%%", maxEfficiencyProduct.third * 100)})")
        }

        println("\n  [재고 상태]")
        val overstockedProducts = products.filter { (todaySales[it.name] ?: 0) == 0 && it.safetyStock > it.stock * 0.5 }
        val overstockedStr = overstockedProducts.joinToString { "${it.name}(${it.safetyStock}개)" }.ifEmpty { "없음" }
        println("  - 재고 과다 품목  : ${overstockedStr}")

        val recommendedOrders = products.filter { it.isStockLow(stockThreshold) }
        println("  - 발주 권장        : 총 ${recommendedOrders.size}개 품목, ${recommendedOrders.sumOf { it.stock - it.safetyStock }}개 수량")
        println("+--------------------------------------------------+")
    }

    /**
     * 전체적인 재고 및 판매 현황을 요약하여 리포트를 생성합니다.
     */
    fun generateOverallStatusReport() {
        printReportHeader("📋 종합 운영 현황")
        val updatedProducts = products.map { p ->
            val sold = todaySales[p.name] ?: 0
            when (p) {
                is Food -> p.copy(safetyStock = p.safetyStock - sold)
                is Beverage -> p.copy(safetyStock = p.safetyStock - sold)
                is Snack -> p.copy(safetyStock = p.safetyStock - sold)
                is Household -> p.copy(safetyStock = p.safetyStock - sold)
                else -> p
            }
        }
        println("  - 전체 등록 상품: ${products.size}종")
        println("  - 현재 총 재고   : ${updatedProducts.sumOf { it.safetyStock }}개")
        println("  - 현재 재고 가치 : ₩${updatedProducts.sumOf { it.price * it.safetyStock }}")
        println("  - 재고 부족 상품 : ${updatedProducts.filter { it.isStockLow(stockThreshold) }.size}종")
        println("  - 유통기한 임박  : ${products.filterIsInstance<Food>().filter { java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), it.expirationDate) <= expiryWarningDays }.size}종")
        println("  - 오늘 총 판매   : ${todaySales.values.sum()}개")
        println("+--------------------------------------------------+")
    }
}