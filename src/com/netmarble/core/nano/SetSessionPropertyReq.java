package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class SetSessionPropertyReq extends MessageNano {
    private static volatile SetSessionPropertyReq[] _emptyArray;
    public SessionProperty[] sessionProperties;

    public static SetSessionPropertyReq[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SetSessionPropertyReq[0];
                }
            }
        }
        return _emptyArray;
    }

    public SetSessionPropertyReq() {
        clear();
    }

    public SetSessionPropertyReq clear() {
        this.sessionProperties = SessionProperty.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    codedOutputByteBufferNano.writeMessage(1, sessionProperty);
                }
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, sessionProperty);
                }
            }
        }
        return computeSerializedSize;
    }

    public SetSessionPropertyReq mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
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
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SetSessionPropertyReq parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SetSessionPropertyReq) MessageNano.mergeFrom(new SetSessionPropertyReq(), bArr);
    }

    public static SetSessionPropertyReq parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SetSessionPropertyReq().mergeFrom(codedInputByteBufferNano);
    }
}
