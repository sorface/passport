package by.sorface.passport.web.services.sleuth

import io.micrometer.tracing.Tracer
import org.springframework.stereotype.Service

@Service
class SleuthService(private val tracer: Tracer) {

    fun getSpanId(): String? = tracer.currentTraceContext().context()?.spanId()

    fun getTraceId(): String? = tracer.currentTraceContext().context()?.traceId()

}
