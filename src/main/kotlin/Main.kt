import di.appModule
import model.Bicycle
import model.ChargingStation
import model.Helmet
import model.InventoryItem
import model.Scooter
import org.koin.dsl.koinApplication
import service.AdmissionResult
import service.FleetService
import service.ServiceCenter

fun printItem(item: InventoryItem) {
    println("${item.inventoryNumber}: ${item.name}")
}

fun printAdmissionResult(
    item: InventoryItem,
    result: AdmissionResult
) {
    val message = when (result) {
        AdmissionResult.ADDED -> "Добавлено в парк"
        AdmissionResult.REJECTED -> "Отказ техосмотра"
        AdmissionResult.DUPLICATE_NUMBER -> "Номер уже используется"
    }

    println("${item.inventoryNumber}: $message")
}

fun main() {
    val app = koinApplication {
        modules(appModule())
    }

    try {
        val serviceCenter = app.koin.get<ServiceCenter>()
        val fleet = app.koin.get<FleetService>()

        runDemo(fleet, serviceCenter)
    } finally {
        app.close()
    }
}

fun runDemo(
    fleet: FleetService,
    serviceCenter: ServiceCenter
) {
    val scooter = Scooter(
        inventoryNumber = "S-001",
        name = "Городской самокат",
        beginnerScore = 6,
        dailyEnergyKwh = 0.5
    )

    printItem(scooter)
    println("Подходит новичкам: ${scooter.isBeginnerFriendly()}")

    println("Прошёл техосмотр: ${serviceCenter.inspect(scooter)}")

    scooter.isOperational = false
    println("После поломки прошёл техосмотр: ${serviceCenter.inspect(scooter)}")

    val bicycle = Bicycle(
        inventoryNumber = "B-001",
        name = "Обычный велосипед",
        beginnerScore = 5
    )

    println()
    printItem(bicycle)
    println("Подходит новичкам: ${bicycle.isBeginnerFriendly()}")
    println("Прошёл техосмотр: ${serviceCenter.inspect(bicycle)}")

    println()
    printAdmissionResult(scooter, fleet.addTransport(scooter))
    printAdmissionResult(bicycle, fleet.addTransport(bicycle))

    val helmet = Helmet("H-001", "Защитный шлем")
    val station = ChargingStation(
        "C-001",
        "Зарядная станция",
        1.5
    )

    printAdmissionResult(helmet, fleet.addEquipment(helmet))
    printAdmissionResult(station, fleet.addEquipment(station))

    for (item in fleet.inventory()) {
        printItem(item)
    }

    val totalEnergy = fleet.totalDailyEnergyKwh()
    println("Суточное энергопотребление парка: $totalEnergy кВт·ч")

    val secondScooter = Scooter(
        inventoryNumber = "S-002",
        name = "Исправный самокат",
        beginnerScore = 7,
        dailyEnergyKwh = 0.5
    )

    printAdmissionResult(secondScooter, fleet.addTransport(secondScooter))
    println(
        "После приёмки энергопотребление парка: ${fleet.totalDailyEnergyKwh()} кВт·ч"
    )

    println()
    println("Проверка повторных номеров:")
    printAdmissionResult(secondScooter, fleet.addTransport(secondScooter))

    val duplicateHelmet = Helmet(
        inventoryNumber = "S-002",
        name = "Шлем с занятым номером"
    )

    printAdmissionResult(duplicateHelmet, fleet.addEquipment(duplicateHelmet))

    println()
    println("Транспорта в парке: ${fleet.transportCount()}")
    println("Вещей на балансе: ${fleet.equipmentCount()}")

    val beginnerVehicles = fleet.beginnerVehicles()

    println("Транспорт для новичков:")

    if (beginnerVehicles.isEmpty()) {
        println("Подходящего транспорта пока нет")
    } else {
        for (transport in beginnerVehicles) {
            printItem(transport)
        }
    }
}
