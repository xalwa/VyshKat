package service

import model.Bicycle
import model.ChargingStation
import model.Helmet
import model.Scooter
import model.Transport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FixedServiceCenter(
    private val accepted: Boolean
) : ServiceCenter {

    override fun inspect(transport: Transport): Boolean {
        return accepted
    }
}

class FleetServiceTest {

    @Test
    fun acceptedTransportIsStored() {
        val serviceCenter = FixedServiceCenter(true)
        val fleet = FleetService(serviceCenter)
        val scooter = Scooter("S-001", "Самокат", 7, 0.5)

        val result = fleet.addTransport(scooter)

        assertEquals(AdmissionResult.ADDED, result)
        assertEquals(listOf(scooter), fleet.inventory())
    }

    @Test
    fun rejectedTransportIsNotStored() {
        val serviceCenter = FixedServiceCenter(false)
        val fleet = FleetService(serviceCenter)
        val scooter = Scooter("S-001", "Самокат", 7, 0.5)

        val result = fleet.addTransport(scooter)

        assertEquals(AdmissionResult.REJECTED, result)
        assertTrue(fleet.inventory().isEmpty())
    }

    @Test
    fun duplicateNumbersDoNotChangeInventory() {
        val fleet = FleetService(FixedServiceCenter(true))
        val scooter = Scooter("S-001", "Самокат", 7, 0.5)
        fleet.addTransport(scooter)

        val helmetResult = fleet.addEquipment(
            Helmet("S-001", "Шлем")
        )

        val bicycleResult = fleet.addTransport(
            Bicycle("S-001", "Велосипед", 8)
        )

        assertEquals(AdmissionResult.DUPLICATE_NUMBER, helmetResult)
        assertEquals(AdmissionResult.DUPLICATE_NUMBER, bicycleResult)
        assertEquals(listOf(scooter), fleet.inventory())
    }

    @Test
    fun energyIncludesStationAndExcludesRejectedTransport() {
        val fleet = FleetService(BasicServiceCenter())

        fleet.addTransport(
            Scooter("S-001", "Исправный", 7, 0.5)
        )
        fleet.addEquipment(
            ChargingStation("C-001", "Станция", 1.5)
        )
        fleet.addEquipment(Helmet("H-001", "Шлем"))

        val brokenScooter = Scooter("S-002", "Сломанный", 8, 9.0)
        brokenScooter.isOperational = false

        val result = fleet.addTransport(brokenScooter)

        assertEquals(AdmissionResult.REJECTED, result)
        assertEquals(2.0, fleet.totalDailyEnergyKwh(), 0.000001)
    }

    @Test
    fun beginnerVehiclesIncludeScoreSixButExcludeFive() {
        val fleet = FleetService(FixedServiceCenter(true))

        fleet.addTransport(Bicycle("B-005", "Сложный", 5))
        fleet.addTransport(Bicycle("B-006", "Простой", 6))
        fleet.addEquipment(Helmet("H-001", "Шлем"))

        val numbers = fleet.beginnerVehicles().map { transport ->
            transport.inventoryNumber
        }

        assertEquals(listOf("B-006"), numbers)
    }
}
