package creativeDSLs.chapter_06.kotlinBuilder


class Person(val firstName: String, val lastName: String, val age: Int?)

class PersonBuilder_JavaStyle {
    private var firstName: String? = null
    private var lastName: String? = null
    private var age: Int? = null

    fun setFirstName(firstName: String): PersonBuilder_JavaStyle {
        this.firstName = firstName
        return this
    }
    fun setLastName(lastName: String): PersonBuilder_JavaStyle {
        this.lastName = lastName
        return this
    }
    fun setAge(age: Int): PersonBuilder_JavaStyle {
        this.age = age
        return this
    }
    fun build() = Person(firstName!!, lastName!!, age)
}

class PersonBuilder {
    private var firstName: String? = null
    private var lastName: String? = null
    private var age: Int? = null

    fun setFirstName(firstName: String) = apply {
        this.firstName = firstName
    }
    fun setLastName(lastName: String) = apply {
        this.lastName = lastName
    }

    fun setAge(age: Int) = apply {
        this.age = age
    }

    fun build() = Person(firstName!!, lastName!!, age)
}