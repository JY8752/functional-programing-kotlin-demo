package demo2

import arrow.core.Either
import arrow.core.andThen
import arrow.core.curried
import arrow.core.right

data class Student(val group: String, val name: String, val age: Int)

fun findStudent(group: String) =
    if(group == "A")
        Student(group, "Alice", 20).right()
    else
        Either.Left("Student not found")

fun csv(useQuotes: Boolean, student: Student) =
    if(useQuotes)
        "\"${student.group}\",\"${student.name}\",\"${student.age}\""
    else
        "${student.group},${student.name},${student.age}"

val curryCsv = ::csv.curried()

//fun getGroup(id: String) = id.first().toString()
fun getGroup(id: String): String {
    if(id.isEmpty()) {
        return ""
    }
    return id.first().toString()
}

fun printStudent(id: String) = ::getGroup
    .andThen(::findStudent)
    .andThen { it.map(curryCsv(true)) }
    .andThen {either ->
        either.fold(
            { println("Error: $it") },
            { println(it) }
        )
    }
    .invoke(id)

fun main() {
    val id = "A-001-00001"
    printStudent(id)
}