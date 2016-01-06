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
public class TCSRequestEntity<T> extends TCSCommEntity<T> {

    @JsonProperty("uuid")
    private String requestUUID;
    private Action actionCode;

    public TCSRequestEntity() {
        this(null);
    }

    public TCSRequestEntity(T body) {
        super(body);
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String uuid) {
        this.requestUUID = uuid;
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
        TCSRequestEntity<?> otherEntity = (TCSRequestEntity<?>) other;
        return (ObjectUtils.nullSafeEquals(this.requestUUID, otherEntity.requestUUID)) &&
                (ObjectUtils.nullSafeEquals(this.actionCode, otherEntity.actionCode)) &&
                (super.equals(other));
    }
    @Override
    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.requestUUID) * 29 +
                ObjectUtils.nullSafeHashCode(this.actionCode)*39 +
                super.hashCode());
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if (this.requestUUID != null) {
            builder.append(this.requestUUID);
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
        // Scene Operations (General)
        SCENE_PROFILE("SCENE_PROFILE"),
        SCENE_CREATE("SCENE_CREATE"),// WARNING: LAZY LOAD!!
        SCENE_FIND("SCENE_FIND"),
        SCENE_DELETE("SCENE_DELETE"),
        SCENE_START("SCENE_START"),
        SCENE_STOP("SCENE_STOP"),
        // Scene Operations (Specific)
        SCENE_SPECIFIC_PROFILE("SCENE_SPECIFIC_PROFILE"),
        // Transport Order Operations
        TO_NEW("TO_NEW"),
        TO_WITHDRAW("TO_WITHDRAW"),
        // Vehicle Operations
        VEHICLE_PROFILE("VEHICLE_PROFILE"),
        VEHICLE_ADAPTER_ATTACH("VEHICLE_ADAPTER_ATTACH"),
        VEHICLE_DISPATCH("VEHICLE_DISPATCH"),
        VEHICLE_STOP_TO("VEHICLE_STOP_TO"),
        VEHICLE_PARK("VEHICLE_PARK")
        ;
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
            throw new IllegalArgumentException("The TCSRequestEntity.Action enum is no recognizable [text=" + text + "]");
        }
    }
}
