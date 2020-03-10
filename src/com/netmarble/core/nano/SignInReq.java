package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class SignInReq extends MessageNano {
    private static volatile SignInReq[] _emptyArray;
    public String gameToken;
    public SessionInfo session;

    public static SignInReq[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SignInReq[0];
                }
            }
        }
        return _emptyArray;
    }

    public SignInReq() {
        clear();
    }

    public SignInReq clear() {
        this.session = null;
        this.gameToken = "";
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.session != null) {
            codedOutputByteBufferNano.writeMessage(1, this.session);
        }
        if (!this.gameToken.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.gameToken);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.session != null) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, this.session);
        }
        return !this.gameToken.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.gameToken) : computeSerializedSize;
    }

    public SignInReq mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.session == null) {
                    this.session = new SessionInfo();
                }
                codedInputByteBufferNano.readMessage(this.session);
            } else if (readTag == 18) {
                this.gameToken = codedInputByteBufferNano.readString();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SignInReq parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SignInReq) MessageNano.mergeFrom(new SignInReq(), bArr);
    }

    public static SignInReq parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SignInReq().mergeFrom(codedInputByteBufferNano);
    }
}
