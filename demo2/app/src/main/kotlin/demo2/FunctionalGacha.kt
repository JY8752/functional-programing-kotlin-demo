package demo2

import arrow.core.*

data class FGacha(val ids: List<Int>, val itemMap: Map<Int, List<Item>>)

sealed class GachaException(message: String) : Exception(message) {
    object GachaNotFoundException : GachaException("ガチャが見つかりません")
    object GachaDrawException : GachaException("ガチャの抽選に失敗しました")
}

fun FGacha.getItems(id: Int): Either<GachaException, List<Item>> {
    return this.itemMap[id]?.right() ?: GachaException.GachaNotFoundException.left()
}

fun getTotalWeight(items: List<Item>): Int {
    return items.sumOf { it.weight }
}

fun random(totalWeight: Int): Int {
    return (Math.random() * totalWeight).toInt()
}

fun getItem(random: Int, items: List<Item>): Either<GachaException, Item> {
    var weight = 0
    for (i in items.indices) {
        weight += items[i].weight
        if (random < weight) {
            return items[i].right()
        }
    }
    return GachaException.GachaDrawException.left()
}

fun FGacha.draw(id: Int) =
    this.getItems(id)
        .map(::getTotalWeight)
        .map(::random)
        .flatMap { random -> getItem(random, this.getItems(id).getOrNull() ?: emptyList()) }

fun main() {
    val gacha = FGacha(
        listOf(1, 2, 3),
        mapOf(
            1 to listOf(
                Item(1, "アイテム1", Rarity.N, 100),
                Item(2, "アイテム2", Rarity.N, 100),
                Item(3, "アイテム3", Rarity.N, 100),
                Item(4, "アイテム4", Rarity.R, 50),
                Item(5, "アイテム5", Rarity.SR, 10)
            ),
            2 to listOf(
                Item(6, "アイテム6", Rarity.N, 100),
                Item(7, "アイテム7", Rarity.N, 100),
                Item(8, "アイテム8", Rarity.N, 100),
                Item(9, "アイテム9", Rarity.R, 50),
                Item(10, "アイテム10", Rarity.SR, 10)
            ),
            3 to listOf(
                Item(11, "アイテム11", Rarity.N, 50),
                Item(12, "アイテム12", Rarity.R, 30),
                Item(13, "アイテム13", Rarity.SR, 15),
                Item(14, "アイテム14", Rarity.SSR, 5),
            )
        )
    )

    val result = when (val item = gacha.draw(4)) {
        is Either.Left -> throw item.value
        is Either.Right -> "ガチャの抽選に成功しました: ${item.value}"
    }
    println (result)
}