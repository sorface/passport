package by.sorface.passport.web.services.sleuth

import io.micrometer.tracing.Tracer
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SleuthService(private val tracer: Tracer) {

    fun getSpanId(): String? = tracer.currentTraceContext().context()?.spanId()

    fun getTraceId(): String? = tracer.currentTraceContext().context()?.traceId()

}
