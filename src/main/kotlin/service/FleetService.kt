package service

import model.EnergyConsumer
import model.Equipment
import model.InventoryItem
import model.Transport

class FleetService(
    private val serviceCenter: ServiceCenter
) {
    private val items = mutableListOf<InventoryItem>()

    private fun hasInventoryNumber(number: String): Boolean {
        return items.any { item ->
            item.inventoryNumber == number
        }
    }

    fun addTransport(transport: Transport): AdmissionResult {
        if (hasInventoryNumber(transport.inventoryNumber)) {
            return AdmissionResult.DUPLICATE_NUMBER
        }

        if (!serviceCenter.inspect(transport)) {
            return AdmissionResult.REJECTED
        }

        items.add(transport)
        return AdmissionResult.ADDED
    }

    fun addEquipment(equipment: Equipment): AdmissionResult {
        if (hasInventoryNumber(equipment.inventoryNumber)) {
            return AdmissionResult.DUPLICATE_NUMBER
        }

        items.add(equipment)
        return AdmissionResult.ADDED
    }

    fun inventory(): List<InventoryItem> {
        return items.toList()
    }

    fun totalDailyEnergyKwh(): Double {
        val consumers: List<EnergyConsumer> =
            items.filterIsInstance<EnergyConsumer>()

        return consumers.sumOf { consumer ->
            consumer.dailyEnergyKwh
        }
    }

    fun transportCount(): Int {
        return items.count { item ->
            item is Transport
        }
    }

    fun equipmentCount(): Int {
        return items.count { item ->
            item is Equipment
        }
    }

    fun beginnerVehicles(): List<Transport> {
        val vehicles: List<Transport> =
            items.filterIsInstance<Transport>()

        return vehicles.filter { transport ->
            transport.isBeginnerFriendly()
        }
    }
}
