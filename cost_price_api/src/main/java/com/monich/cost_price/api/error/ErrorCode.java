package com.monich.cost_price.api.error;

public enum ErrorCode {

    UNKNOWN_ERROR,
    HANDLER_NOT_FOUND,

    EMAIL_ALREADY_EXISTS,
    PHONE_ALREADY_EXISTS,
    EMAIL_AND_PHONE_LOCKED,
    REGISTRATION_ATTEMPTS_EXCEED,
    RESTORE_PASSWORD_ATTEMPTS_EXCEED,
    RESTORE_PASSWORD_LOCKED,

    USER_NOT_FOUND,

    EMAIL_NOT_VERIFIED,
    EMAIL_ALREADY_VERIFIED,
    PHONE_NOT_VERIFIED,
    PHONE_ALREADY_VERIFIED,
    PHONE_CODE_NOT_VALID,
    EMAIL_CODE_NOT_VALID,

    VERIFICATION_ATTEMPTS_EXCEED,
    VERIFICATION_TIME_EXCEED,
    REGISTRATION_BLOCKED,

    QR_CODE_NOT_FOUND,
    QR_CODE_EXPIRED,
    QR_CODE_ALREADY_IN_USE,
    QR_CODE_WRONG_DEVICE,

    PHONE_CANT_BE_CHANGED,

    CUSTOMER_ALREADY_CREATED,
    CUSTOMER_NOT_FOUND,
    MEMBERSHIP_NOT_FOUND,
    WRONG_CUSTOMER_TYPE,
    WRONG_PARENT_CUSTOMER_TYPE,

    KYC_ALREADY_FINISHED,
    CUSTOMER_DATA_NOT_FILLED,
    NO_ACTIVE_KYC,
    WRONG_KYC_STATE,

    CHILD_KYC_NOT_COMPLETED,

    WRONG_KYC_SECTION,
    KYC_NOT_CHANGED,
    KYC_WAITING_RESPONSE,

    DOCUMENT_NOT_FOUND,

    SMS_NOT_DELIVERED,

    UNSUPPORTED_MEDIA_TYPE,
    MALFORMED_DOCUMENT,
    PAGE_OUT_OF_RANGE,
    PASSWORD_INVALID,
    PASSWORD_RECENTLY_USED,
    PASSWORD_OLD_WRONG,

    WRONG_ADDRESS_TYPE,

    SUB_CUSTOMER_ALREADY_EXISTS,
    SUB_CUSTOMER_LINKED_AS_PARENT,
    SUB_CUSTOMER_AS_CUSTOMER,

    WRONG_RELATION_STATE,

    WRONG_DEVICE_ID,
    DEVICE_ALREADY_CONFIRMED,

    ACCOUNT_NOT_FOUND,
    MAIN_ACCOUNT_NOT_FOUND,
    MAIN_ACCOUNT_NOT_LINKED,
    WRONG_ACCOUNT_TYPE,

    BANK_PROCESSING_ERROR,

    ORGANIZATION_NOT_FOUND,
}
