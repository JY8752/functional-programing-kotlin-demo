package demo2

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

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

})