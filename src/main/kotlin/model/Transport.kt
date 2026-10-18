package model

abstract class Transport(
    override val inventoryNumber: String,
    override val name: String,
    val beginnerScore: Int
) : InventoryItem {
    var isOperational: Boolean = true

    init {
        require(inventoryNumber.isNotBlank()) {
            "Инвентарный номер не должен быть пустым"
        }

        require(name.isNotBlank()) {
            "Название не должно быть пустым"
        }

        require(beginnerScore in 1..10) {
            "Простота должна быть от 1 до 10"
        }
    }

    fun isBeginnerFriendly(): Boolean {
        return beginnerScore >= 6
    }
}
