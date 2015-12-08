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
public class TcsSockRequestEntity<T> extends TcsSockEntity<T> {

    public TcsSockRequestEntity() {
        this(Action.UNKNOWN, null);
    }

    public TcsSockRequestEntity(Action action) {
        this(action, null);
    }

    public TcsSockRequestEntity(Action action, T body) {
        super(action, body);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return  true;
        }
        if (other == null || !other.getClass().equals(getClass())) {
            return false;
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
