package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class EndMaintenanceNtf extends MessageNano {
    private static volatile EndMaintenanceNtf[] _emptyArray;
    public int maintenanceType;
    public String serviceCode;

    public static EndMaintenanceNtf[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new EndMaintenanceNtf[0];
                }
            }
        }
        return _emptyArray;
    }

    public EndMaintenanceNtf() {
        clear();
    }

    public EndMaintenanceNtf clear() {
        this.maintenanceType = 0;
        this.serviceCode = "";
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.maintenanceType != 0) {
            codedOutputByteBufferNano.writeInt32(1, this.maintenanceType);
        }
        if (!this.serviceCode.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.serviceCode);
        }
        super.writeTo(codedOutputByteBufferNano);
        
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.maintenanceType != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.maintenanceType);
        }
        return !this.serviceCode.equals("") ? computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.serviceCode) : computeSerializedSize;
    }

    public EndMaintenanceNtf mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag != 0) {
                if (readTag == 8) {
                    int readInt32 = codedInputByteBufferNano.readInt32();
                    switch (readInt32) {
                        case 0:
                        case 1:
                            this.maintenanceType = readInt32;
                            break;
                    }
                } else if (readTag == 18) {
                    this.serviceCode = codedInputByteBufferNano.readString();
                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public static EndMaintenanceNtf parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (EndMaintenanceNtf) MessageNano.mergeFrom(new EndMaintenanceNtf(), bArr);
    }

    public static EndMaintenanceNtf parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new EndMaintenanceNtf().mergeFrom(codedInputByteBufferNano);
    }
}
