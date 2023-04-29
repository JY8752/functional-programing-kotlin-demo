package demo2

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

internal class FunctionalGachaTest : FunSpec({
    context("getItems") {
        data class TestPattern(
            val name: String,
            val gacha: FGacha,
            val id: Int,
            val expected: Either<GachaException, List<Item>>,
        )

        withData(
            nameFn = { it.name },
            TestPattern(
                "id=1 アイテム一覧が取得できること",
                FGacha(
                    listOf(1),
                    mapOf(
                        1 to listOf(
                            Item(1, "アイテム1", Rarity.N, 100),
                            Item(2, "アイテム2", Rarity.R, 50),
                        ),
                    )
                ),
                1,
                listOf(
                    Item(1, "アイテム1", Rarity.N, 100),
                    Item(2, "アイテム2", Rarity.R, 50),
                ).right()
            ),
            TestPattern(
                "id=2 存在しないidを指定し、GachaNotFoundExceptionが発生すること",
                FGacha(
                    listOf(1),
                    mapOf(
                        1 to listOf(
                            Item(1, "アイテム1", Rarity.N, 100),
                            Item(2, "アイテム2", Rarity.R, 50),
                        ),
                    )
                ),
                2,
                GachaException.GachaNotFoundException.left()
            ),
        ) { (_, gacha, id, expected) ->
            gacha.getItems(id) shouldBe expected
        }
    }
    test("getTotalWeight") {
        checkAll<Int, Int> {a, b ->
            val items = listOf(
                Item(1, "アイテム1", Rarity.N, a),
                Item(2, "アイテム2", Rarity.R, b),
            )
            getTotalWeight(items) shouldBe a + b
        }
    }
    context("random") {
        test("0以上totalWeight未満の値がランダムに取得できること") {
            checkAll(Arb.positiveInt()) { totalWeight ->
                random(totalWeight) shouldBeInRange (0 until totalWeight)
            }
        }
        test("totalWeightが0のとき") {
            random(0) shouldBe 0
        }
    }
    context("getItem") {
        test("最初のアイテムが取得できること") {
            checkAll(Arb.nonNegativeInt(max = 1_000)) { random ->
                val items = listOf(
                    Item(1, "アイテム1", Rarity.N, random + 1),
                    Item(2, "アイテム2", Rarity.R, 50),
                )
                val result = getItem(random, items)

                result.isRight() shouldBe true
                result.getOrNull() shouldBe items.first()
            }
        }
        test("２つ目のアイテムが取得できること") {
            checkAll(Arb.int(min = 10, max = 1000)) { random ->
                val items = listOf(
                    Item(1, "アイテム1", Rarity.SSR, 5),
                    Item(2, "アイテム2", Rarity.R, 5 + random),
                )
                val result = getItem(random, items)

                result.isRight() shouldBe true
                result.getOrNull() shouldBe items[1]
            }
        }
    }
})