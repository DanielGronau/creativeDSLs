package creativeDSLs.chapter_06

data class Contact(val name: String, val phone: String?)

data class Person(val name: String, val phone: String, val contacts: List<Contact>)

class PersonBuilder {
    private var name: String? = null
    private var phone: String? = null
    private var addingContact = false
    private var contactName: String? = null
    private var contactPhone: String? = null
    private val contacts: MutableList<Contact> = mutableListOf()

    fun beginContact() = apply {
        require(!addingContact)
        addingContact = true
    }

    fun endContact() = apply {
        require(addingContact)
        contacts.add(Contact(contactName!!, contactPhone))
        contactName = null
        contactPhone = null
        addingContact = false
    }

    fun setName(name: String) = apply {
        if (addingContact) this.contactName = name else this.name = name
    }

    fun setPhone(phone: String) = apply {
        if (addingContact) this.contactPhone = phone else this.phone = phone
    }

    fun build(): Person {
        require(!addingContact)
        return Person(name!!, phone!!, contacts)
    }
}

fun main() {
    val superman = PersonBuilder()
        .setName("Superman")
        .beginContact().setName("Wonder Woman").endContact()
        .setPhone("555-3213-125")
        .beginContact().setName("Lois Lane").setPhone("555-4112-423").endContact()
        .build()

    println(superman)
}