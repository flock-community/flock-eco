package community.flock.eco.core.services

import community.flock.eco.core.events.Event
import org.springframework.stereotype.Service

@Service
class EventService {

    private val listeners: MutableMap<Class<out Event>, MutableList<(Event) -> Unit>> = mutableMapOf()

    fun registerEventListener(clazz: Class<out Event>, listener: (event: Event) -> Unit) {
        listeners.getOrPut(clazz) { mutableListOf() }.add(listener)
    }

    fun emitEvent(event: Event) {
        listeners[event.javaClass]!!.forEach { it(event) }
    }

}
