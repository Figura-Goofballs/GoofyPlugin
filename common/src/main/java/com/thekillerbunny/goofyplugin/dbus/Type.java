package com.thekillerbunny.goofyplugin.dbus;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import java.nio.file.Path;

public sealed interface Type<T> permits
    Type.ArrayType,
    Type.BooleanType,
    Type.ByteType,
    Type.DoubleType,
    Type.Int16Type,
    Type.Int32Type,
    Type.Int64Type,
    Type.ObjectPathType,
    Type.SignatureType,
    Type.StringType,
    Type.StructType,
    Type.UInt16Type,
    Type.UInt32Type,
    Type.UInt64Type
{
    final class ByteType implements Type<Byte> {
        ByteType() {}

        @Override
        public Byte read(Message message) {
            return null;
        }

        @Override
        public void write(Byte value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('y');
        }
    }
    final class BooleanType implements Type<Boolean> {
        BooleanType() {}

        @Override
        public Boolean read(Message message) {
            return null;
        }

        @Override
        public void write(Boolean value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('b');
        }
    }
    final class Int16Type implements Type<Short> {
        Int16Type() {}

        @Override
        public Short read(Message message) {
            return null;
        }

        @Override
        public void write(Short value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('n');
        }
    }
    final class UInt16Type implements Type<Short> {
        UInt16Type() {}

        @Override
        public Short read(Message message) {
            return null;
        }

        @Override
        public void write(Short value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('q');
        }
    }
    final class Int32Type implements Type<Integer> {
        Int32Type() {}

        @Override
        public Integer read(Message message) {
            return null;
        }

        @Override
        public void write(Integer value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('i');
        }
    }
    final class UInt32Type implements Type<Integer> {
        UInt32Type() {}

        @Override
        public Integer read(Message message) {
            return null;
        }

        @Override
        public void write(Integer value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('u');
        }
    }
    final class Int64Type implements Type<Long> {
        Int64Type() {}

        @Override
        public Long read(Message message) {
            return null;
        }

        @Override
        public void write(Long value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('x');
        }
    }
    final class UInt64Type implements Type<Long> {
        UInt64Type() {}

        @Override
        public Long read(Message message) {
            return null;
        }

        @Override
        public void write(Long value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('t');
        }
    }
    final class DoubleType implements Type<Double> {
        DoubleType() {}

        @Override
        public Double read(Message message) {
            return null;
        }

        @Override
        public void write(Double value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('d');
        }
    }

    final class StringType implements Type<String> {
        @Override
        public String read(Message message) {
            return "";
        }

        @Override
        public void write(String value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('s');
        }
    }
    final class ObjectPathType implements Type<Path> {
        @Override
        public Path read(Message message) {
            return null;
        }

        @Override
        public void write(Path value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('o');
        }
    }
    final class SignatureType implements Type<List<Type<?>>> {
        @Override
        public List<Type<?>> read(Message message) {
            return List.of();
        }

        @Override
        public void write(List<Type<?>> value, Message message) {

        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('g');
        }
    }

    final class ArrayType<T> implements Type<List<T>> {
        Type<T> inner;
        ArrayType(Type<T> inner) {
            this.inner = inner;
        }

        @Override
        public List<T> read(Message message) {
            return List.of();
        }

        @Override
        public void write(List<T> value, Message message) {
            
        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('a');
            inner.writeTypeCode(out);
        }

        @Override
        public int depth() {
            return inner.depth() + 1;
        }
    }

    // FIXME(PoolloverNathan): structs should be variants for type-safety once variants are implemented
    // FIX(James Gosling): structs cannot be compile-time typesafe without type parameter lists
    // TODO(PoolloverNathan): find a way to make structs safe anyway — maybe recursive generics?
    final class StructType implements Type<List<?>> {
        List<Type<?>> inner;
        StructType(List<Type<?>> inner) {
            this.inner = inner;
        }

        @Override
        public List<?> read(Message message) {
            return List.of();
        }

        @Override
        public void write(List<?> value, Message message) {
            
        }

        @Override
        public void writeTypeCode(StringBuilder out) {
            out.append('(');
            for (Type<?> t: inner) t.writeTypeCode(out);
            out.append(')');
        }
    }

    Type<Byte> BYTE = new ByteType();
    Type<Boolean> BOOLEAN = new BooleanType();
    Type<Short> INT16 = new Int16Type();
    Type<Short> UINT16 = new UInt16Type();
    Type<Integer> INT32 = new Int32Type();
    Type<Integer> UINT32 = new UInt32Type();
    Type<Long> INT64 = new Int64Type();
    Type<Long> UINT64 = new UInt64Type();
    Type<Double> DOUBLE = new DoubleType();

    Type<String> STRING = new StringType();
    Type<Path> OBJECT_PATH = new ObjectPathType();
    Type<List<Type<?>>> SIGNATURE = new SignatureType();

    public abstract T read(Message message);
    public abstract void write(T value, Message message);
    public abstract void writeTypeCode(StringBuilder out);

    public default int depth() {
        return 0;
    }

    public default String typeCode() {
        StringBuilder b = new StringBuilder();
        writeTypeCode(b);
        return b.toString();
    }

    public static @Nullable Type<?> parse(@NotNull Supplier<Character> seq, ParseContext ctx) {
        char c = seq.get();
        return switch (c) {
            case 'y' -> BYTE;
            case 'b' -> BOOLEAN;
            case 'n' -> INT16;
            case 'q' -> UINT16;
            case 'i' -> INT32;
            case 'u' -> UINT32;
            case 'x' -> INT64;
            case 't' -> UINT64;
            case 'd' -> DOUBLE;

            case 's' -> STRING;
            case 'o' -> OBJECT_PATH;
            case 'g' -> SIGNATURE;

            case 'a' -> array(parse(seq));
            case '(' -> {
                List<Type<?>> types = new ArrayList<>();
                for (;;) {
                    Type<?> current = parse(seq, ParseContext.STRUCT);
                    if (current == null) yield struct(types);
                    types.add(current);
                }
            }
            case ')' -> {
                if (ctx == ParseContext.STRUCT) {
                    yield null;
                } else {
                    throw new IllegalArgumentException("unexpected end of struct");
                }
            }
            case '{' -> {
                if (ctx == ParseContext.ARRAY) {
                    throw new NotImplementedException("TODO: return a dictionary entry");
                } else {
                    throw new IllegalArgumentException("dictionary entries may only be in arrays");
                }
            }
            case '}' -> throw new IllegalArgumentException(switch (ctx) {
                case DIRECT -> "unexpected end of dictionary";
                case STRUCT -> "smoothen the bracket and try again";
                case ARRAY -> "right-to-left dictionaries are not supported";
                case KEY -> "how would that even work";
                case VALUE -> "set in disguise detected";
            });

            case 0 -> {
                if (ctx == ParseContext.DIRECT) {
                    yield null;
                } else {
                    throw new IllegalArgumentException(switch (ctx) {
                        case DIRECT -> throw new AssertionError("ctx became DIRECT after checking? impossible");
                        case STRUCT -> "unterminated struct";
                        case ARRAY -> "unterminated array";
                        case KEY -> "unterminated dict; expected key";
                        case VALUE -> "unterminated dict; expected value";
                    });
                }
            }

            default -> {
                throw new IllegalArgumentException("Bad type specifier " + c);
            }
        };
    }

    enum ParseContext {
        DIRECT,
        STRUCT,
        ARRAY,
        KEY,
        VALUE;
    }

    public static @Nullable Type<?> parse(@NotNull Supplier<Character> seq) {
        return parse(seq, ParseContext.DIRECT);
    }

    public static <T> Type<List<T>> array(Type<T> inner) {
        return new ArrayType<>(Objects.requireNonNull(inner));
    }

    public static Type<?> struct(List<Type<?>> inner) {
        return new StructType(Objects.requireNonNull(inner));
    }
}
