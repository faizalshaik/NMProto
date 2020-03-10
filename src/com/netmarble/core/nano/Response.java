package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class Response extends MessageNano {
    private static volatile Response[] _emptyArray;
    public int errorCode;

    public static Response[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new Response[0];
                }
            }
        }
        return _emptyArray;
    }

    public Response() {
        clear();
    }

    public Response clear() {
        this.errorCode = 0;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.errorCode != 0) {
            codedOutputByteBufferNano.writeInt32(1, this.errorCode);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        return this.errorCode != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(1, this.errorCode) : computeSerializedSize;
    }

    public Response mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.errorCode = codedInputByteBufferNano.readInt32();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static Response parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (Response) MessageNano.mergeFrom(new Response(), bArr);
    }

    public static Response parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new Response().mergeFrom(codedInputByteBufferNano);
    }
}
