package service

import model.Transport

class BasicServiceCenter : ServiceCenter {
    override fun inspect(transport: Transport): Boolean {
        return transport.isOperational
    }
}
