package com.mj.tcs.api.v1.dto.communication;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TcsResponseEntity<T> extends TcsCommEntity<T> {
    private final Status statusCode;
    private String statusMessage;

    public TcsResponseEntity() {
        this(Status.ERROR, null, Status.ERROR.toString());
    }

    public TcsResponseEntity(Status statusCode) {
        this(statusCode, null, statusCode.toString());
    }

    public TcsResponseEntity(Status statusCode, T body) {
        this(statusCode, body, statusCode.toString());
    }

    public TcsResponseEntity(Status statusCode, T body, String statusMessage) {
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

    public TcsResponseEntity setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return  true;
        }
        if (other == null || !other.getClass().equals(getClass())) {
            return false;
        }
        TcsResponseEntity<?> otherEntity = (TcsResponseEntity<?>) other;
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
//            throw new IllegalArgumentException("The TcsResponseEntity.Type enum is no recognizable [text=" + text + "]");
//        }
//    }

    /**
     * alert, warning, success, error, information
     */
    public enum Status {
        ALERT("ALERT"),
        WARNING("WARNING"),
        SUCCESS("SUCCESS"),
        ERROR("ERROR"),
        INFORMATION("INFORMATION");

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

            throw new IllegalArgumentException("The TcsResponseEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
