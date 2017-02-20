package com.java.networking.function;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ContentHandle {

    static int VERSION_SIZE = 4;
    private final int version;
    private final byte[] content;

    public ContentHandle(byte[] versionAndData) {
        version = after(versionAndData);
        content = new byte[versionAndData.length - VERSION_SIZE];
        System.arraycopy(versionAndData, VERSION_SIZE, content, 0,
                content.length);
    }

    public ContentHandle(int version, byte[] data) {
        this.version = version;
        this.content = data;
    }

    public int getVersion() {
        return version;
    }

    public byte[] getData() {
        return content;
    }

    public static byte[] before(int v) {
        byte[] b = new byte[VERSION_SIZE];
        int r = v;
        for (int i = VERSION_SIZE - 1; i >= 0; i--) {
            b[i] = (byte) (r % 256 + Byte.MIN_VALUE);
            r /= 256;
        }
        return b;
    }

    public static int after(byte[] b) {
        int v = 0;
        for (int i = 0; i < VERSION_SIZE; i++) {
            v = v * 256 + ((int) b[i] - Byte.MIN_VALUE);
        }
        return v;
    }

    public static byte[] read(InputStream inputStream, byte[] b, int s) {
        try {
            int length = inputStream.read(b, s, b.length - s);
            if (length < 0) {
                return null;
            }

            int size = s + length;
            if (size < b.length) {
                return Arrays.copyOf(b, size);
            } else {
                return b;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
