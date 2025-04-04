package com.andersen.service.exception.errorcode;

public enum ErrorCode {

    // General Error Codes
    GENERAL_ERROR("GE_001", "A general error has occurred"),

    // Booking-related Error Codes
    BK_001("BK_001", "Booking not found"),
    BK_002("BK_002", "Unauthorized access to booking"),
    BK_003("BK_003", "Booking creation error"),
    BK_004("BK_004", "Invalid booking parameters"),

    // Workspace-related Error Codes
    WS_001("WS_001", "Workspace not found"),
    WS_002("WS_002", "Invalid workspace parameters"),
    WS_003("WS_003", "Workspace deactivation error"),
    WS_004("WS_004", "No available workspaces"),

    // Authentication-related Error Codes
    AU_001("AU_001", "Invalid username or password"),
    AU_002("AU_002", "Username already exists"),
    AU_003("AU_003", "Email already exists"),
    AU_004("AU_004", "Registration error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}