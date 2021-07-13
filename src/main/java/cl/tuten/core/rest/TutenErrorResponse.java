package cl.tuten.core.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

/**
 * @author gustavo
 */
public class TutenErrorResponse extends TutenResponse {

    private String errorCode;
    private String error;
    private List<String> errorMessages;

    public String getErrorCode() {
        return errorCode;
    }

    private void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public static Response getAccepted(Status status, String error, String errorCode) {
        TutenErrorResponse r = new TutenErrorResponse();
        r.setStatusCode(status.getStatusCode());
        r.setStatus(status.toString());
        r.setError(error);
        r.setErrorCode(errorCode);
        return Response.status(Status.ACCEPTED).entity(r).build();
    }

    public static Response badRequest(String error, String errorCode) {
        final Status status = Status.BAD_REQUEST;
        return getResponse(error, errorCode, status);
    }

    public static Response serverError(String error, String errorCode) {
        final Status status = Status.INTERNAL_SERVER_ERROR;
        return getResponse(error, errorCode, status);
    }

    private static Response getResponse(String error, String errorCode, Status status) {
        TutenErrorResponse r = new TutenErrorResponse();
        r.setStatusCode(status.getStatusCode());
        r.setStatus(status.toString());
        r.setError(error);
        r.setErrorCode(errorCode);
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(r).build();
    }

    public static Response unauthorized(String error, String errorCode) {
        final Status status = Status.UNAUTHORIZED;
        return getResponse(error, errorCode, status);
    }

    public static Response build(final Status status, String error, String errorCode) {
        return getResponse(error, errorCode, status);
    }

    public static Response badRequest(ERROR error, List<String> errors) {
        final Status status = Status.BAD_REQUEST;
        TutenErrorResponse r = new TutenErrorResponse();
        r.setStatusCode(status.getStatusCode());
        r.setStatus(status.toString());
        r.setError(error.getValue());
        r.setErrorMessages(errors);
        r.setErrorCode(error.name());
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(r).build();
    }

    public enum ERROR {
        HAS_NULLS("Hay parámetros nulos"),
        INVALID_APP("Campo app inválido"),
        INVALID_PERMISSION("Permiso declarado no válido"),
        INVALID_TOKEN("Token inválido o sesión expirada"),
        INVALID_USER("Usuario no existe"),
        MISSING_PERMISSION("Usted no tiene el permiso necesario para ejecutar esta acción"),
        PARTIALLY_OK_WITH_ERRORS("Se procesaron los valores parcialmente, algunos con error.");

        private final String value;

        private ERROR(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ERROR_BLOCK {
        PROFESSIONAL_NULL("El id professional ingresado es inválido"),
        START_DATE_GREATHER_THAN_FINISHDATE("La fecha de inicio es mayor que la del fin"),
        BUSY_BOOKING_RANGE("El técnico tiene servicios agendados para el rango de fecha indicado"),
        BUSY_BLOCK_RANGE("El tecinico tiene bloques de indisponibilidad en el rango de fecha indicado"),
        INVALID_DATES("Debe ingresar fechas válidas"),
        DATE_OUT_OF_RANGE("No puede seleccionar un hora fuera del horario laboral"),
        REASON_EMPTY("No se especifico la razon"),
        BLOCK_DELETED("El bloque ha sido eliminado");

        private final String value;

        private ERROR_BLOCK(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
