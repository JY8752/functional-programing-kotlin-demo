package demo2

import arrow.core.*
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.fold
import arrow.optics.copy
import arrow.optics.optics
import java.util.*

object UserNotFound

data class User(val id: Long)

//fun findUserById(id: Int): Either<UserNotFound, User> {
//    return if (id == 1) {
//        User(1).right()
//    } else {
//        UserNotFound.left()
//    }
//}

fun findUserById(id: Int) = either {
    ensure(id == 1) { UserNotFound }
    User(1)
//    user(id)
}

fun Raise<UserNotFound>.user(id: Int): User {
    return if(id == 1) {
        User(1)
    } else {
        raise(UserNotFound)
    }
}

fun optionExample() {
    val a = 1.some()
    val b = none<Int>()

    println("a: ${a.getOrNull()}")
    println("b: ${b.getOrElse { "default" }}")

    val c = Option.fromNullable(1)
    val d = Option.fromNullable(null)

    println("c: ${c.getOrNull()}")
    println("d: ${d.getOrElse { "default" }}")
}

//fun <A> List<A>.firstOrElse(default: () -> A): A = firstOrNull() ?: default()

fun <A> List<A>.firstOrElse(default: () -> A): A =
    when(val option = firstOrNone()) {
        is Some -> option.value
        None -> default()
    }

fun example() {
    val a = emptyList<Int?>().firstOrElse { -1 } // -1
    val b = listOf(1, null, 3).firstOrElse { -1 } // 1
    val c = listOf(null, 2, 3).firstOrElse { -1 } // expect null but -1

    println("a: $a")
    println("b: $b")
    println("c: $c")
}

//data class Person(val name: String, val age: Int, val address: Address)
//data class Address(val street: Street, val city: City)
//data class Street(val name: String, val number: Int?)
//data class City(val name: String, val country: String)

@optics data class Person(val name: String, val age: Int, val address: Address) {
    companion object
}
@optics
data class Address(val street: Street, val city: City) {
    companion object
}
@optics data class Street(val name: String, val number: Int?) {
    companion object
}
@optics data class City(val name: String, val country: String) {
    companion object
}

fun Person.capitalizeCountryModify(): Person =
    Person.address.city.country.modify(this) {country ->
        country.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

fun lensesExample() {
    val person = Person("John", 30, Address(Street("Main", 42), City("London", "uk")))
    val modifiedPerson = person.capitalizeCountryModify()
    println("person: $person")
    println("modifiedPerson: $modifiedPerson")
}

fun main() {
//    val user = when (val result = findUserById(1)) {
//        is Either.Left -> {
//            println("User not found")
//            return
//        }
//        is Either.Right -> result.value
//    }
//    println("User found: $user")
//    optionExample()
    lensesExample()
}