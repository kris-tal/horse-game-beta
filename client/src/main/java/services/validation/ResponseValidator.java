package services.validation;

public interface ResponseValidator {
    boolean isValidJson(String responseBody);
    boolean isSuccessfulResponse(String responseBody);
    String extractErrorMessage(String responseBody);
}







