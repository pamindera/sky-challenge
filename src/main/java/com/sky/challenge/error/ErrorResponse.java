package com.sky.challenge.error;

public record ErrorResponse(String error, String message) {

    @Override
    public String toString() {
        return String.format("{\"error\":\"%s\", \"message\":\"%s\"}", error, message);
    }
}
