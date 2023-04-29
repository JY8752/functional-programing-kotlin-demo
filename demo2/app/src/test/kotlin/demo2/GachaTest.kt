package demo2

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

internal class GachaTest : FunSpec({
    context("draw") {
        val gacha = Gacha(
            listOf(1, 2),
            mapOf(
                1 to listOf(
                    Item(1, "アイテム1", Rarity.N, 100),
                ),
                2 to listOf(
                    Item(1, "アイテム2", Rarity.R, 0),
                    Item(2, "アイテム2", Rarity.SR, 10)
                )
            )
        )

        data class TestPattern(
            val name: String,
            val id: Int,
            val expected: Item,
            val isError: Boolean
        )

        withData(
            nameFn = { it.name },
            TestPattern("id=1 アイテム1が取得できること", 1, Item(1, "アイテム1", Rarity.N, 100), false),
            TestPattern("id=2 アイテム2が取得できること", 2, Item(2, "アイテム2", Rarity.SR, 10), false),
            TestPattern("id=3 エラーになること", 3, Item(0, "", Rarity.N, 0), true),
        ) { (_, id, expected, isError) ->
            if (isError) {
                shouldThrow<IllegalArgumentException> {
                    gacha.draw(id)
                }
            } else {
                gacha.draw(id) shouldBe expected
            }
        }
    }
    context("drawRandom") {
        test("ランダムにアイテムが取得できること") {
            val gacha = Gacha(
                listOf(1),
                mapOf(
                    1 to listOf(
                        Item(1, "アイテム1", Rarity.N, 100),
                    ),
                )
            )
            gacha.drawRandom() shouldBe Item(1, "アイテム1", Rarity.N, 100)
        }
    }
})