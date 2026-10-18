package model

class ChargingStation(
    inventoryNumber: String,
    name: String,
    override val dailyEnergyKwh: Double
) : Equipment(inventoryNumber, name), EnergyConsumer {
    init {
        require(
            dailyEnergyKwh.isFinite() && dailyEnergyKwh >= 0.0
        ) {
            "Расход энергии должен быть конечным и неотрицательным"
        }
    }
}
