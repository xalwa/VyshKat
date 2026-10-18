package model

class Scooter(
    inventoryNumber: String,
    name: String,
    beginnerScore: Int,
    override val dailyEnergyKwh: Double
) : Transport(
    inventoryNumber,
    name,
    beginnerScore
), EnergyConsumer {
    init {
        require(
            dailyEnergyKwh.isFinite() && dailyEnergyKwh >= 0.0
        ) {
            "Расход энергии должен быть конечным и неотрицательным"
        }
    }
}
