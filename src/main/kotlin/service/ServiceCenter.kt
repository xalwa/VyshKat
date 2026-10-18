package service

import model.Transport

interface ServiceCenter {
    fun inspect(transport: Transport): Boolean
}
