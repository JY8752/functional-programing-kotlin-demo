package demo2

data class Item(
    val id: Int,
    val name: String,
    val rarity: Rarity,
    val weight: Int
)

enum class Rarity(val value: Int) {
    N(1),
    R(2),
    SR(3),
    SSR(4)
}

class Gacha(
    private val ids: List<Int>,
    private val items: Map<Int, List<Item>>
) {
    // 指定のidのガチャを引く
    fun draw(id: Int): Item {
        val item = this.items[id] ?: throw IllegalArgumentException("id=$id のガチャは存在しません")
        val totalWeight = item.sumOf { it.weight }
        val random = (Math.random() * totalWeight).toInt()
        var weight = 0
        for (i in item.indices) {
            weight += item[i].weight
            if (random < weight) {
                return item[i]
            }
        }
        throw IllegalStateException("ガチャの抽選に失敗しました")
    }

    // ランダムにガチャを引く
    fun drawRandom(): Item {
        val id = this.ids.random()
        return this.draw(id)
    }

    // 指定のガチャのアイテム一覧を取得する
    fun list(id: Int): List<Item> {
        return this.items[id] ?: throw IllegalArgumentException("id=$id のガチャは存在しません")
    }

    // ガチャのID一覧を取得する
    fun listIds(): List<Int> {
        return this.ids
    }
}