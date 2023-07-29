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

sealed class Val<T : Any>
class Without<T : Any> : Val<T>()
class With<T : Any>(val value: T) : Val<T>()

data class ProductBuilder<ID : Val<UUID>, NAME : Val<String>, PRICE : Val<BigDecimal>> (
    val id: ID, val name: NAME, val price: PRICE, val description: String?, val images: List<URI>
) {
    companion object {
        operator fun invoke() = ProductBuilder(
            id = Without(),
            name = Without(),
            price = Without(),
            description = null,
            images = listOf()
        )
    }

    fun id(uuid: UUID) = ProductBuilder(With(uuid), name, price, description, images)

    fun name(name: String) = ProductBuilder(id, With(name), price, description, images)

    fun price(price: BigDecimal) = ProductBuilder(id, name, With(price), description, images)

    fun description(desc: String) = copy(description = desc)

    fun addImage(img: URI) = copy(images = images + img)
}

fun ProductBuilder<With<UUID>, With<String>, With<BigDecimal>>.build() =
    Product(id.value, name.value, price.value, description, images)

fun main() {
    ProductBuilder()
        .id(UUID.randomUUID())
        .name("Comb")
        .description("Green plastic comb")
        .price(12.34.toBigDecimal())
        .build()
}
