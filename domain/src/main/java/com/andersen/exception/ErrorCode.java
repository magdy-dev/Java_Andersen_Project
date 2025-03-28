package com.andersen.exception;

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
    BK_007("BK_007", "Booking already cancelled"),
    BK_008("BK_008", "Booking retrieval failed"),
    BK_009("BK_009", "Unauthorized booking modification"),
    BK_010("BK_010", "Booking status transition invalid"),

    // Workspace domain errors (WS_XXX)
    WS_001("WS_001", "Workspace creation failed"),
    WS_002("WS_002", "Workspace retrieval failed"),
    WS_003("WS_003", "Workspace not found"),
    WS_004("WS_004", "Workspace update failed"),
    WS_005("WS_005", "Workspace deletion failed"),
    WS_006("WS_006", "Workspace availability check failed"),
    WS_007("WS_007", "Workspace not available"),
    WS_008("WS_008", "Workspace capacity exceeded"),
    WS_009("WS_009", "Workspace already exists"),

    // User domain errors (US_XXX)
    US_001("US_001", "User not found"),
    US_002("US_002", "User creation failed"),
    US_003("US_003", "User update failed"),
    US_004("US_004", "User deletion failed"),
    US_005("US_005", "Unauthorized access"),
    US_006("US_006", "Authentication failed"),
    US_007("US_007", "User retrieval failed"),
    US_008("US_008", "Username already exists"),
    US_009("US_009", "Email already registered"),

    // System/Infrastructure errors (SY_XXX)
    SY_001("SY_001", "Database connection failure"),
    SY_002("SY_002", "Database constraint violation"),
    SY_003("SY_003", "Network communication failure"),
    SY_004("SY_004", "External service unavailable"),
    SY_005("SY_005", "Invalid system configuration"),
    SY_006("SY_006", "IO operation failed"),
    SY_007("SY_007", "Data integrity violation"),
    SY_008("SY_008", "Concurrent modification detected"),

    // Validation errors (VL_XXX)
    VL_001("VL_001", "Invalid input data"),
    VL_002("VL_002", "Missing required field"),
    VL_003("VL_003", "Field value too long"),
    VL_004("VL_004", "Invalid email format"),
    VL_005("VL_005", "Invalid date/time format"),
    VL_006("VL_006", "Value out of range"),
    VL_007("VL_007", "Invalid status transition"),
    VL_008("VL_008", "Invalid identifier format"),

    // Business rule violations (BR_XXX)
    BR_001("BR_001", "Operation violates business rules"),
    BR_002("BR_002", "Past booking modification not allowed"),
    BR_003("BR_003", "Maximum bookings per user exceeded"),
    BR_004("BR_004", "Minimum booking duration not met"),
    BR_005("BR_005", "Maximum booking duration exceeded"),
    BR_006("BR_006", "Outside operating hours"),
    BR_007("BR_007", "Payment required for confirmation"),
    BR_008("BR_008", "Refund policy violation");

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