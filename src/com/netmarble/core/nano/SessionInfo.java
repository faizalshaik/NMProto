package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MapFactories;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Map;

public final class SessionInfo extends MessageNano {
    private static volatile SessionInfo[] _emptyArray;
    public String cid;
    public String clientAddr;
    public String deviceKey;
    public Map<String, String> extraData;
    public String gameCode;
    public String pid;
    public String serverAddr;
    public SessionProperty[] sessionProperties;
    public String sid;
    public String wid;

    public static SessionInfo[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SessionInfo[0];
                }
            }
        }
        return _emptyArray;
    }

    public SessionInfo() {
        clear();
    }

    public SessionInfo clear() {
        this.gameCode = "";
        this.pid = "";
        this.cid = "";
        this.wid = "";
        this.sid = "";
        this.serverAddr = "";
        this.clientAddr = "";
        this.extraData = null;
        this.sessionProperties = SessionProperty.emptyArray();
        this.deviceKey = "";
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.gameCode.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.gameCode);
        }
        if (!this.pid.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.pid);
        }
        if (!this.cid.equals("")) {
            codedOutputByteBufferNano.writeString(3, this.cid);
        }
        if (!this.wid.equals("")) {
            codedOutputByteBufferNano.writeString(4, this.wid);
        }
        if (!this.sid.equals("")) {
            codedOutputByteBufferNano.writeString(5, this.sid);
        }
        if (!this.serverAddr.equals("")) {
            codedOutputByteBufferNano.writeString(6, this.serverAddr);
        }
        if (!this.clientAddr.equals("")) {
            codedOutputByteBufferNano.writeString(7, this.clientAddr);
        }
        if (this.extraData != null) {
            InternalNano.serializeMapField(codedOutputByteBufferNano, this.extraData, 8, 9, 9);
        }
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    codedOutputByteBufferNano.writeMessage(9, sessionProperty);
                }
            }
        }
        if (!this.deviceKey.equals("")) {
            codedOutputByteBufferNano.writeString(10, this.deviceKey);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (!this.gameCode.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.gameCode);
        }
        if (!this.pid.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.pid);
        }
        if (!this.cid.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.cid);
        }
        if (!this.wid.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.wid);
        }
        if (!this.sid.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(5, this.sid);
        }
        if (!this.serverAddr.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(6, this.serverAddr);
        }
        if (!this.clientAddr.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(7, this.clientAddr);
        }
        if (this.extraData != null) {
            computeSerializedSize += InternalNano.computeMapFieldSize(this.extraData, 8, 9, 9);
        }
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(9, sessionProperty);
                }
            }
        }
        return !this.deviceKey.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(10, this.deviceKey) : computeSerializedSize;
    }

    public SessionInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        MapFactories.MapFactory mapFactory = MapFactories.getMapFactory();
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            switch (readTag) {
                case 0:
                    return this;
                case 10:
                    this.gameCode = codedInputByteBufferNano.readString();
                    break;
                case 18:
                    this.pid = codedInputByteBufferNano.readString();
                    break;
                case 26:
                    this.cid = codedInputByteBufferNano.readString();
                    break;
                case 34:
                    this.wid = codedInputByteBufferNano.readString();
                    break;
                case 42:
                    this.sid = codedInputByteBufferNano.readString();
                    break;
                case 50:
                    this.serverAddr = codedInputByteBufferNano.readString();
                    break;
                case 58:
                    this.clientAddr = codedInputByteBufferNano.readString();
                    break;
                case 66:
                    this.extraData = InternalNano.mergeMapEntry(codedInputByteBufferNano, this.extraData, mapFactory, 9, 9, null, 10, 18);
                    break;
                case 74:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 74);
                    int length = this.sessionProperties == null ? 0 : this.sessionProperties.length;
                    SessionProperty[] sessionPropertyArr = new SessionProperty[(repeatedFieldArrayLength + length)];
                    if (length != 0) {
                        System.arraycopy(this.sessionProperties, 0, sessionPropertyArr, 0, length);
                    }
                    while (length < sessionPropertyArr.length - 1) {
                        sessionPropertyArr[length] = new SessionProperty();
                        codedInputByteBufferNano.readMessage(sessionPropertyArr[length]);
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    sessionPropertyArr[length] = new SessionProperty();
                    codedInputByteBufferNano.readMessage(sessionPropertyArr[length]);
                    this.sessionProperties = sessionPropertyArr;
                    break;
                case 82:
                    this.deviceKey = codedInputByteBufferNano.readString();
                    break;
                default:
                    if (WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        break;
                    } else {
                        return this;
                    }
            }
        }
    }

    public static SessionInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SessionInfo) MessageNano.mergeFrom(new SessionInfo(), bArr);
    }

    public static SessionInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SessionInfo().mergeFrom(codedInputByteBufferNano);
    }
}
