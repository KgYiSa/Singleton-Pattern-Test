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
public class TcsRequestEntity<T> extends TcsCommEntity<T> {

    private String uuid;
    private Action actionCode;

    public TcsRequestEntity() {
        this(null);
    }

    public TcsRequestEntity(T body) {
        super(body);
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return  true;
        }
        if (other == null || !other.getClass().equals(getClass())) {
            return false;
        }
        TcsRequestEntity<?> otherEntity = (TcsRequestEntity<?>) other;
        return (ObjectUtils.nullSafeEquals(this.uuid, otherEntity.uuid)) &&
                (ObjectUtils.nullSafeEquals(this.actionCode, otherEntity.actionCode)) &&
                (super.equals(other));
    }
    @Override
    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.uuid) * 29 +
                ObjectUtils.nullSafeHashCode(this.actionCode)*39 +
                super.hashCode());
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if (this.uuid != null) {
            builder.append(this.uuid);
        }
        if (this.actionCode != null) {
            builder.append(',')
                    .append(this.actionCode.toString());
        }
        if (super.toString() != null) {
            builder.append(',')
                    .append(super.toString());
        }
        builder.append('>');
        return builder.toString();
    }

    public enum Action {
        UNKNOWN("UNKNOWN"),
        SCENE_PROFILE("SCENE_PROFILE"),
        SCENE_CREATE("SCENE_CREATE"),// WARNING: LAZY LOAD!!
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
            throw new IllegalArgumentException("The TcsRequestEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
