package demo2

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

internal class Demo2Test : FunSpec({
    context("findStudent") {
        test("グループがA以外の指定のときはStudent not foundが返る") {
            checkAll(Arb.stringPattern("[^A]")) { group ->
                println(group)
                val student = findStudent(group)
                student shouldBeLeft "Student not found"
            }
        }
        test("グループがAのときStudentが返る") {
            val student = findStudent("A")
            student shouldBeRight Student("A", "Alice", 20)
        }
    }
    context("csv") {
        test("useQuotesがtrueのときはダブルクォーテーションで囲まれた文字列が返る") {
            checkAll<String, String, Int> {group, name, age ->
                val student = Student(group, name, age)
                val csv = csv(true, student)
                csv shouldBe "\"$group\",\"$name\",\"$age\""
            }
        }
        test("useQuotesがfalseのときはダブルクォーテーションで囲まれない文字列が返る") {
            checkAll<String, String, Int> {group, name, age ->
                val student = Student(group, name, age)
                val csv = csv(false, student)
                csv shouldBe "$group,$name,$age"
            }
        }
    }
    context("getGroup") {
        test("idの先頭の文字が返る") {
            checkAll<String> { id ->
                val group = getGroup(id)
                group shouldBe if (id.isEmpty()) "" else id.first().toString()
            }
        }
    }
})