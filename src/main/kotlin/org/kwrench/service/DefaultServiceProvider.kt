package org.kwrench.service

import java.util.*

/**
 *
 */
open class DefaultServiceProvider(): ServiceBase(), ServiceProvider {

    private var parentServiceProvider: ServiceProvider? = null
    override val servicesMap = LinkedHashMap<Class<out Service>, Service>()

    override fun doInit(serviceProvider: ServiceProvider?) {
        parentServiceProvider = serviceProvider

        for (service in services) {
            service.init(this)
        }
    }

    override fun doShutdown() {
        for (service in services.reversed()) {
            service.shutdown()
        }
    }

    override fun <T: Service> getService(serviceType: Class<T>): T? {
        val service = servicesMap[serviceType]
        return if (service != null) service as T
               else parentServiceProvider?.getService(serviceType)
    }

    override fun <T: Service> addService(service: T): T {
        val key = service.javaClass
        val existingService = getService(key)
        if (existingService != null) throw IllegalArgumentException("A service of type $key is already registered with this service provider (or its parent)")

        servicesMap.put(key, service)
        if (active) service.init(this)
        return service
    }

    override fun <T: Service> removeService(serviceType: Class<T>): T? {
        val existingService = servicesMap.remove(serviceType)

        if (existingService != null) {
            if (existingService.active) existingService.shutdown()
        }

        return existingService as T?
    }
}