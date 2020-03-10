package com.netmarble.core;

//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.ironsource.mediationsdk.utils.IronSourceConstants;
//import com.netmarble.Log;
import com.netmarble.Result;
import com.netmarble.TCPSession;
import com.netmarble.core.nano.BasePacket;
import com.netmarble.core.nano.ClientProtocol;
import com.netmarble.core.nano.CloseSessionNtf;
import com.netmarble.core.nano.DeleteSessionPropertyReq;
import com.netmarble.core.nano.DeleteSessionPropertyRes;
import com.netmarble.core.nano.EndMaintenanceNtf;
import com.netmarble.core.nano.GetSessionPropertyReq;
import com.netmarble.core.nano.GetSessionPropertyRes;
import com.netmarble.core.nano.PingReq;
import com.netmarble.core.nano.SessionInfo;
import com.netmarble.core.nano.SessionProperty;
import com.netmarble.core.nano.SetSessionPropertyReq;
import com.netmarble.core.nano.SetSessionPropertyRes;
import com.netmarble.core.nano.SignInReq;
import com.netmarble.core.nano.SignInRes;
import com.netmarble.core.nano.StartMaintenanceNtf;
//import com.netmarble.network.HttpAsyncTask;
import com.netmarble.network.TCPSessionNetwork;
import com.netmarble.network.socket.NetmarbleSocket;
import com.netmarble.plugin.IRequest;
import com.netmarble.plugin.ITCPSession;
//import com.netmarble.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.json.JSONException;
import org.json.JSONObject;

public class TCPSessionManager implements IRequest {
    private static final int DEFAULT_RECONNECT_SEC = 15;
    private static final int MAX_RECONNECT_SEC = 3600;
    private static final int OFFLINE = 3;
    private static final int PING_INTERVAL_DEFAULT_SEC = 60;
    private static final int PING_INTERVAL_MIN_SEC = 5;
    private static final int RECONNECT_REQUEST_FROM_SERVER = 9;
    private static final int REQUEST_HOST_IS_NOT_SUCCESS = 4;
    private static final int REQUEST_HOST_JSON_EXCEPTION = 7;
    private static final int REQUEST_HOST_NETWORK_FAIL = 5;
    private static final int REQUEST_HOST_NOT_EXIST_HOST_OR_PORT = 6;
    private static final String SESSION_SERVER_SERVICE_NAME = "session";
    private static final int SOCKET_CONNECT_FAIL = 1;
    private static final int SOCKET_IS_CLOSE = 8;
    /* access modifiers changed from: private */
    public static final String TAG = TCPSessionManager.class.getCanonicalName();
    private static final int TCP_SIGN_IN_RESPONSE_FAIL = 2;
    private AtomicLong atomicLong;
    private String characterID;
    /* access modifiers changed from: private */
    public volatile boolean connectAPICall;
    private long getSessionProperty;
    //private Handler handler;
    /* access modifiers changed from: private */
    public String hostname;
    /* access modifiers changed from: private */
    public volatile boolean isAppBackground;
    /* access modifiers changed from: private */
    public boolean isPingReceived;
    /* access modifiers changed from: private */
    public int lastReconnectCause;
    private long ping;
    /* access modifiers changed from: private */
    public int pingIntervalSec;
    /* access modifiers changed from: private */
    public int port;
    /* access modifiers changed from: private */
    public volatile boolean receivedNotRetry;
    private long removeSessionProperty;
    /* access modifiers changed from: private */
    public volatile SessionInfo sessionInfo;
    private int sessionReconnectSec;
    /* access modifiers changed from: private */
    public int sessionState;
    private long setSessionProperty;
    private long signIn;
    private NetmarbleSocket socket;
    /* access modifiers changed from: private */
    public TimerTask task;
    /* access modifiers changed from: private */
    public TCPSession.TCPSessionExecuteListener tcpSessionExecuteListener;
    /* access modifiers changed from: private */
    public Set<TCPSession.TCPSessionListener> tcpSessionListenerSet;
    private Map<String, ITCPSession> tcpSessionMap;
    private Timer timer;
    private final Object timer_lock;
    private String worldID;

    public static class SessionState {
        public static final int NONE = 0;
        public static final int SIGN_IN_COMPLETED = 3;
        public static final int SOCKET_CONNECTED = 2;
        public static final int SOCKET_CONNECTING = 1;
        public static final int SOCKET_DISCONNECTED = 4;
    }

    public void onCreatedSession() {
    }

    public void onDestroy() {
    }

