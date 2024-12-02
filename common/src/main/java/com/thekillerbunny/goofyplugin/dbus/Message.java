package com.thekillerbunny.goofyplugin.dbus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Message(@NotNull ByteBuf buffer) {
    private static boolean strict;

    public Message {
        Objects.requireNonNull(buffer);
    }
    public Message() {
        this(Unpooled.buffer());
    }
    public Message(int bytes) {
        this(Unpooled.buffer(bytes));
    }

    public <T> T read(Type<T> type) {
        return type.read(this);
    }

    public void padTo(int step) {
        int over = buffer.writerIndex() % step;
        if (over != 0) buffer.writeBytes(new byte[step - over]);
    }

    public void skipToPad(int step, boolean strict) {
        int over = buffer.readerIndex() % step;
        if (over != 0) {
            byte read[] = new byte[over];
            buffer.readBytes(read);
            if (strict) for (byte value: read) assert value == 0;
        }
    }

    static {
        assert strict = true;
    }
}
