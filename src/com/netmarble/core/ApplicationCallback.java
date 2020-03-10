package com.netmarble.core;


public interface ApplicationCallback {

    void onDestroy();

    void onPause();    
    
    void onRequestPermissionsResult(int i, String[] strArr, int[] iArr);

    void onResume();

    void onStop();
}


