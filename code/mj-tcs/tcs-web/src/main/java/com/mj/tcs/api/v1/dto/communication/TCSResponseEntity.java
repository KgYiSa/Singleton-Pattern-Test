package com.mj.tcs.api.v1.dto.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TCSResponseEntity<T> extends TCSCommEntity<T> {

    @JsonProperty("uuid")
    private String responseUUID;
    private final Status statusCode;
    private String statusMessage;

    public TCSResponseEntity() {
        this(Status.ERROR, null, Status.ERROR.toString());
    }

    public TCSResponseEntity(Status statusCode) {
        this(statusCode, null, statusCode.toString());
    }

    public TCSResponseEntity(Status statusCode, T body) {
        this(statusCode, body, statusCode.toString());
    }

    public TCSResponseEntity(Status statusCode, T body, String statusMessage) {
        super(body);

        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String getResponseUUID() {
        return responseUUID;
    }

    public void setResponseUUID(String uuid) {
        this.responseUUID = uuid;
    }

    public Status getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public TCSResponseEntity setStatusMessage(String statusMessage) {
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
        TCSResponseEntity<?> otherEntity = (TCSResponseEntity<?>) other;
        return (super.equals(other)) &&
                (ObjectUtils.nullSafeEquals(this.responseUUID, otherEntity.responseUUID)) &&
                (ObjectUtils.nullSafeEquals(this.statusCode, otherEntity.statusCode)) &&
                (ObjectUtils.nullSafeEquals(this.statusMessage, otherEntity.statusMessage));
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 39 +
                ObjectUtils.nullSafeHashCode(this.responseUUID) * 49 +
                ObjectUtils.nullSafeHashCode(this.statusCode) * 59 +
                ObjectUtils.nullSafeHashCode(this.statusMessage)*69);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(super.toString());
        if (this.responseUUID != null) {
            builder.append(this.responseUUID);
        }
        if (this.statusCode != null) {
            builder.append(',')
                    .append(this.statusCode.toString());
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
//            throw new IllegalArgumentException("The TCSResponseEntity.Type enum is no recognizable [text=" + text + "]");
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

            throw new IllegalArgumentException("The TCSResponseEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
