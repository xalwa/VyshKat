package model

abstract class Equipment(
    override val inventoryNumber: String,
    override val name: String
) : InventoryItem {
    init {
        require(inventoryNumber.isNotBlank()) {
            "Инвентарный номер не должен быть пустым"
        }

        require(name.isNotBlank()) {
            "Название не должно быть пустым"
        }
    }
}
