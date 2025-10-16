import store.Beverage
import store.Food
import store.Household
import store.InventoryManager
import store.Snack
import java.time.LocalDate

fun main() {
    val products = listOf(
        Snack("새우깡", 1500, 30, 5),
        Beverage("콜라 500ml", 1500, 25, 8, 500),
        Food("김치찌개 도시락", 5500, 20, 3, LocalDate.now().plusDays(2)),
        Food("참치마요 삼각김밥", 1500, 15, 12, LocalDate.now().plusDays(1)),
        Food("딸기 샌드위치", 2800, 10, 2, LocalDate.now()),
        Beverage("물 500ml", 1000, 50, 25, 500),
        Snack("초코파이", 3000, 20, 15),
        Food("즉석라면", 1200, 40, 45, LocalDate.now().plusDays(30)),
        Household("물티슈", 2000, 30, 10)
    )

    val todaySales = mapOf(
        "새우깡" to 15,
        "콜라 500ml" to 12,
        "참치마요 삼각김밥" to 10,
        "초코파이" to 8,
        "물 500ml" to 7,
        "딸기 샌드위치" to 3,
        "김치찌개 도시락" to 2,
        "물티슈" to 5
    )

    val stockThreshold = 0.3
    val expiryWarningDays = 3L
    val discountPolicy = mapOf(
        3 to 0.0,
        2 to 0.3,
        1 to 0.5,
        0 to 0.7
    )

    val inventoryManager = InventoryManager(products, todaySales, stockThreshold, expiryWarningDays, discountPolicy)

    while (true) {
        println("""
        +--------------------------------------------------+
        |   ____ ____ ____ ____ ____ ____ ____ ____ ____   |
        |  ||C |||S |||S |||Y |||S |||T |||E |||M |||   |  |
        |  ||__|||__|||__|||__|||__|||__|||__|||__ |  |
        |  |/__\|/__\|/__\|/__\|/__\|/__\|/__\|/__\|/__\|  |
        |                                                  |
        |   24/7 Convenience Store Inventory System      |
        +--------------------------------------------------+
        """)
        println(" [ 메뉴 ]")
        println(" 1. 전체 보고서 생성")
        println(" 2. 🚨 긴급 재고 알림")
        println(" 3. 📆 유통기한 관리")
        println(" 4. 🏆 오늘의 베스트셀러")
        println(" 5. 📈 매출 현황")
        println(" 6. 📊 경영 분석 리포트")
        println(" 7. 📋 종합 운영 현황")
        println(" 0. 종료")
        println("+--------------------------------------------------+")
        print(" >> 원하는 기능의 번호를 입력하세요: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> inventoryManager.generateReports()
            2 -> inventoryManager.generateUrgentStockReport()
            3 -> inventoryManager.generateExpirationDateReport()
            4 -> inventoryManager.generateBestsellersReport()
            5 -> inventoryManager.generateSalesReport()
            6 -> inventoryManager.generateManagementAnalysisReport()
            7 -> inventoryManager.generateOverallStatusReport()
            0 -> {
                println(" >> 시스템을 종료합니다.")
                return
            }
            else -> println(" >> 잘못된 입력입니다. 다시 시도해주세요.")
        }
    }
}