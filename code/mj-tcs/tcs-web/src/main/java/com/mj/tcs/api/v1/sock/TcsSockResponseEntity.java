package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TcsSockResponseEntity<T> extends TcsSockEntity<T> {
    private final Status statusCode;
    private String statusMessage;
//    private final Type entityType;

    public TcsSockResponseEntity() {
        this(Status.ERROR, null, null);
    }

    public TcsSockResponseEntity(Status statusCode) {
        this(statusCode, null, null);
    }

    public TcsSockResponseEntity(Status statusCode, T body) {
        this(statusCode, body, null);
    }

    public TcsSockResponseEntity(Status statusCode, String statusMessage) {
        this(statusCode, null, statusMessage);
    }

    public TcsSockResponseEntity(Status statusCode, T body, String statusMessage) {
        super(body);

        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Status getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return  true;
        }
        if (other == null || !other.getClass().equals(getClass())) {
            return false;
        }
        TcsSockResponseEntity<?> otherEntity = (TcsSockResponseEntity<?>) other;
        return (super.equals(other)) &&
                (ObjectUtils.nullSafeEquals(this.statusCode, otherEntity.statusCode)) &&
                (ObjectUtils.nullSafeEquals(this.statusMessage, otherEntity.statusMessage));
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 39 +
                ObjectUtils.nullSafeHashCode(this.statusCode) * 49 +
                ObjectUtils.nullSafeHashCode(this.statusMessage)*59);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(super.toString());
        if (this.statusCode != null) {
            builder.append(this.statusCode.toString());
            if (this.statusMessage != null) {
                builder.append(',');
            }
        }

        if (this.statusMessage != null) {
            builder.append(this.statusMessage);
        }

        builder.append('>');
        return builder.toString();
    }

//    public enum Type {
//
//        POINT("POINT"),
//        SCENE("SCENE");
//
//        private String text;
//
//        Type(String text) {
//            this.text = text;
//        }
//
//        @Override
//        public String toString() {
//            return this.text;
//        }
//
//        public static Type fromString(String text) {
//            Optional<Type> type = Arrays.stream(Type.values())
//                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();
//
//            if (type.isPresent()) {
//                return type.get();
//            }
//
//            throw new IllegalArgumentException("The TcsSockResponseEntity.Type enum is no recognizable [text=" + text + "]");
//        }
//    }

    public enum Status {

        OK("OK"),
        ERROR("ERROR");

        private String text;

        Status(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static Status fromString(String text) {
            Optional<Status> type = Arrays.stream(Status.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new IllegalArgumentException("The TcsSockResponseEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
