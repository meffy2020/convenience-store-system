package store

import java.time.LocalDate

/**
 * 식품 상품을 나타내는 데이터 클래스입니다.
 * @property expirationDate 유통기한.
 */
data class Food(
    override val name: String,
    override val price: Int,
    override val stock: Int,
    override val safetyStock: Int,
    val expirationDate: LocalDate
) : Product