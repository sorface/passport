package by.sorface.passport.web.api.advices

import by.sorface.passport.web.exceptions.*
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.util.*
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
class ExceptionAdviceTest {

    @Mock
    private lateinit var sleuthService: SleuthService

    @Mock
    private lateinit var localeI18Service: LocaleI18Service

    @InjectMocks
    private lateinit var exceptionAdvice: ExceptionAdvice

    companion object {
        @JvmStatic
        private fun globalExceptionExceptionStore(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(GlobalSystemException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR, HttpStatus.BAD_REQUEST)),
                Arguments.of(UserRequestException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR, args = mapOf())),
                Arguments.of(NotFoundException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR, args = mapOf())),
                Arguments.of(ObjectExpiredException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR)),
                Arguments.of(ObjectInvalidException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR)),
                Arguments.of(UnauthorizedException(I18Codes.I18GlobalCodes.UNKNOWN_ERROR))
            )
        }
    }

    @ParameterizedTest
    @MethodSource("globalExceptionExceptionStore")
    fun handleUserRequestException(exception: GlobalSystemException) {
        val traceId = UUID.randomUUID().toString()
        val spanId = UUID.randomUUID().toString()

        Mockito.`when`(sleuthService.getSpanId()).thenReturn(spanId)
        Mockito.`when`(sleuthService.getTraceId()).thenReturn(traceId)
        Mockito.`when`(localeI18Service.getI18Message(exception.i18Code, exception.args)).thenReturn("Unknown error")

        val response = exceptionAdvice.handleUserRequestException(exception)

        Mockito.verify(sleuthService).getSpanId()
        Mockito.verify(sleuthService).getTraceId()
        Mockito.verify(localeI18Service).getI18Message(exception.i18Code, exception.args)

        assertEquals(exception.httpStatus, response.statusCode)

        val operationError = response.body

        assertNotNull(operationError)

        assertEquals("Unknown error", operationError?.message)
        assertEquals(spanId, operationError?.spanId)
        assertEquals(traceId, operationError?.traceId)
        assertEquals(exception.httpStatus.value(), operationError?.code)
    }

    @Test
    fun handleValidateException() {
    }

    @Test
    fun handleAccessDenied() {
    }

    @Test
    fun handleInsufficientAuthentication() {
    }

    @Test
    fun handleAuthenticationException() {
    }

    @Test
    fun handleNotFoundException() {
    }

    @Test
    fun testHandleValidateException() {
    }
}