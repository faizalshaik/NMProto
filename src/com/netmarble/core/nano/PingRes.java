package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class PingRes extends MessageNano {
    private static volatile PingRes[] _emptyArray;
    public int errorCode;

    public static PingRes[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new PingRes[0];
                }
            }
        }
        return _emptyArray;
    }

    public PingRes() {
        clear();
    }

    public PingRes clear() {
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

    public PingRes mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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

    public static PingRes parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (PingRes) MessageNano.mergeFrom(new PingRes(), bArr);
    }

    public static PingRes parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new PingRes().mergeFrom(codedInputByteBufferNano);
    }
}
