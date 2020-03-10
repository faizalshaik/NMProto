package com.netmarble;

import com.netmarble.core.TCPSessionManager;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TCPSession {
    public static final String CHARACTER_ID = "characterID";
    private static final String TAG = TCPSession.class.getCanonicalName();
    public static final String WORLD_ID = "worldID";

    public interface Action {
        public static final int CONNECT = 1;
        public static final int DISCONNECT = 4;
        public static final int GET_CONNECT_STATE = 5;
        public static final int GET_PROPERTIES = 8;
        public static final int REMOVE_PROPERTIES = 7;
        public static final int SET_PROPERTIES = 6;
    }

    public interface TCPSessionExecuteListener {
        public static final int CONNECTED = 1;
        public static final int DISCONNECTED = 2;
        public static final int END_MAINTENANCE = 10;
        public static final int GET_PROPERTIES = 8;
        public static final int RECONNECT = 3;
        public static final int REMOVED_PROPERTIES = 7;
        public static final int SET_PROPERTIES = 6;
        public static final int START_MAINTENANCE = 9;

        void onReceived(int i, Map<String, Object> map);
    }

    public interface TCPSessionListener {
        void onConnected(String str);

        void onDisconnected();

        void onReconnect(int i);
    }

    public static boolean setTCPSessionListener(TCPSessionListener tCPSessionListener) {
        //Log.APICalled("boolean TCPSession.setTCPSessionListener()", "Parameters\nlistener : " + tCPSessionListener);
        boolean tCPSessionListener2 = TCPSessionManager.getInstance().setTCPSessionListener(tCPSessionListener);
        String str = "result : " + tCPSessionListener2;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.setTCPSessionListener()", str);
        return tCPSessionListener2;
    }

    public static boolean removeTCPSessionListener(TCPSessionListener tCPSessionListener) {
        //Log.APICalled("boolean TCPSession.removeTCPSessionListener()", "Parameters\nlistener : " + tCPSessionListener);
        boolean removeTCPSessionListener = TCPSessionManager.getInstance().removeTCPSessionListener(tCPSessionListener);
        String str = "result : " + removeTCPSessionListener;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.removeTCPSessionListener()", str);
        return removeTCPSessionListener;
    }

    public static int connectWithUserData(Map<String, Object> map) {
        //Log.APICalled("int TCPSession.connectWithUserData()", "Parameters\nuserData : " + map);
        int connectWithUserData = TCPSessionManager.getInstance().connectWithUserData(map);
        String str = "result : " + connectWithUserData;
        //Log.d(TAG, str);
        //Log.APIReturn("int TCPSession.connectWithUserData()", str);
        return connectWithUserData;
    }

    public static boolean disconnect() {
        //Log.APICalled("boolean TCPSession.disconnect()", (String) null);
        boolean disconnect = TCPSessionManager.getInstance().disconnect();
        String str = "result : " + disconnect;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.disconnect()", str);
        return disconnect;
    }

    public static boolean isConnected() {
        //Log.APICalled("boolean TCPSession.isConnected()", (String) null);
        boolean isConnected = TCPSessionManager.getInstance().isConnected();
        String str = "result : " + isConnected;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.isConnected()", str);
        return isConnected;
    }

    public static boolean setTCPSessionExecuteListener(TCPSessionExecuteListener tCPSessionExecuteListener) {
        //Log.APICalled("boolean TCPSession.setTCPSessionExecuteListener()", "Parameters\nlistener : " + tCPSessionExecuteListener);
        boolean tCPSessionExecuteListener2 = TCPSessionManager.getInstance().setTCPSessionExecuteListener(tCPSessionExecuteListener);
        String str = "result : " + tCPSessionExecuteListener2;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.setTCPSessionExecuteListener()", str);
        return tCPSessionExecuteListener2;
    }

    public static boolean removeTCPSessionExecuteListener() {
        //Log.APICalled("boolean TCPSession.removeTCPSessionExecuteListener()", (String) null);
        boolean removeTCPSessionExecuteListener = TCPSessionManager.getInstance().removeTCPSessionExecuteListener();
        String str = "result : " + removeTCPSessionExecuteListener;
        //Log.d(TAG, str);
        //Log.APIReturn("boolean TCPSession.removeTCPSessionExecuteListener()", str);
        return removeTCPSessionExecuteListener;
    }

    public static int execute(int i, Map<String, Object> map) {
        //Log.APICalled("int TalkSession.execute", "Parameters\naction : " + i + ", map : " + map);
        int execute = TCPSessionManager.getInstance().execute(i, map);
        StringBuilder sb = new StringBuilder();
        sb.append("result : ");
        sb.append(execute);
        String sb2 = sb.toString();
        //Log.d(TAG, sb2);
        //Log.APIReturn("int TalkSession.execute", sb2);
        return execute;
        
    }

    public static class Property {
        public boolean booleanValue = false;
        public int intValue = 0;
        public String key = "";
        public long longValue = 0;
        public int propertyType = 0;
        public String stringValue = "";
        public int valueType = 0;

        public Property() {
        }

        public Property(JSONObject jSONObject) {
            if (jSONObject != null) {
                this.key = jSONObject.optString("Key");
                this.stringValue = jSONObject.optString("StringValue");
                this.intValue = jSONObject.optInt("IntValue");
                this.longValue = jSONObject.optLong("LongValue");
                this.booleanValue = jSONObject.optBoolean("BooleanValue");
                this.propertyType = jSONObject.optInt("PropertyType");
                this.valueType = jSONObject.optInt("ValueType");
            }
        }

        public String getKey() {
            return this.key;
        }

        public JSONObject toJSONObject() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("Key", this.key);
                jSONObject.put("StringValue", this.stringValue);
                jSONObject.put("IntValue", this.intValue);
                jSONObject.put("LongValue", this.longValue);
                jSONObject.put("BooleanValue", this.booleanValue);
                jSONObject.put("PropertyType", this.propertyType);
                jSONObject.put("ValueType", this.valueType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jSONObject;
        }
    }

    public static class KeyOnlyProperty extends Property {
        public KeyOnlyProperty(String str) {
            this.key = str;
            this.propertyType = 0;
        }

        public String toString() {
            return "KeyOnlyProperty{key='" + this.key + '\'' + '}';
        }
    }

    public static class StringProperty extends Property {
        public StringProperty(String str, String str2) {
            this.key = str;
            this.stringValue = str2;
            this.propertyType = 1;
            this.valueType = 0;
        }

        public String getValue() {
            return this.stringValue;
        }

        public String toString() {
            return "StringProperty{key='" + this.key + '\'' + ", value='" + this.stringValue + '\'' + '}';
        }
    }

    public static class IntProperty extends Property {
        public IntProperty(String str, int i) {
            this.key = str;
            this.intValue = i;
            this.propertyType = 1;
            this.valueType = 1;
        }

        public int getValue() {
            return this.intValue;
        }

        public String toString() {
            return "IntProperty{key='" + this.key + '\'' + ", value=" + this.intValue + '}';
        }
    }

    public static class LongProperty extends Property {
        public LongProperty(String str, long j) {
            this.key = str;
            this.longValue = j;
            this.propertyType = 1;
            this.valueType = 2;
        }

        public long getValue() {
            return this.longValue;
        }

        public String toString() {
            return "LongProperty{key='" + this.key + '\'' + ", value=" + this.longValue + '}';
        }
    }

    public static class BooleanProperty extends Property {
        public BooleanProperty(String str, boolean z) {
            this.key = str;
            this.booleanValue = z;
            this.propertyType = 1;
            this.valueType = 3;
        }

        public boolean getValue() {
            return this.booleanValue;
        }

        public String toString() {
            return "BooleanProperty{key='" + this.key + '\'' + ", value=" + this.booleanValue + '}';
        }
    }
}
