package by.sorface.passport.web.services.sleuth;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SleuthService {

    private final Tracer tracer;

    public String getSpanId() {
        return getTraceContext()
                .map(io.micrometer.tracing.TraceContext::spanId)
                .orElse(null);
    }

    public String getTraceId() {
        return getTraceContext()
                .map(io.micrometer.tracing.TraceContext::traceId)
                .orElse(null);
    }

    private Optional<io.micrometer.tracing.TraceContext> getTraceContext() {
        return Optional.of(tracer.currentTraceContext()).map(CurrentTraceContext::context);
    }
}
