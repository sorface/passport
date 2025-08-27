package by.sorface.idp.records.events

import org.springframework.context.ApplicationEvent

class SessionRefreshEvent(val sessionId: String) : ApplicationEvent(sessionId)
