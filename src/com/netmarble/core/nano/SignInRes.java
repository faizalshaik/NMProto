package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class SignInRes extends MessageNano {
    private static volatile SignInRes[] _emptyArray;
    public int errorCode;
    public String errorMessage;
    public SessionInfo session;

    public static SignInRes[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SignInRes[0];
                }
            }
        }
        return _emptyArray;
    }

    public SignInRes() {
        clear();
    }

    public SignInRes clear() {
        this.errorCode = 0;
        this.errorMessage = "";
        this.session = null;
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
        if (this.session != null) {
            codedOutputByteBufferNano.writeMessage(3, this.session);
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
        return this.session != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(3, this.session) : computeSerializedSize;
    }

    public SignInRes mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.errorCode = codedInputByteBufferNano.readInt32();
            } else if (readTag == 18) {
                this.errorMessage = codedInputByteBufferNano.readString();
            } else if (readTag == 26) {
                if (this.session == null) {
                    this.session = new SessionInfo();
                }
                codedInputByteBufferNano.readMessage(this.session);
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SignInRes parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SignInRes) MessageNano.mergeFrom(new SignInRes(), bArr);
    }

    public static SignInRes parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SignInRes().mergeFrom(codedInputByteBufferNano);
    }
}
