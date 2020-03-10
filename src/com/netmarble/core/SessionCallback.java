package com.netmarble.core;

public interface SessionCallback {
    public static final int CHANNEL_CONNECTED = 5;
    public static final int CREATE_CHANNEL_CONNECTED = 3;
    public static final int LOAD_CHANNEL_CONNECTED = 4;
    public static final int WORLD_CHANGED = 1;
    public static final int WORLD_REMOVED = 2;

    void onCreatedSession();

    void onInitializedSession();

    void onSignedSession();

    void onUpdatedSession(int i);
}
