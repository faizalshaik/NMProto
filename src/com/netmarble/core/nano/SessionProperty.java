package com.netmarble.core.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;

public final class SessionProperty extends MessageNano {
    public static final int BOOLEAN = 3;
    public static final int INTEGER = 1;
    public static final int KEY_AND_VALUE = 1;
    public static final int KEY_ONLY = 0;
    public static final int LONG = 2;
    public static final int STRING = 0;
    private static volatile SessionProperty[] _emptyArray;
    public boolean boolenValue;
    public int intValue;
    public String key;
    public long longValue;
    public int propertyType;
    public String stringValue;
    public int valueType;

    public static SessionProperty[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SessionProperty[0];
                }
            }
        }
        return _emptyArray;
    }

    public SessionProperty() {
        clear();
    }

    public SessionProperty clear() {
        this.key = "";
        this.stringValue = "";
        this.intValue = 0;
        this.longValue = 0;
        this.boolenValue = false;
        this.propertyType = 0;
        this.valueType = 0;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (!this.key.equals("")) {
            codedOutputByteBufferNano.writeString(1, this.key);
        }
        if (!this.stringValue.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.stringValue);
        }
        if (this.intValue != 0) {
            codedOutputByteBufferNano.writeInt32(3, this.intValue);
        }
        if (this.longValue != 0) {
            codedOutputByteBufferNano.writeInt64(4, this.longValue);
        }
        if (this.boolenValue) {
            codedOutputByteBufferNano.writeBool(5, this.boolenValue);
        }
        if (this.propertyType != 0) {
            codedOutputByteBufferNano.writeInt32(6, this.propertyType);
        }
        if (this.valueType != 0) {
            codedOutputByteBufferNano.writeInt32(7, this.valueType);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (!this.key.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.key);
        }
        if (!this.stringValue.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.stringValue);
        }
        if (this.intValue != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.intValue);
        }
        if (this.longValue != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(4, this.longValue);
        }
        if (this.boolenValue) {
            computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, this.boolenValue);
        }
        if (this.propertyType != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, this.propertyType);
        }
        return this.valueType != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(7, this.valueType) : computeSerializedSize;
    }

    public SessionProperty mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                this.key = codedInputByteBufferNano.readString();
            } else if (readTag == 18) {
                this.stringValue = codedInputByteBufferNano.readString();
            } else if (readTag == 24) {
                this.intValue = codedInputByteBufferNano.readInt32();
            } else if (readTag == 32) {
                this.longValue = codedInputByteBufferNano.readInt64();
            } else if (readTag != 40) {
                if (readTag != 48) {
                    if (readTag == 56) {
                        int readInt32 = codedInputByteBufferNano.readInt32();
                        switch (readInt32) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                this.valueType = readInt32;
                                break;
                        }
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                } else {
                    int readInt322 = codedInputByteBufferNano.readInt32();
                    switch (readInt322) {
                        case 0:
                        case 1:
                            this.propertyType = readInt322;
                            break;
                    }
                }
            } else {
                this.boolenValue = codedInputByteBufferNano.readBool();
            }
        }
    }

    public static SessionProperty parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SessionProperty) MessageNano.mergeFrom(new SessionProperty(), bArr);
    }

    public static SessionProperty parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SessionProperty().mergeFrom(codedInputByteBufferNano);
    }
}
