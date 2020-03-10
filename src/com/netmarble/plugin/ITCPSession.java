package com.netmarble.plugin;

import com.netmarble.core.ApplicationCallback;
import com.netmarble.core.SessionCallback;
import com.netmarble.core.nano.BasePacket;

public interface ITCPSession extends SessionCallback, ApplicationCallback {
    String getServiceCode();

    void onDisconnected();

    void onReceived(BasePacket basePacket);

    void onSessionSignInCompleted();
}



