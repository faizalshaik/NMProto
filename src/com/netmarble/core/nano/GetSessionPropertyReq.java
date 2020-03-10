package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class GetSessionPropertyReq extends MessageNano {
    private static volatile GetSessionPropertyReq[] _emptyArray;
    public SessionInfo session;
    
    

    public static GetSessionPropertyReq[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new GetSessionPropertyReq[0];
                }
            }
        }
        return _emptyArray;
    }

    public GetSessionPropertyReq() {
        clear();
    }

    public GetSessionPropertyReq clear() {
        this.session = null;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.session != null) {
            codedOutputByteBufferNano.writeMessage(1, this.session);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        return this.session != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(1, this.session) : computeSerializedSize;
    }

    public GetSessionPropertyReq mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static GetSessionPropertyReq parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (GetSessionPropertyReq) MessageNano.mergeFrom(new GetSessionPropertyReq(), bArr);
    }

    public static GetSessionPropertyReq parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new GetSessionPropertyReq().mergeFrom(codedInputByteBufferNano);
    }
}
