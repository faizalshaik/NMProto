package com.netmarble.network.socket;

import com.netmarble.Result;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetmarbleSocket {
    private final int DEFAULT_TIME_OUT_SEC = 5;
    /* access modifiers changed from: private */
    public String TAG = NetmarbleSocket.class.getCanonicalName();
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ExecutorService receiveExecutor = Executors.newSingleThreadExecutor();
    private ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public Socket socket;
    private SocketListener socketListener;
    /* access modifiers changed from: private */
    public int timeoutSec = 5;

    public interface SocketListener {
        void onConnected(Result result);

        void onDisconnected();

        void onReceived(byte[] bArr);
    }

    /* access modifiers changed from: private */
    public void initInputOutputStream() {
        if (this.socket == null) {
            //Log.e(this.TAG, "socket is null");
            return;
        }
        try {
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void connectedCallback(Result result) {
        if (this.socketListener != null) {
            this.socketListener.onConnected(result);
        }
    }

    /* access modifiers changed from: private */
    public void disconnectedCallback() {
        if (this.socketListener != null) {
            this.socketListener.onDisconnected();
        }
    }

    /* access modifiers changed from: private */
    public void initSocket() {
        if (this.socket != null) {
            this.socket = null;
        }
        this.socket = new Socket();
    }

    public void setSocketTimeoutSec(int i) {
        if (i > 0) {
            this.timeoutSec = i;
        } else {
            //Log.w(this.TAG, "sec is must be > 0");
        }
    }

    public void connect(final String str, final int i) {
        if (isConnected()) {
            //Log.w(this.TAG, "already connected");
            return;
        }
        this.sendExecutor.execute(new Runnable() {
            public void run() {
                NetmarbleSocket.this.initSocket();
                try {
                    NetmarbleSocket.this.socket.connect(new InetSocketAddress(str, i), NetmarbleSocket.this.timeoutSec * 1000);
                    NetmarbleSocket.this.initInputOutputStream();
                    //Log.v(NetmarbleSocket.this.TAG, "listen");
                    NetmarbleSocket.this.listen();
                    NetmarbleSocket.this.connectedCallback(new Result(0, Result.SUCCESS_STRING));
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    NetmarbleSocket.this.connectedCallback(new Result(65540, e.getMessage()));
                } catch (IOException e2) {
                    e2.printStackTrace();
                    NetmarbleSocket.this.connectedCallback(new Result(65539, e2.getMessage()));
                }
            }
        });
    }

    
    
    
    
    public boolean disconnect() {
        this.sendExecutor.execute(new Runnable() {
            public void run() {
                if (NetmarbleSocket.this.socket == null) {
                    //Log.v(NetmarbleSocket.this.TAG, "socket is null");
                    return;
                }
                try {
                    if (!NetmarbleSocket.this.socket.isClosed()) {
                        NetmarbleSocket.this.socket.close();
                        Socket unused = NetmarbleSocket.this.socket = null;
                        NetmarbleSocket.this.disconnectedCallback();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public boolean isConnected() {
        if (this.socket == null) {
            return false;
        }
        return this.socket.isConnected();
    }

    public void setSocketListener(SocketListener socketListener2) {
        this.socketListener = socketListener2;
    }

    public void send(final byte[] bArr) {
        this.sendExecutor.execute(new Runnable() {
            public void run() {
                boolean unused = NetmarbleSocket.this.sendPacket(NetmarbleSocket.this.makeSendPacket(bArr));
            }
        });
    }

    /* access modifiers changed from: private */
    public void listen() {
        this.receiveExecutor.execute(new Runnable() {
            public void run() {
                NetmarbleSocket.this.receivePacket();
            }
        });
    }

    /* access modifiers changed from: private */
    public void receivePacket() {
        while (true) {
            try {
                //Log.v(this.TAG, "try read");
                byte[] bArr = new byte[this.dataInputStream.readInt()];
                this.dataInputStream.readFully(bArr);
                if (this.socketListener != null) {
                    this.socketListener.onReceived(bArr);
                } else {
                    //Log.v(this.TAG, "receiveListener is null");
                }
            } catch (IOException e) {
                //Log.w(this.TAG, e.getMessage());
                disconnect();
                return;
            }
        }
    }

    private byte[] intToByteArray(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    /* access modifiers changed from: private */
    public byte[] makeSendPacket(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return new byte[0];
        }
        byte[] intToByteArray = intToByteArray(bArr.length);
        byte[] bArr2 = new byte[(intToByteArray.length + bArr.length)];
        System.arraycopy(intToByteArray, 0, bArr2, 0, intToByteArray.length);
        System.arraycopy(bArr, 0, bArr2, intToByteArray.length, bArr.length);
        return bArr2;
    }

    
    
    /* access modifiers changed from: private */
    public boolean sendPacket(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            //Log.e(this.TAG, "bytes is null or empty");
            return false;
        } else if (this.dataOutputStream == null) {
            //Log.e(this.TAG, "dataOutputStream is null");
            return false;
        } else {
            try {
                this.dataOutputStream.write(bArr);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    
}
