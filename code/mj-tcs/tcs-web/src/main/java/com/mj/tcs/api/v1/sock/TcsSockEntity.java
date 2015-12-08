package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TcsSockEntity<T> {

    private String uuid;
    private Action actionCode;
    private final T body;

    private String timestamp;

    public TcsSockEntity() {
        this(Action.UNKNOWN, null);
    }

    public TcsSockEntity(Action action) {
        this(action, null);
    }

    public TcsSockEntity(T body) {
        this(Action.UNKNOWN, body);
    }

    public TcsSockEntity(Action action, T body) {
        this.actionCode = action;
        this.body = body;

        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS").format(new Date());
    }

    public T getBody() {
        return body;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public Action getActionCode() {
        return actionCode;
    }

    public void setActionCode(Action actionCode) {
        this.actionCode = actionCode;
    }

    public String getTimestamp() {
        return timestamp;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return  true;
        }
        if (other == null || !other.getClass().equals(getClass())) {
            return false;
        }
        TcsSockEntity<?> otherEntity = (TcsSockEntity<?>) other;
        return (ObjectUtils.nullSafeEquals(this.uuid, otherEntity.uuid)) &&
                (ObjectUtils.nullSafeEquals(this.actionCode, otherEntity.actionCode)) &&
                (ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
    }

    @Override
    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.uuid) * 29 +
                ObjectUtils.nullSafeHashCode(this.actionCode)*39 +
                ObjectUtils.nullSafeHashCode(this.body));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if (this.uuid != null) {
            builder.append(this.body);
        }
        if (this.actionCode != null) {
            builder.append(',')
                   .append(this.actionCode.toString());
        }

        if (this.body != null) {
            builder.append(',')
                   .append(this.body);
        }

        builder.append('>');
        return builder.toString();
    }

    public enum Action {
        UNKNOWN("UNKNOWN"),
        SCENE_PROFILE("SCENE_PROFILE"),
        SCENE_CREATE("SCENE_CREATE"),
        SCENE_FIND("SCENE_FIND"),
        SCENE_DELETE("SCENE_DELETE"),
        SCENE_START("SCENE_START"),
        SCENE_STOP("SCENE_STOP");


        private String text;

        Action(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static Action fromString(String text) {
            Optional<Action> type = Arrays.stream(Action.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new IllegalArgumentException("The TcsSockResponseEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
