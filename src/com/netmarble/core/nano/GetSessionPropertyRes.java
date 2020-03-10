package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class GetSessionPropertyRes extends MessageNano {
    private static volatile GetSessionPropertyRes[] _emptyArray;
    public int errorCode;
    public String errorMessage;
    public SessionProperty[] sessionProperties;

    public static GetSessionPropertyRes[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new GetSessionPropertyRes[0];
                }
            }
        }
        return _emptyArray;
    }

    public GetSessionPropertyRes() {
        clear();
    }

    public GetSessionPropertyRes clear() {
        this.errorCode = 0;
        this.errorMessage = "";
        this.sessionProperties = SessionProperty.emptyArray();
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
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
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
        if (this.sessionProperties != null && this.sessionProperties.length > 0) {
            for (SessionProperty sessionProperty : this.sessionProperties) {
                if (sessionProperty != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, sessionProperty);
                }
            }
        }
        return computeSerializedSize;
    }

    public GetSessionPropertyRes mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.errorCode = codedInputByteBufferNano.readInt32();
            } else if (readTag == 18) {
                this.errorMessage = codedInputByteBufferNano.readString();
            } else if (readTag == 34) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
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

    public static GetSessionPropertyRes parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (GetSessionPropertyRes) MessageNano.mergeFrom(new GetSessionPropertyRes(), bArr);
    }

    public static GetSessionPropertyRes parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new GetSessionPropertyRes().mergeFrom(codedInputByteBufferNano);
    }
}
