package store

/**
 * 모든 상품 유형이 구현해야 하는 공통 인터페이스입니다.
 * 상품의 기본 속성과 재고 분석을 위한 기본 메소드를 정의합니다.
 */
interface Product {
    val name: String
    val price: Int
    val stock: Int
    val safetyStock: Int

    /**
     * 현재 재고가 안전 재고 임계치보다 낮은지 확인합니다.
     * @param threshold 재고율 임계치 (예: 0.3은 30%를 의미).
     * @return 재고가 임계치 미만이면 true, 아니면 false.
     */
    fun isStockLow(threshold: Double): Boolean = safetyStock.toDouble() / stock < threshold

    /**
     * 재고 회전율을 계산합니다. (판매량 / 평균 재고)
     * @param sold 판매된 상품의 수.
     * @return 계산된 재고 회전율. 판매량이 0이면 0.0을 반환합니다.
     */
    fun getInventoryTurnoverRate(sold: Int): Double {
        if (sold == 0) return 0.0
        val averageInventory = (safetyStock + (safetyStock - sold)) / 2.0
        if (averageInventory == 0.0) return Double.POSITIVE_INFINITY
        return sold / averageInventory
    }

    /**
     * 판매 효율을 계산합니다. (판매량 / 현재 재고)
     * @param sold 판매된 상품의 수.
     * @return 계산된 판매 효율. 현재 재고가 0이면 무한대를 반환합니다.
     */
    fun getSalesEfficiency(sold: Int): Double {
        if (safetyStock == 0) return Double.POSITIVE_INFINITY
        return sold.toDouble() / safetyStock
    }
}
