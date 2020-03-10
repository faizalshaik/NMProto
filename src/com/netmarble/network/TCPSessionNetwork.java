package com.netmarble.network;

//import com.netmarble.network.HttpAsyncTask;

public class TCPSessionNetwork {
    private static final String TAG = TCPSessionNetwork.class.getCanonicalName();

    //public static void sessions(String str, String str2, String str3, String str4, HttpAsyncTask.HttpAsyncTaskListener httpAsyncTaskListener) {
public static void sessions(String str, String str2, String str3, String str4) {    
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append("/sessions?gameCode=");
        stringBuffer.append(str4);
        //HttpAsyncTask httpAsyncTask = new HttpAsyncTask(stringBuffer.toString(), "GET");
        //httpAsyncTask.addHeader("NMPlayerID", str2);
        //httpAsyncTask.addHeader("gameToken", str3);
        //httpAsyncTask.execute(httpAsyncTaskListener);
        //Log.v(TAG, "sessions");
    }
}
