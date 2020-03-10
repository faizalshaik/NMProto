package com.netmarble.core.nano;

public interface ClientProtocol {
    public static final int ALREADY_SIGNED_IN = 3001;
    public static final int CLOSE_SESSION_NTF = 302;
    public static final int DELETE_PROPERTY_REQ = 500;
    public static final int DELETE_PROPERTY_REQ_ERROR = 2500;
    public static final int DELETE_PROPERTY_RES = 501;
    public static final int DISABLE_GAME_SESSION = 4001;
    public static final int DUMMY_MSG_TYPE = 0;
    public static final int END_MAINTENANCE_NTF = 311;
    public static final int GAME = 0;
    public static final int GET_PROPERTY_REQ = 600;
    public static final int GET_PROPERTY_RES = 601;
    public static final int INACTIVE_SESSION_SERVER = 4000;
    public static final int INTERNAL_SERVER_ERROR = 9999;
    public static final int INVALID_CID = 2005;
    public static final int INVALID_GAMECODE = 2001;
    public static final int INVALID_PARAM = 2000;
    public static final int INVALID_PID = 2002;
    public static final int INVALID_SERVICE_CODE = 2004;
    public static final int INVALID_SESSION = 2003;
    public static final int INVALID_WID = 2006;
    public static final int OVER_LIMIT_PROPERTY_SIZE = 2410;
    public static final int PING_REQ = 200;
    public static final int PING_RES = 201;
    public static final int SERVICE = 1;
    public static final int SERVICE_NOT_AVAILABLE = 1000;
    public static final int SET_PROPERTY_REQ = 400;
    public static final int SET_PROPERTY_REQ_ERROR = 2400;
    public static final int SET_PROPERTY_RES = 401;
    public static final int SIGN_IN_REQ = 100;
    public static final int SIGN_IN_RES = 101;
    public static final int START_MAINTENANCE_NTF = 310;
    public static final int SUCCESS = 0;
    public static final int VERIFY_FAIL = 3000;
}
