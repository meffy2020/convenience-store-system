package store

/**
 * 생활용품 상품을 나타내는 데이터 클래스입니다.
 */
data class Household(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int
) : Product