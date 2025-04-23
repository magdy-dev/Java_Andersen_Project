package com.andersen.domain.exception.errorCode;

/**
 * Standardized error codes for the booking system
 * Format: [Domain]_[ErrorType]_[SpecificCode]
 */
public enum ErrorCode {
    // Booking domain errors (BK_XXX)
    BK_001("BK_001", "Booking creation failed"),
    BK_002("BK_002", "Booking not found"),
    BK_003("BK_003", "Booking update failed"),
    BK_004("BK_004", "Booking deletion failed"),
    BK_005("BK_005", "Booking time conflict"),
    BK_006("BK_006", "Invalid booking time range"),

    // Workspace domain errors (WS_XXX)
    WS_001("WS_001", "Workspace creation failed"),
    WS_002("WS_002", "Workspace retrieval failed"),
    WS_003("WS_003", "Workspace not found"),
    WS_004("WS_004", "Workspace update failed"),
    WS_005("WS_005", "Workspace deletion failed"),
    WS_006("WS_006", "Workspace availability check failed"),

    // User domain errors (US_XXX)
    US_001("US_001", "User not found"),
    US_002("US_002", "User creation failed"),
    US_003("US_003", "User update failed"),
    US_004("US_004", "User deletion failed"),
    US_005("US_005", "Unauthorized access"),
    US_006("US_006", "Authentication failed"),
    US_007("US_007", "User retrieval failed");



    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

    /**
     * Finds an ErrorCode by its code value
     * @param code The error code to search for
     * @return Matching ErrorCode or null if not found
     */
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}