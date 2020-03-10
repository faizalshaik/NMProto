package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class CloseSessionNtf extends MessageNano {
    private static volatile CloseSessionNtf[] _emptyArray;
    public int cause;
    public String message;

    public static CloseSessionNtf[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new CloseSessionNtf[0];
                }
            }
        }
        return _emptyArray;
    }

    public CloseSessionNtf() {
        clear();
    }

    public CloseSessionNtf clear() {
        this.cause = 0;
        this.message = "";
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.cause != 0) {
            codedOutputByteBufferNano.writeInt32(1, this.cause);
        }
        if (!this.message.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.message);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.cause != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.cause);
        }
        return !this.message.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.message) : computeSerializedSize;
    }

    public CloseSessionNtf mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.cause = codedInputByteBufferNano.readInt32();
            } else if (readTag == 18) {
                this.message = codedInputByteBufferNano.readString();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static CloseSessionNtf parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (CloseSessionNtf) MessageNano.mergeFrom(new CloseSessionNtf(), bArr);
    }

    public static CloseSessionNtf parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new CloseSessionNtf().mergeFrom(codedInputByteBufferNano);
    }
}
