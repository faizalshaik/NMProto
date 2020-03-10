package com.netmarble;

public class Result {
    public static final int CONNECT_CHANNEL_OPTION_CHANNEL_CONNECTION_MODIFIED = 327681;
    public static final int CONNECT_CHANNEL_OPTION_NEW_CHANNELID = 327683;
    public static final int CONNECT_CHANNEL_OPTION_USED_CHANNELID = 327682;
    public static final int DETAIL_NEW_CHANNELID = -102102;
    public static final int DETAIL_OTP_INPUT_RESTRICTED = -105101;
    public static final int DETAIL_OTP_INVALID = -105102;
    public static final int DETAIL_OTP_WAS_EXPIREXD = -105100;
    public static final int DETAIL_PLAYERID_MOVED = -101104;
    public static final int DETAIL_USED_CHANNELID = -102101;
    public static final String FACEBOOK_DOMAIN = "FACEBOOK_DOMAIN";
    public static final String GOOGLEPLUS_DOMAIN = "GOOGLEPLUS_DOMAIN";
    public static final int HTTP_STATUS_UNKNOWN = 0;
    public static final int INVALID_PARAM = 196610;
    public static final int INVALID_TOKEN = 131074;
    public static final int IN_PROGRESS = 196614;
    public static final int JSON_PARSING_FAIL = 196613;
    public static final String KAKAO_DOMAIN = "KAKAO_DOMAIN";
    public static final String MSDK_DOMAIN = "MSDK_DOMAIN";
    public static final String NAVER_DOMAIN = "NAVER_DOMAIN";
    public static final String NETMARBLES_DOMAIN = "NETMARBLES_DOMAIN";
    public static final int NETWORK_UNAVAILABLE = 65539;
    public static final int NOT_AUTHENTICATED = 196609;
    public static final int NOT_SUPPORTED = 196611;
    public static final int OTP_INPUT_RESTRICT = 393219;
    public static final int OTP_IS_NOT_VALID = 393217;
    public static final int OTP_WAS_EXPIRED = 393218;
    public static final int PERMISSION = 196612;
    public static final int PLAYERID_MOVED = -101104;
    public static final int SERVICE = 65538;
    public static final int SUCCESS = 0;
    public static final String SUCCESS_STRING = "Success";
    public static final int TIMEOUT = 65540;
    public static final int UNKNOWN = 65537;
    public static final int USER_CANCELED = 131073;
    private int code;
    private int detailCode;
    private String domain;
    private int httpStatusCode;
    private String message;

    public String toString() {
        if (this.domain == null || (!this.domain.equalsIgnoreCase(MSDK_DOMAIN) && !this.domain.equalsIgnoreCase(KAKAO_DOMAIN))) {
            return "Result{Domain=" + this.domain + ", Code=" + Integer.toHexString(this.code) + ", DetailCode=" + this.detailCode + ", Message=" + this.message + "}";
        }
        return "Result{Domain=" + this.domain + ", Code=" + this.code + ", DetailCode=" + this.detailCode + ", Message=" + this.message + "}";
    }

    public Result(String str, int i, String str2) {
        this.domain = str;
        this.code = i;
        if (i == 0) {
            this.detailCode = 0;
        } else {
            this.detailCode = -1;
        }
        this.message = str2;
        this.httpStatusCode = 0;
    }

    public Result(String str, int i, int i2, String str2) {
        this.domain = str;
        this.code = i;
        this.detailCode = i2;
        this.message = str2;
        this.httpStatusCode = 0;
    }

    public Result(int i, String str) {
        this.domain = NETMARBLES_DOMAIN;
        this.code = i;
        if (i == 0) {
            this.detailCode = 0;
        } else {
            this.detailCode = -1;
        }
        this.message = str;
        this.httpStatusCode = 0;
    }

    public Result(int i, int i2, String str) {
        this.domain = NETMARBLES_DOMAIN;
        this.code = i;
        this.detailCode = i2;
        this.message = str;
        this.httpStatusCode = 0;
    }

    public Result(int i, String str, int i2) {
        this.domain = NETMARBLES_DOMAIN;
        this.code = i;
        if (i == 0) {
            this.detailCode = 0;
        } else {
            this.detailCode = -1;
        }
        this.message = str;
        this.httpStatusCode = i2;
    }

    public String getDomain() {
        return this.domain;
    }

    public int getCode() {
        return this.code;
    }

    public int getDetailCode() {
        return this.detailCode;
    }

    public String getMessage() {
        return this.message;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }
}
