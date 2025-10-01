import store.Beverage
import store.Food
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
        Food("즉석라면", 1200, 40, 45, LocalDate.now().plusDays(30))
    )

    val todaySales = mapOf(
        "새우깡" to 15,
        "콜라 500ml" to 12,
        "참치마요 삼각김밥" to 10,
        "초코파이" to 8,
        "물 500ml" to 7,
        "딸기 샌드위치" to 3,
        "김치찌개 도시락" to 2
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
    inventoryManager.generateReports()
}