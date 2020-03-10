package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;

public final class BasePacket extends MessageNano {
    private static volatile BasePacket[] _emptyArray;
    public long debug;
    public int msgType;
    public int multicastType;
    public byte[] payload;
    public long sequence;
    public String serviceCode;
    public SessionInfo[] session;
    public SessionProperty[] sessionProperties;
    public int version;

    public static BasePacket[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new BasePacket[0];
                }
            }
        }
        return _emptyArray;
    }

    public BasePacket() {
        clear();
    }

    public BasePacket clear() {
        this.serviceCode = "";
        this.msgType = 0;
        this.sequence = 0;
        this.payload = WireFormatNano.EMPTY_BYTES;
        this.session = SessionInfo.emptyArray();
        this.multicastType = 0;
        this.version = 0;
        this.sessionProperties = SessionProperty.emptyArray();
        this.debug = 0;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.serviceCode.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.serviceCode);
        }
        if (this.msgType != 0) {
            codedOutputByteBufferNano.writeInt32(2, this.msgType);
        }
        if (this.sequence != 0) {
            codedOutputByteBufferNano.writeInt64(3, this.sequence);
        }
        if (!Arrays.equals(this.payload, WireFormatNano.EMPTY_BYTES)) {
            codedOutputByteBufferNano.writeBytes(4, this.payload);
        }
        if (this.session != null && this.session.length > 0) {
            for (SessionInfo sessionInfo : this.session) {
                if (sessionInfo != null) {
                    codedOutputByteBufferNano.writeMessage(5, sessionInfo);
                }
            }
        }
        if (this.multicastType != 0) {
            codedOutputByteBufferNano.writeInt32(6, this.multicastType);
        }
        if (this.version != 0) {
            codedOutputByteBufferNano.writeInt32(7, this.version);
        }
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    codedOutputByteBufferNano.writeMessage(8, sessionProperty);
                }
            }
        }
        if (this.debug != 0) {
            codedOutputByteBufferNano.writeInt64(99, this.debug);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (!this.serviceCode.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.serviceCode);
        }
        if (this.msgType != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, this.msgType);
        }
        if (this.sequence != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(3, this.sequence);
        }
        if (!Arrays.equals(this.payload, WireFormatNano.EMPTY_BYTES)) {
            computeSerializedSize += CodedOutputByteBufferNano.computeBytesSize(4, this.payload);
        }
        if (this.session != null && this.session.length > 0) {
            int i = computeSerializedSize;
            for (SessionInfo sessionInfo : this.session) {
                if (sessionInfo != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(5, sessionInfo);
                }
            }
            computeSerializedSize = i;
        }
        if (this.multicastType != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, this.multicastType);
        }
        if (this.version != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, this.version);
        }
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(8, sessionProperty);
                }
            }
        }
        return this.debug != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(99, this.debug) : computeSerializedSize;
    }

    public BasePacket mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                this.serviceCode = codedInputByteBufferNano.readString();
            } else if (readTag == 16) {
                this.msgType = codedInputByteBufferNano.readInt32();
            } else if (readTag == 24) {
                this.sequence = codedInputByteBufferNano.readInt64();
            } else if (readTag == 34) {
                this.payload = codedInputByteBufferNano.readBytes();
            } else if (readTag != 42) {
                if (readTag == 48) {
                    int readInt32 = codedInputByteBufferNano.readInt32();
                    switch (readInt32) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.multicastType = readInt32;
                            break;
                    }
                } else if (readTag == 56) {
                    this.version = codedInputByteBufferNano.readInt32();
                } else if (readTag == 66) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 66);
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
                } else if (readTag == 792) {
                    this.debug = codedInputByteBufferNano.readInt64();
                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                    return this;
                }
            } else {
                int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                int length2 = this.session == null ? 0 : this.session.length;
                SessionInfo[] sessionInfoArr = new SessionInfo[(repeatedFieldArrayLength2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.session, 0, sessionInfoArr, 0, length2);
                }
                while (length2 < sessionInfoArr.length - 1) {
                    sessionInfoArr[length2] = new SessionInfo();
                    codedInputByteBufferNano.readMessage(sessionInfoArr[length2]);
                    codedInputByteBufferNano.readTag();
                    length2++;
                }
                sessionInfoArr[length2] = new SessionInfo();
                codedInputByteBufferNano.readMessage(sessionInfoArr[length2]);
                this.session = sessionInfoArr;
            }
        }
    }

    public static BasePacket parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (BasePacket) MessageNano.mergeFrom(new BasePacket(), bArr);
    }

    public static BasePacket parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new BasePacket().mergeFrom(codedInputByteBufferNano);
    }
}
