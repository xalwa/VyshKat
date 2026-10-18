package di

import org.koin.dsl.module
import service.BasicServiceCenter
import service.FleetService
import service.ServiceCenter

fun appModule() = module {
    single<ServiceCenter> {
        BasicServiceCenter()
    }

    single {
        FleetService(get())
    }
}
