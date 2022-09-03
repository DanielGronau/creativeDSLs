package creativeDSLs.chapter_06

import java.math.BigDecimal
import java.net.URI
import java.util.*

data class Product(
    val id: UUID,
    val name: String,
    val price: BigDecimal,
    val description: String?,
    val images: List<URI>
)

sealed class Mandatory<T : Any>
class Without<T : Any> : Mandatory<T>()
class With<T : Any>(val value: T) : Mandatory<T>()

data class ProductBuilder<ID : Mandatory<UUID>, NAME : Mandatory<String>, PRICE : Mandatory<BigDecimal>>(
    val id: ID, val name: NAME, val price: PRICE, val description: String?, val images: List<URI>
) {
    fun id(uuid: UUID) = ProductBuilder(With(uuid), name, price, description, images)
    fun name(n: String) = ProductBuilder(id, With(n), price, description, images)
    fun price(p: BigDecimal) = ProductBuilder(id, name, With(p), description, images)
    fun description(desc: String) = copy(description = desc)
    fun addImage(img: URI) = copy(images = images + img)
}

fun productBuilder() = ProductBuilder(
    id = Without(),
    name = Without(),
    price = Without(),
    description = null,
    images = listOf()
)

fun ProductBuilder<With<UUID>, With<String>, With<BigDecimal>>.build() =
    Product(id.value, name.value, price.value, description, images)

fun main() {
    productBuilder()
        .id(UUID.randomUUID())
        .name("Comb")
        .description("Green plastic comb")
        .price(12.34.toBigDecimal())
        .build()
}