    public void onPause() {
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public SessionInfo getSessionInfoFromServer() {
        return this.sessionInfo;
    }

    /* access modifiers changed from: package-private */
    public void addTCPSession(ITCPSession iTCPSession) {
        String str = TAG;
        //Log.v(str, "addTCPSession :" + iTCPSession);
        this.tcpSessionMap.put(iTCPSession.getServiceCode(), iTCPSession);
    }

    private void callbackOnSessionSignInCompleted() {
        if (this.tcpSessionMap != null && this.tcpSessionMap.size() != 0) {
            for (String str : this.tcpSessionMap.keySet()) {
                ITCPSession iTCPSession = this.tcpSessionMap.get(str);
                if (iTCPSession != null) {
                    iTCPSession.onSessionSignInCompleted();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void callbackOnDisconnected() {
        if (this.tcpSessionMap != null && this.tcpSessionMap.size() != 0) {
            for (String str : this.tcpSessionMap.keySet()) {
                ITCPSession iTCPSession = this.tcpSessionMap.get(str);
                if (iTCPSession != null) {
                    iTCPSession.onDisconnected();
                }
            }
        }
    }

    public void onResume() {
        //Log.v(TAG, "onResume");
        this.isAppBackground = false;
//        if (this.connectAPICall && !TextUtils.isEmpty(SessionImpl.getInstance().getGameToken()) && !this.receivedNotRetry) {
//            //Log.v(TAG, "try connect");
//            doTCPConnect();
//        }
        String gameToken = "gameToken";
        if (this.connectAPICall && !gameToken.isEmpty() && !this.receivedNotRetry) {
            //Log.v(TAG, "try connect");
            doTCPConnect();
        }
    }

    public void onStop() {
//        ActivityManager activityManager;
//        Context applicationContext = ActivityManager.getInstance().getApplicationContext();
//        if (applicationContext != null && (activityManager = (ActivityManager) applicationContext.getSystemService("activity")) != null) {
//            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
//            if (runningTasks == null || runningTasks.size() == 0) {
//                Log.d(TAG, "onStop. taskInfo is null or empty");
//            } else if (!runningTasks.get(0).topActivity.getPackageName().equals(applicationContext.getPackageName())) {
//                this.isAppBackground = true;
//                this.sessionState = 0;
//                if (this.connectAPICall) {
//                    cancelTimer();
//                    disconnectSocket();
//                    return;
//                }
//                cancelTimer();
//            }
//        }
        this.isAppBackground = true;
        this.sessionState = 0;
        if (this.connectAPICall) {
            cancelTimer();
            disconnectSocket();
            return;
        }
        cancelTimer();
    }

    private void getSessionReconnectSec() {
        this.sessionReconnectSec = 30;
        if (this.sessionReconnectSec > MAX_RECONNECT_SEC) {
            this.sessionReconnectSec = MAX_RECONNECT_SEC;
        }        
//        if (this.sessionReconnectSec == 0) {
//            String url = SessionImpl.getInstance().getUrl("sessionReconnectSec");
//            if (!TextUtils.isEmpty(url)) {
//                if (!TextUtils.isDigitsOnly(url)) {
//                    Log.w(TAG, "sessionReconnectSec is not digits");
//                    return;
//                }
//                try {
//                    this.sessionReconnectSec = Integer.valueOf(url).intValue();
//                    if (this.sessionReconnectSec > MAX_RECONNECT_SEC) {
//                        this.sessionReconnectSec = MAX_RECONNECT_SEC;
//                    }
//                    String str = TAG;
//                    Log.v(str, "sessionReconnectSec : " + this.sessionReconnectSec);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public void onInitializedSession() {
        //Log.v(TAG, "onInitializedSession");
        this.sessionState = 0;
        cancelTimer();
        disconnect();
        getSessionReconnectSec();
    }

    private boolean isValidPropertyKey(String str) {
        return true;
//        String url = SessionImpl.getInstance().getUrl("property_key_pattern");
//        String str2 = TAG;
//        Log.v(str2, "property_key_pattern : " + url);
//        if (TextUtils.isEmpty(url)) {
//            Log.v(TAG, "property_key_pattern pattern is missing from server");
//            return true;
//        }
//        try {
//            return Pattern.matches(url, str);
//        } catch (PatternSyntaxException unused) {
//            Log.v(TAG, "key pattern syntax error");
//            return true;
//        }
    }

    private boolean isValidPropertyValue(String str) {
        return true;
//        String url = SessionImpl.getInstance().getUrl("property_string_value_pattern");
//        String str2 = TAG;
//        Log.v(str2, "property_string_value_pattern : " + url);
//        if (TextUtils.isEmpty(url)) {
//            Log.v(TAG, "property_string_value_pattern pattern is missing from server");
//            return true;
//        }
//        try {
//            return Pattern.matches(url, str);
//        } catch (PatternSyntaxException unused) {
//            Log.v(TAG, "value pattern syntax error");
//            return true;
//        }
    }

    private boolean isValidWorldID(String str) {
        return true;
//        String url = SessionImpl.getInstance().getUrl("world_id_pattern");
//        String str2 = TAG;
//        Log.v(str2, "world_id_pattern : " + url);
//        if (TextUtils.isEmpty(url)) {
//            Log.v(TAG, "worldID pattern is missing from server");
//            return true;
//        }
//        try {
//            return Pattern.matches(url, str);
//        } catch (PatternSyntaxException unused) {
//            Log.v(TAG, "worldID pattern syntax error");
//            return true;
//        }
    }

    private boolean isValidCharacterID(String str) {
            return true;        
//        String url = SessionImpl.getInstance().getUrl("character_id_pattern");
//        String str2 = TAG;
//        Log.v(str2, "character_id_pattern : " + url);
//        if (TextUtils.isEmpty(url)) {
//            Log.v(TAG, "characterID pattern is missing from server");
//            return true;
//        }
//        try {
//            return Pattern.matches(url, str);
//        } catch (PatternSyntaxException unused) {
//            Log.v(TAG, "characterID pattern syntax error");
//            return true;
//        }
    }

    private void tcpSessionListenerCallbackOnConnected(final String str) {
        //Log.v(TAG, "TCPSessionListener.onConnected()");
//        this.handler.post(new Runnable() {
//            public void run() {
//                for (TCPSession.TCPSessionListener onConnected : TCPSessionManager.this.tcpSessionListenerSet) {
//                    onConnected.onConnected(str);
//                }
//            }
//        });
        HashMap hashMap = new HashMap();
        hashMap.put("sessionID", str);
        responseExecute(1, hashMap);
    }

    /* access modifiers changed from: private */
    public void tcpSessionListenerCallbackOnDisconnected() {
        //Log.v(TAG, "TCPSessionListener.onDisconnected()");
//        this.handler.post(new Runnable() {
//            public void run() {
//                for (TCPSession.TCPSessionListener onDisconnected : TCPSessionManager.this.tcpSessionListenerSet) {
//                    onDisconnected.onDisconnected();
//                }
//            }
//        });
        responseExecute(2, new HashMap());
    }

    private void tcpSessionListenerCallbackOnReconnect(final int i) {
        //Log.v(TAG, String.format(Locale.ENGLISH, "TCPSessionListener.onReconnect(%d)", new Object[]{Integer.valueOf(i)}));
//        this.handler.post(new Runnable() {
//            public void run() {
//                for (TCPSession.TCPSessionListener onReconnect : TCPSessionManager.this.tcpSessionListenerSet) {
//                    onReconnect.onReconnect(i);
//                }
//            }
//        });
        HashMap hashMap = new HashMap();
        hashMap.put("cause", Integer.valueOf(i));
        responseExecute(3, hashMap);
    }

    private void responseExecute(final int i, final Map<String, Object> map) {
//        this.handler.post(new Runnable() {
//            public void run() {
//                if (TCPSessionManager.this.tcpSessionExecuteListener != null) {
//                    String str = "action : " + i + ", map " + map;
//                    //Log.d(TCPSessionManager.TAG, "onReceived_callback " + str);
//                    //Log.APICallback("void TCPSession.TCPSessionExecuteListener.onReceived()", str);
//                    TCPSessionManager.this.tcpSessionExecuteListener.onReceived(i, map);
//                }
//            }
//        });
    }

    public boolean setTCPSessionExecuteListener(final TCPSession.TCPSessionExecuteListener tCPSessionExecuteListener) {
        if (tCPSessionExecuteListener == null) {
            return false;
        }
//        this.handler.post(new Runnable() {
//            public void run() {
//                TCPSession.TCPSessionExecuteListener unused = TCPSessionManager.this.tcpSessionExecuteListener = tCPSessionExecuteListener;
//                String access$200 = TCPSessionManager.TAG;
//                //Log.v(access$200, "TCPSessionExecuteListener was set : " + tCPSessionExecuteListener);
//            }
//        });
        return true;
    }

    public boolean removeTCPSessionExecuteListener() {
        if (this.tcpSessionExecuteListener == null) {
            return false;
        }
//        this.handler.post(new Runnable() {
//            public void run() {
//                TCPSession.TCPSessionExecuteListener unused = TCPSessionManager.this.tcpSessionExecuteListener = null;
//                //Log.v(TCPSessionManager.TAG, "TcpSessionExecuteListener removed");
//            }
//        });
        return true;
    }

    @Deprecated
    public boolean setTCPSessionListener(TCPSession.TCPSessionListener tCPSessionListener) {
        if (tCPSessionListener == null) {
            return false;
        }
        this.tcpSessionListenerSet.add(tCPSessionListener);
        return true;
    }

    @Deprecated
    public boolean removeTCPSessionListener(TCPSession.TCPSessionListener tCPSessionListener) {
        if (tCPSessionListener == null) {
            return false;
        }
        this.tcpSessionListenerSet.remove(tCPSessionListener);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0079 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00cb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isChangedUserData(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            java.lang.String r0 = r5.worldID
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0034
            if (r6 == 0) goto L_0x0034
            java.lang.String r0 = r5.worldID
            boolean r0 = r0.equals(r6)
            if (r0 == 0) goto L_0x0011
            goto L_0x0074
        L_0x0011:
            java.lang.String r0 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Userdata changed(worldID) : "
            r3.append(r4)
            java.lang.String r4 = r5.worldID
            r3.append(r4)
            java.lang.String r4 = " -> "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            com.netmarble.Log.v(r0, r3)
            r5.worldID = r6
            goto L_0x0057
        L_0x0034:
            java.lang.String r0 = r5.worldID
            if (r0 == 0) goto L_0x0059
            java.lang.String r6 = TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "Userdata changed(worldID) : "
            r0.append(r3)
            java.lang.String r3 = r5.worldID
            r0.append(r3)
            java.lang.String r3 = " -> null"
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.netmarble.Log.v(r6, r0)
            r5.worldID = r1
        L_0x0057:
            r6 = 1
            goto L_0x0075
        L_0x0059:
            if (r6 == 0) goto L_0x0074
            java.lang.String r0 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Userdata changed(worldID) : null -> "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            com.netmarble.Log.v(r0, r3)
            r5.worldID = r6
            goto L_0x0057
        L_0x0074:
            r6 = 0
        L_0x0075:
            java.lang.String r0 = r5.characterID
            if (r0 == 0) goto L_0x00a7
            if (r7 == 0) goto L_0x00a7
            java.lang.String r0 = r5.characterID
            boolean r0 = r0.equals(r7)
            if (r0 == 0) goto L_0x0084
            goto L_0x00e6
        L_0x0084:
            java.lang.String r6 = TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Userdata changed(characterID) : "
            r0.append(r1)
            java.lang.String r1 = r5.characterID
            r0.append(r1)
            java.lang.String r1 = " -> "
            r0.append(r1)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            com.netmarble.Log.v(r6, r0)
            r5.characterID = r7
            goto L_0x00e7
        L_0x00a7:
            java.lang.String r0 = r5.characterID
            if (r0 == 0) goto L_0x00cb
            java.lang.String r6 = TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "Userdata changed(characterID) : "
            r7.append(r0)
            java.lang.String r0 = r5.characterID
            r7.append(r0)
            java.lang.String r0 = " -> null"
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            com.netmarble.Log.v(r6, r7)
            r5.characterID = r1
            goto L_0x00e7
        L_0x00cb:
            if (r7 == 0) goto L_0x00e6
            java.lang.String r6 = TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Userdata changed(characterID) : null -> "
            r0.append(r1)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            com.netmarble.Log.v(r6, r0)
            r5.characterID = r7
            goto L_0x00e7
        L_0x00e6:
            r2 = r6
        L_0x00e7:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netmarble.core.TCPSessionManager.isChangedUserData(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int connectWithUserData(java.util.Map<java.lang.String, java.lang.Object> r5) {
        /*
            r4 = this;
            com.netmarble.core.SessionImpl r0 = com.netmarble.core.SessionImpl.getInstance()
            java.lang.String r0 = r0.getGameToken()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0010
            r5 = -1
            return r5
        L_0x0010:
            r0 = 0
            if (r5 == 0) goto L_0x005f
            int r1 = r5.size()
            if (r1 != 0) goto L_0x001a
            goto L_0x005f
        L_0x001a:
            java.lang.String r1 = "worldID"
            boolean r1 = r5.containsKey(r1)
            if (r1 == 0) goto L_0x003b
            r1 = -2
            java.lang.String r2 = "worldID"
            java.lang.Object r2 = r5.get(r2)     // Catch:{ ClassCastException -> 0x003a }
            if (r2 == 0) goto L_0x003b
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ ClassCastException -> 0x003a }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ ClassCastException -> 0x003a }
            if (r3 != 0) goto L_0x003c
            boolean r3 = r4.isValidWorldID(r2)     // Catch:{ ClassCastException -> 0x003a }
            if (r3 != 0) goto L_0x003c
            return r1
        L_0x003a:
            return r1
        L_0x003b:
            r2 = r0
        L_0x003c:
            java.lang.String r1 = "characterID"
            boolean r1 = r5.containsKey(r1)
            if (r1 == 0) goto L_0x0060
            r1 = -3
            java.lang.String r3 = "characterID"
            java.lang.Object r5 = r5.get(r3)     // Catch:{ ClassCastException -> 0x005e }
            if (r5 == 0) goto L_0x0060
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ ClassCastException -> 0x005e }
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ ClassCastException -> 0x005e }
            if (r0 != 0) goto L_0x005c
            boolean r0 = r4.isValidCharacterID(r5)     // Catch:{ ClassCastException -> 0x005e }
            if (r0 != 0) goto L_0x005c
            return r1
        L_0x005c:
            r0 = r5
            goto L_0x0060
        L_0x005e:
            return r1
        L_0x005f:
            r2 = r0
        L_0x0060:
            boolean r5 = r4.isChangedUserData(r2, r0)
            r0 = 1
            if (r5 == 0) goto L_0x006d
            r4.connectAPICall = r0
            r4.doTCPConnect()
            return r0
        L_0x006d:
            boolean r5 = r4.isConnected()
            if (r5 == 0) goto L_0x0077
            r4.connectAPICall = r0
            r5 = 2
            return r5
        L_0x0077:
            boolean r5 = r4.connectAPICall
            if (r5 == 0) goto L_0x0084
            java.lang.String r5 = TAG
            java.lang.String r0 = "Already try to connecting..."
            com.netmarble.Log.v(r5, r0)
            r5 = 3
            return r5
        L_0x0084:
            r4.connectAPICall = r0
            r4.doTCPConnect()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netmarble.core.TCPSessionManager.connectWithUserData(java.util.Map):int");
    }

    private int getConnectState() {
        if (isConnected()) {
            return 1;
        }
        return this.connectAPICall ? 3 : -1;
    }

    public boolean disconnect() {
        cancelTimer();
        this.connectAPICall = false;
        this.characterID = null;
        this.worldID = null;
        return disconnectSocket();
    }

    private boolean disconnectSocket() {
        if (this.socket == null || !this.socket.isConnected()) {
            return false;
        }
        this.socket.disconnect();
        return true;
    }

    public boolean isConnected() {
        return this.sessionState == 3;
    }

    /* access modifiers changed from: private */
    public void doTCPConnect() {
        if (this.socket == null || !this.socket.isConnected()) {
            requestHost();
        } else {
            signInReq();
        }
    }

    public void onSignedSession() {
        //Log.v(TAG, "onSignedSession");
        this.receivedNotRetry = false;
    }

    public void onUpdatedSession(int i) {
        String str = TAG;
        //Log.v(str, "onUpdatedSession : " + i);
        if (3 == i || 4 == i) {
            this.sessionState = 0;
            cancelTimer();
            disconnect();
        }
    }

    private void requestHost() {
        //Log.v(TAG, "requestHost");
//        if (this.hostname==null || this.hostname.isEmpty() || this.port == 0) {
//            //String url = SessionImpl.getInstance().getUrl("sessionManagerUrl");
//            String url = "";
//            if (url==null || url.isEmpty()) {
//                url = "http://session-api.netmarble.com";
//                //Log.w(TAG, "sessionManagerUrl is null or empty. set default");
//            }
//            //SessionImpl instance = SessionImpl.getInstance();
//            //String gameToken = instance.getGameToken();
//            String gameToken = "gameToken";
//            
//            if (gameToken.isEmpty()) {
//                //Log.e(TAG, "gameToken is null or empty");
//            } else {
//                String playerID = "playerID";
//                String gameCode = "gameCode";
//                //TCPSessionNetwork.sessions(url, instance.getPlayerID(), gameToken, ConfigurationImpl.getInstance().getGameCode(), new HttpAsyncTask.HttpAsyncTaskListener() {
//                TCPSessionNetwork.sessions(url, playerID, gameToken, gameCode, new HttpAsyncTask.HttpAsyncTaskListener() {                
//                    public void onReceive(Result result, String str) {
//                        if (result.isSuccess()) {
//                            String access$200 = TCPSessionManager.TAG;
//                            //Log.v(access$200, "sessions : " + str);
//                            try {
//                                JSONObject jSONObject = new JSONObject(str);
//                                int optInt = jSONObject.optInt(IronSourceConstants.EVENTS_ERROR_CODE, -1);
//                                if (optInt != 0) {
//                                    String optString = jSONObject.optString("errorMessage");
//                                    String access$2002 = TCPSessionManager.TAG;
//                                    Log.e(access$2002, "sessionManagerServer is error : " + optInt + ", " + optString);
//                                    TCPSessionManager.this.reconnect(4);
//                                    return;
//                                }
//                                JSONObject jSONObject2 = jSONObject.getJSONObject("resultData");
//                                String optString2 = jSONObject2.optString("sessionIp");
//                                int optInt2 = jSONObject2.optInt("sessionPort", -1);
//                                String access$2003 = TCPSessionManager.TAG;
//                                Log.v(access$2003, "session : " + optString2 + ":" + optInt2);
//                                if (TextUtils.isEmpty(optString2) || -1 == optInt2) {
//                                    TCPSessionManager.this.reconnect(6);
//                                    return;
//                                }
//                                String unused = TCPSessionManager.this.hostname = optString2;
//                                int unused2 = TCPSessionManager.this.port = optInt2;
//                                TCPSessionManager.this.doConnect();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                TCPSessionManager.this.reconnect(7);
//                            }
//                        } else {
//                            TCPSessionManager.this.reconnect(5);
//                        }
//                    }
//                });
//            }
//        } else {
//            String str = TAG;
//            Log.v(str, "already host exist : " + this.hostname + ":" + this.port);
//            doConnect();
//        }
    }

    /* access modifiers changed from: private */
    public boolean rejectAPI() {
        return 3 != this.sessionState;
    }

    /* access modifiers changed from: private */
    public void cancelTimer() {
        synchronized (this.timer_lock) {
            if (this.task != null) {
                this.task.cancel();
                this.task = null;
            }
        }
    }

    private int getRandomInteger() {
        if (this.sessionReconnectSec == 0) {
            this.sessionReconnectSec = 15;
        }
        int nextInt = new Random().nextInt(this.sessionReconnectSec);
        if (nextInt == 0) {
            nextInt = 1;
        }
        return nextInt * 1000;
    }

    /* access modifiers changed from: private */
    public void reconnect(int i) {
        tcpSessionListenerCallbackOnReconnect(i);
        int randomInteger = getRandomInteger();
        String str = TAG;
        //Log.v(str, "reconnect : " + randomInteger);
        synchronized (this.timer_lock) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.task = new TimerTask() {
                public void run() {
                    boolean access$700 = TCPSessionManager.this.isOnline();
                    String access$200 = TCPSessionManager.TAG;
                    //Log.v(access$200, "isOnline : " + access$700);
                    if (access$700) {
                        TCPSessionManager.this.doTCPConnect();
                        TCPSessionManager.this.task.cancel();
                        return;
                    }
                    TCPSessionManager.this.reconnect(3);
                }
            };
            try {
                this.timer = new Timer("NetmarbleS.Session.Reconnect", true);
                long j = (long) randomInteger;
                this.timer.schedule(this.task, j, j);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void makePingTimer() {
        //Log.v(TAG, "makePingTimer");
        synchronized (this.timer_lock) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.task = new TimerTask() {
                public void run() {
                    if (TCPSessionManager.this.rejectAPI() && TCPSessionManager.this.task != null) {
                        TCPSessionManager.this.task.cancel();
                    } else if (TCPSessionManager.this.isPingReceived && 60 != TCPSessionManager.this.pingIntervalSec) {
                        boolean unused = TCPSessionManager.this.isPingReceived = false;
                        TCPSessionManager.this.pingReq();
                        int unused2 = TCPSessionManager.this.pingIntervalSec = 60;
                        TCPSessionManager.this.makePingTimer();
                    } else if (TCPSessionManager.this.isPingReceived || 5 == TCPSessionManager.this.pingIntervalSec) {
                        boolean unused3 = TCPSessionManager.this.isPingReceived = false;
                        TCPSessionManager.this.pingReq();
                    } else {
                        boolean unused4 = TCPSessionManager.this.isPingReceived = false;
                        TCPSessionManager.this.pingReq();
                        int unused5 = TCPSessionManager.this.pingIntervalSec = TCPSessionManager.this.pingIntervalSec / 2;
                        if (5 > TCPSessionManager.this.pingIntervalSec) {
                            int unused6 = TCPSessionManager.this.pingIntervalSec = 5;
                        }
                        TCPSessionManager.this.makePingTimer();
                    }
                }
            };
            try {
                this.timer = new Timer("NetmarbleS.Session", true);
                this.timer.schedule(this.task, (long) (this.pingIntervalSec * 1000), (long) (this.pingIntervalSec * 1000));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void parseTCPSession(BasePacket basePacket) {
        if (basePacket == null) {
            //Log.e(TAG, "basePacket is null");
            return;
        }
        String str = basePacket.serviceCode;
        if (str==null || str.isEmpty()) {
            //Log.e(TAG, "serviceCode is null or empty");
            return;
        }
        ITCPSession iTCPSession = this.tcpSessionMap.get(str);
        if (iTCPSession == null) {
            String str2 = TAG;
            //Log.e(str2, "tcpSession is null : " + str);
            return;
        }
        iTCPSession.onReceived(basePacket);
    }

    /* access modifiers changed from: private */
    public void parseSession(BasePacket basePacket) throws InvalidProtocolBufferNanoException {
        Result result;
        Result result2;
        Result result3;
        int i = basePacket.msgType;
        if (i != 101) {
            if (i != 201) {
                if (i == 302) {
                    int i2 = CloseSessionNtf.parseFrom(basePacket.payload).cause;
                    switch (i2) {
                        case 0:
                            //Log.i(TAG, "BY_ADMIN");
                            this.receivedNotRetry = true;
                            return;
                        case 1:
                            //Log.i(TAG, "NOT_SIGNED_IN");
                            return;
                        case 2:
                            //Log.i(TAG, "TRIGGER_RECONNECT");
                            this.hostname = null;
                            this.port = 0;
                            this.lastReconnectCause = 9;
                            disconnectSocket();
                            return;
                        default:
                            String str = TAG;
                            //Log.w(str, "invalid cause : " + i2);
                            return;
                    }
                } else if (i != 401) {
                    if (i != 501) {
                        if (i != 601) {
                            switch (i) {
                                case ClientProtocol.START_MAINTENANCE_NTF /*310*/:
                                    StartMaintenanceNtf parseFrom = StartMaintenanceNtf.parseFrom(basePacket.payload);
                                    int i3 = parseFrom.maintenanceType;
                                    String str2 = parseFrom.serviceCode;
                                    String str3 = parseFrom.message;
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("maintenanceType", Integer.valueOf(i3));
                                    hashMap.put("serviceCode", str2);
                                    hashMap.put("message", str3);
                                    responseExecute(9, hashMap);
                                    return;
                                case ClientProtocol.END_MAINTENANCE_NTF /*311*/:
                                    EndMaintenanceNtf parseFrom2 = EndMaintenanceNtf.parseFrom(basePacket.payload);
                                    int i4 = parseFrom2.maintenanceType;
                                    String str4 = parseFrom2.serviceCode;
                                    HashMap hashMap2 = new HashMap();
                                    hashMap2.put("maintenanceType", Integer.valueOf(i4));
                                    hashMap2.put("serviceCode", str4);
                                    responseExecute(10, hashMap2);
                                    return;
                                default:
                                    String str5 = TAG;
                                    //Log.e(str5, "check msgType : " + i);
                                    return;
                            }
                        } else if (basePacket.sequence != this.getSessionProperty) {
                            //Log.e(TAG, "sequence not match");
                        } else {
                            GetSessionPropertyRes parseFrom3 = GetSessionPropertyRes.parseFrom(basePacket.payload);
                            int i5 = parseFrom3.errorCode;
                            String str6 = TAG;
                            //Log.d(str6, "getSessionPropertyRes.errorCode : " + i5);
                            if (i5 == 0) {
                                result3 = new Result(0, Result.SUCCESS_STRING);
                            } else {
                                result3 = new Result(65538, i5, parseFrom3.errorMessage);
                            }
                            HashMap hashMap3 = new HashMap();
                            hashMap3.put(IronSourceConstants.EVENTS_RESULT, result3);
                            hashMap3.put("propertyList", makeSessionPropertyList(parseFrom3.sessionProperties));
                            responseExecute(8, hashMap3);
                        }
                    } else if (basePacket.sequence != this.removeSessionProperty) {
                        //Log.e(TAG, "sequence not match");
                    } else {
                        DeleteSessionPropertyRes parseFrom4 = DeleteSessionPropertyRes.parseFrom(basePacket.payload);
                        int i6 = parseFrom4.errorCode;
                        String str7 = TAG;
                        //Log.d(str7, "deleteSessionPropertyRes.errorCode : " + i6);
                        if (i6 == 0) {
                            result2 = new Result(0, Result.SUCCESS_STRING);
                        } else {
                            result2 = new Result(65538, i6, parseFrom4.errorMessage);
                        }
                        HashMap hashMap4 = new HashMap();
                        hashMap4.put(IronSourceConstants.EVENTS_RESULT, result2);
                        hashMap4.put("propertyCount", Integer.valueOf(parseFrom4.sessionPropertyCount));
                        hashMap4.put("propertyList", makeSessionPropertyList(parseFrom4.failedSessionProperties));
                        responseExecute(7, hashMap4);
                    }
                } else if (basePacket.sequence != this.setSessionProperty) {
                    //Log.e(TAG, "sequence not match");
                } else {
                    SetSessionPropertyRes parseFrom5 = SetSessionPropertyRes.parseFrom(basePacket.payload);
                    int i7 = parseFrom5.errorCode;
                    String str8 = TAG;
                    //Log.d(str8, "SetSessionPropertyRes.errorCode : " + i7);
                    if (i7 == 0) {
                        result = new Result(0, Result.SUCCESS_STRING);
                    } else {
                        result = new Result(65538, i7, parseFrom5.errorMessage);
                    }
                    HashMap hashMap5 = new HashMap();
                    hashMap5.put(IronSourceConstants.EVENTS_RESULT, result);
                    hashMap5.put("propertyCount", Integer.valueOf(parseFrom5.sessionPropertyCount));
                    hashMap5.put("propertyList", makeSessionPropertyList(parseFrom5.failedSessionProperties));
                    responseExecute(6, hashMap5);
                }
            } else if (this.ping != basePacket.sequence) {
                //Log.e(TAG, "sequence not match");
            } else {
                //Log.v(TAG, "PING_RES");
                this.isPingReceived = true;
            }
        } else if (this.signIn != basePacket.sequence) {
            //Log.e(TAG, "sequence not match");
        } else {
            SignInRes parseFrom6 = SignInRes.parseFrom(basePacket.payload);
            int i8 = parseFrom6.errorCode;
            String str9 = TAG;
            //Log.d(str9, "SignInRes.errorCode : " + i8);
            if (i8 == 0) {
                this.sessionInfo = parseFrom6.session;
                String str10 = TAG;
                //Log.d(str10, "sessionInfo : " + this.sessionInfo);
                String str11 = "";
                if (this.sessionInfo != null) {
                    str11 = this.sessionInfo.sid;
                } else {
                    //Log.w(TAG, "sessionInfo is null. check session server");
                }
                this.sessionState = 3;
                makePingTimer();
                callbackOnSessionSignInCompleted();
                tcpSessionListenerCallbackOnConnected(str11);
                return;
            }
            String str12 = TAG;
            //Log.w(str12, "errorMessage : " + parseFrom6.errorMessage);
            this.lastReconnectCause = 2;
            disconnectSocket();
        }
    }

    /* access modifiers changed from: private */
    public boolean isOnline() {
//        NetworkInfo activeNetworkInfo;
//        Context applicationContext = ActivityManager.getInstance().getApplicationContext();
//        if (applicationContext == null) {
//            Log.e(TAG, "context is null");
//            return false;
//        }
//        ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService("connectivity");
//        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected()) {
//            return false;
//        }
//        return true;
        return true;
    }

    private void initSocketListener() {
        if (this.socket == null) {
            //Log.e(TAG, "socket is null");
        } else {
            this.socket.setSocketListener(new NetmarbleSocket.SocketListener() {
                public void onConnected(Result result) {
                    String access$200 = TCPSessionManager.TAG;
                    //Log.v(access$200, "onConnected : " + result);
                    if (result.isSuccess()) {
                        int unused = TCPSessionManager.this.sessionState = 2;
                        TCPSessionManager.this.signInReq();
                        return;
                    }
                    int unused2 = TCPSessionManager.this.sessionState = 0;
                    TCPSessionManager.this.reconnect(1);
                }

                public void onDisconnected() {
                    //Log.v(TCPSessionManager.TAG, "onDisconnected");
                    if (TCPSessionManager.this.sessionInfo != null) {
                        SessionInfo unused = TCPSessionManager.this.sessionInfo = null;
                    }
                    TCPSessionManager.this.cancelTimer();
                    int unused2 = TCPSessionManager.this.sessionState = 4;
                    TCPSessionManager.this.callbackOnDisconnected();
                    if (TCPSessionManager.this.isAppBackground || TCPSessionManager.this.receivedNotRetry || !TCPSessionManager.this.connectAPICall) {
                        TCPSessionManager.this.tcpSessionListenerCallbackOnDisconnected();
                        return;
                    }
                
                    TCPSessionManager.this.reconnect(TCPSessionManager.this.lastReconnectCause);
                    int unused3 = TCPSessionManager.this.lastReconnectCause = 8;
                }

                public void onReceived(byte[] bArr) {
                    try {
                        BasePacket parseFrom = BasePacket.parseFrom(bArr);
                        String str = parseFrom.serviceCode;
                        if (str==null || str.isEmpty()) {
                            //Log.e(TCPSessionManager.TAG, "serviceCode is null or empty");
                            return;
                        }
                        //Log.v(TCPSessionManager.TAG, "onReceived : " + "serviceCode(" + parseFrom.serviceCode + "), msgType(" + parseFrom.msgType + "), sequence(" + parseFrom.sequence + ")");
                        char c = 65535;
                        if (str.hashCode() == 1984987798) {
                            if (str.equals(TCPSessionManager.SESSION_SERVER_SERVICE_NAME)) {
                                c = 0;
                            }
                        }
                        if (c != 0) {
                            TCPSessionManager.this.parseTCPSession(parseFrom);
                        } else {
                            TCPSessionManager.this.parseSession(parseFrom);
                        }
                    } catch (InvalidProtocolBufferNanoException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void doConnect() {
        //Log.v(TAG, "doConnect");
        if (this.socket == null) {
            //Log.e(TAG, "socket is null");
        } else if (this.socket.isConnected()) {
            //Log.v(TAG, "already connected");
        } else if (this.sessionState == 1) {
            //Log.d(TAG, "try connecting");
        } else {
            this.sessionState = 0;
            //this.socket.setSocketTimeoutSec(com.netmarble.Configuration.getHttpTimeOutSec());
            this.socket.setSocketTimeoutSec(5);
            this.socket.connect(this.hostname, this.port);
            this.sessionState = 1;
        }
    }

//    private SessionInfo getSessionInfo() {
//        SessionImpl instance = SessionImpl.getInstance();
//        SessionInfo sessionInfo2 = new SessionInfo();
//        sessionInfo2.gameCode = com.netmarble.Configuration.getGameCode();
//        sessionInfo2.pid = instance.getPlayerID();
//        sessionInfo2.clientAddr = instance.getClientIP();
//        if (!TextUtils.isEmpty(this.characterID)) {
//            sessionInfo2.cid = this.characterID;
//        }
//        if (!TextUtils.isEmpty(this.worldID)) {
//            sessionInfo2.wid = this.worldID;
//        }
//        Context applicationContext = ActivityManager.getInstance().getApplicationContext();
//        if (applicationContext != null) {
//            String androidID = Utils.getAndroidID(applicationContext);
//            if (TextUtils.isEmpty(androidID)) {
//                Log.w(TAG, "AndroidID is null");
//                androidID = instance.getDeviceKey();
//            }
//            sessionInfo2.deviceKey = androidID;
//        }
//        return sessionInfo2;
//    }

    /* access modifiers changed from: private */
    public void signInReq() {
        this.signIn = getSequence();
        String str = TAG;
        //Log.v(str, "signInReq : " + this.signIn);
        //String gameToken = SessionImpl.getInstance().getGameToken();
        String gameToken = "gameToken";
        if (gameToken==null || gameToken.isEmpty()) {
            //Log.e(TAG, "gameToken is null or empty");
            return;
        }
        SignInReq signInReq = new SignInReq();
        signInReq.gameToken = gameToken;
        //signInReq.session = getSessionInfo();
        BasePacket basePacket = new BasePacket();
        basePacket.serviceCode = SESSION_SERVER_SERVICE_NAME;
        basePacket.msgType = 100;
        basePacket.payload = convert(signInReq);
        basePacket.sequence = this.signIn;
        send(basePacket);
    }

    public int getSessionState() {
        return this.sessionState;
    }

    public boolean send(BasePacket basePacket) {
        if (this.socket == null) {
            //Log.e(TAG, "socket is nul");
            return false;
        } else if (!this.socket.isConnected()) {
            //Log.e(TAG, "socket is not connected");
            return false;
        } else {
            this.socket.send(convert(basePacket));
            //Log.v(TAG, "send success");
            return true;
        }
    }

    public long getSequence() {
        return this.atomicLong.incrementAndGet();
    }

    /* access modifiers changed from: private */
    public void pingReq() {
        this.ping = getSequence();
        String str = TAG;
        //Log.v(str, "pingReq : " + this.ping);
        PingReq pingReq = new PingReq();
        BasePacket basePacket = new BasePacket();
        basePacket.serviceCode = SESSION_SERVER_SERVICE_NAME;
        basePacket.msgType = 200;
        basePacket.payload = convert(pingReq);
        basePacket.sequence = this.ping;
        send(basePacket);
    }

    private byte[] convert(MessageNano messageNano) {
        byte[] bArr = new byte[messageNano.getSerializedSize()];
        try {
            messageNano.writeTo(CodedOutputByteBufferNano.newInstance(bArr));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bArr;
    }

    private List<SessionProperty> makeSessionPropertyList(List<TCPSession.Property> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            return arrayList;
        }
        for (TCPSession.Property next : list) {
            String str = next.key;
            if (str==null || str.isEmpty()) {
                String str2 = TAG;
                //Log.w(str2, str + " is null or empty");
            } else if (!isValidPropertyKey(str)) {
                String str3 = TAG;
                //Log.w(str3, str + " is invalid");
            } else {
                SessionProperty sessionProperty = new SessionProperty();
                sessionProperty.key = str;
                int i = next.propertyType;
                int i2 = next.valueType;
                sessionProperty.propertyType = i;
                sessionProperty.valueType = i2;
                if (i == 1) {
                    switch (i2) {
                        case 1:
                            sessionProperty.intValue = next.intValue;
                            break;
                        case 2:
                            sessionProperty.longValue = next.longValue;
                            break;
                        case 3:
                            sessionProperty.boolenValue = next.booleanValue;
                            break;
                        default:
                            String str4 = next.stringValue;
                            if (str4!=null && !str4.isEmpty()) {
                                if (isValidPropertyValue(str4)) {
                                    sessionProperty.stringValue = str4;
                                    break;
                                } else {
                                    String str5 = TAG;
                                    //Log.w(str5, str4 + " is invalid");
                                    break;
                                }
                            } else {
                                String str6 = TAG;
                                //Log.w(str6, str4 + " is null or empty");
                                continue;
                            }
                    }
                }
                arrayList.add(sessionProperty);
            }
        }
        return arrayList;
    }

    private List<TCPSession.Property> makeSessionPropertyList(SessionProperty[] sessionPropertyArr) {
        ArrayList arrayList = new ArrayList();
        if (sessionPropertyArr == null) {
            return arrayList;
        }
        for (SessionProperty sessionProperty : sessionPropertyArr) {
            if (sessionProperty.propertyType == 0) {
                arrayList.add(new TCPSession.KeyOnlyProperty(sessionProperty.key));
            } else {
                int i = sessionProperty.valueType;
                if (i == 0) {
                    arrayList.add(new TCPSession.StringProperty(sessionProperty.key, sessionProperty.stringValue));
                } else if (i == 3) {
                    arrayList.add(new TCPSession.BooleanProperty(sessionProperty.key, sessionProperty.boolenValue));
                } else if (i == 1) {
                    arrayList.add(new TCPSession.IntProperty(sessionProperty.key, sessionProperty.intValue));
                } else if (i == 2) {
                    arrayList.add(new TCPSession.LongProperty(sessionProperty.key, sessionProperty.longValue));
                } else {
                    //Log.w(TAG, "Unknown type : " + i);
                }
            }
        }
        return arrayList;
    }

    private int setSessionPropertyReq(Map<String, Object> map) {
        if (this.sessionState != 3) {
            return -1;
        }
        List list = null;
        if (map != null && map.containsKey("propertyList")) {
            try {
                Object obj = map.get("propertyList");
                if (obj != null) {
                    list = (List) obj;
                }
            } catch (ClassCastException unused) {
                //Log.w(TAG, "propertyList is not List<TCPSession.Property>");
            }
        }
        if (list == null) {
            //Log.w(TAG, "propertyList not found");
            return -2;
        }
        List<SessionProperty> makeSessionPropertyList = makeSessionPropertyList((List<TCPSession.Property>) list);
        if (makeSessionPropertyList.size() < 1) {
            //Log.w(TAG, "valid propertyList not found");
            return -3;
        }
        this.setSessionProperty = getSequence();
        String str = TAG;
        //Log.v(str, "setSessionProperty : " + this.setSessionProperty);
        SetSessionPropertyReq setSessionPropertyReq = new SetSessionPropertyReq();
        setSessionPropertyReq.sessionProperties = (SessionProperty[]) makeSessionPropertyList.toArray(new SessionProperty[makeSessionPropertyList.size()]);
        BasePacket basePacket = new BasePacket();
        basePacket.serviceCode = SESSION_SERVER_SERVICE_NAME;
        basePacket.msgType = ClientProtocol.SET_PROPERTY_REQ;
        basePacket.payload = convert(setSessionPropertyReq);
        basePacket.sequence = this.setSessionProperty;
        send(basePacket);
        return 1;
    }

    private int deleteSessionPropertyReq(Map<String, Object> map) {
        if (this.sessionState != 3) {
            return -1;
        }
        List list = null;
        if (map != null && map.containsKey("propertyList")) {
            try {
                Object obj = map.get("propertyList");
                if (obj != null) {
                    list = (List) obj;
                }
            } catch (ClassCastException unused) {
                //Log.w(TAG, "propertyList is not List<TCPSession.Property>");
            }
        }
        if (list == null) {
            //Log.w(TAG, "propertyList not found");
            return -2;
        }
        List<SessionProperty> makeSessionPropertyList = makeSessionPropertyList((List<TCPSession.Property>) list);
        if (makeSessionPropertyList.size() < 1) {
            //Log.w(TAG, "valid propertyList not found");
            return -3;
        }
        this.removeSessionProperty = getSequence();
        String str = TAG;
        //Log.v(str, "removeSessionProperty : " + this.removeSessionProperty);
        DeleteSessionPropertyReq deleteSessionPropertyReq = new DeleteSessionPropertyReq();
        deleteSessionPropertyReq.sessionProperties = (SessionProperty[]) makeSessionPropertyList.toArray(new SessionProperty[makeSessionPropertyList.size()]);
        BasePacket basePacket = new BasePacket();
        basePacket.serviceCode = SESSION_SERVER_SERVICE_NAME;
        basePacket.msgType = 500;
        basePacket.payload = convert(deleteSessionPropertyReq);
        basePacket.sequence = this.removeSessionProperty;
        send(basePacket);
        return 1;
    }

    private int getSessionPropertyReq() {
        if (this.sessionState != 3) {
            return -1;
        }
        this.getSessionProperty = getSequence();
        String str = TAG;
        //Log.v(str, "getSessionProperty : " + this.getSessionProperty);
        GetSessionPropertyReq getSessionPropertyReq = new GetSessionPropertyReq();
        BasePacket basePacket = new BasePacket();
        basePacket.serviceCode = SESSION_SERVER_SERVICE_NAME;
        basePacket.msgType = 600;
        basePacket.payload = convert(getSessionPropertyReq);
        basePacket.sequence = this.getSessionProperty;
        send(basePacket);
        return 1;
    }

    public int execute(int i, Map<String, Object> map) {
        if (i == 1) {
            return connectWithUserData(map);
        }
        switch (i) {
            case 4:
                if (disconnect()) {
                    return 1;
                }
                return 2;
            case 5:
                return getConnectState();
            case 6:
                return setSessionPropertyReq(map);
            case 7:
                return deleteSessionPropertyReq(map);
            case 8:
                return getSessionPropertyReq();
            default:
                return -999;
        }
    }

    private TCPSessionManager() {
        this.lastReconnectCause = 8;
        this.pingIntervalSec = 60;
        this.receivedNotRetry = false;
        this.connectAPICall = false;
        this.isAppBackground = false;
        this.tcpSessionListenerSet = new HashSet();
        this.sessionState = 0;
        this.tcpSessionMap = new HashMap();
        this.atomicLong = new AtomicLong();
        this.socket = new NetmarbleSocket();
        this.isPingReceived = true;
        this.timer_lock = new Object();
        //this.handler = new Handler(Looper.getMainLooper());
        initSocketListener();
        
    }

    public static TCPSessionManager getInstance() {
        return SocketManagerHolder.instance;
    }

    private static class SocketManagerHolder {
        static final TCPSessionManager instance = new TCPSessionManager();

        private SocketManagerHolder() {
        }
    }
}
