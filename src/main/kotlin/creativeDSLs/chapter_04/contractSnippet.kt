package creativeDSLs.chapter_04

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
fun String?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}

fun bar(x: String?) {
    if (!x.isNullOrEmpty()) {
        println("length of '$x' is ${x.length}") // smartcast
    }
}

sealed class ContactData {
    @OptIn(ExperimentalContracts::class)
    fun isEmail(): Boolean {
        contract {
            returns(true) implies (this@ContactData is Email)
            returns(false) implies (this@ContactData is Phone)
        }
        return this is Email
    }
}

data class Email(val address: String): ContactData() {
    fun send(msg: String) = println("sending '$msg' to $address")
}
data class Phone(val number: String): ContactData() {
    fun call(msg: String) = println("calling $number telling '$msg'")
}

fun sendMessage(cd: ContactData, msg: String) =
    if (cd.isEmail()) cd.send(msg) else cd.call(msg)


fun main() {
    bar("edfdf")
    bar(null)

    sendMessage(Email("john@acme.com"), "Hi John!")
}