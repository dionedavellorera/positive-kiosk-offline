package com.nerdvana.positiveoffline.model;

public class ErrorDataModel {
    private String errorMessage;
    private boolean isValid;

    public ErrorDataModel(String errorMessage, boolean isValid) {
        this.errorMessage = errorMessage;
        this.isValid = isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValid() {
        return isValid;
    }
}
