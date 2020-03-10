package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class SetSessionPropertyRes extends MessageNano {
    private static volatile SetSessionPropertyRes[] _emptyArray;
    public int errorCode;
    public String errorMessage;
    public SessionProperty[] failedSessionProperties;
    public int sessionPropertyCount;

    public static SetSessionPropertyRes[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SetSessionPropertyRes[0];
                }
            }
        }
        return _emptyArray;
    }

    public SetSessionPropertyRes() {
        clear();
    }

    public SetSessionPropertyRes clear() {
        this.errorCode = 0;
        this.errorMessage = "";
        this.sessionPropertyCount = 0;
        this.failedSessionProperties = SessionProperty.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.errorCode != 0) {
            codedOutputByteBufferNano.writeInt32(1, this.errorCode);
        }
        if (!this.errorMessage.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.errorMessage);
        }
        if (this.sessionPropertyCount != 0) {
            codedOutputByteBufferNano.writeInt32(3, this.sessionPropertyCount);
        }
        if (this.failedSessionProperties != null && this.failedSessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.failedSessionProperties) {
                if (sessionProperty != null) {
                    codedOutputByteBufferNano.writeMessage(4, sessionProperty);
                }
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.errorCode != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.errorCode);
        }
        if (!this.errorMessage.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.errorMessage);
        }
        if (this.sessionPropertyCount != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.sessionPropertyCount);
        }
        if (this.failedSessionProperties != null && this.failedSessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.failedSessionProperties) {
                if (sessionProperty != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, sessionProperty);
                }
            }
        }
        return computeSerializedSize;
    }

    public SetSessionPropertyRes mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.errorCode = codedInputByteBufferNano.readInt32();
            } else if (readTag == 18) {
                this.errorMessage = codedInputByteBufferNano.readString();
            } else if (readTag == 24) {
                this.sessionPropertyCount = codedInputByteBufferNano.readInt32();
            } else if (readTag == 34) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                int length = this.failedSessionProperties == null ? 0 : this.failedSessionProperties.length;
                SessionProperty[] sessionPropertyArr = new SessionProperty[(repeatedFieldArrayLength + length)];
                if (length != 0) {
                    System.arraycopy(this.failedSessionProperties, 0, sessionPropertyArr, 0, length);
                }
                while (length < sessionPropertyArr.length - 1) {
                    sessionPropertyArr[length] = new SessionProperty();
                    codedInputByteBufferNano.readMessage(sessionPropertyArr[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                sessionPropertyArr[length] = new SessionProperty();
                codedInputByteBufferNano.readMessage(sessionPropertyArr[length]);
                this.failedSessionProperties = sessionPropertyArr;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SetSessionPropertyRes parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SetSessionPropertyRes) MessageNano.mergeFrom(new SetSessionPropertyRes(), bArr);
    }

    public static SetSessionPropertyRes parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SetSessionPropertyRes().mergeFrom(codedInputByteBufferNano);
    }
}
