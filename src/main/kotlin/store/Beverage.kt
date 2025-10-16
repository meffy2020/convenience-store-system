package store

/**
 * 음료 상품을 나타내는 데이터 클래스입니다.
 * @property volume 용량 (ml).
 */
data class Beverage(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int,
    val volume: Int
) : Product