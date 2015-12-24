package com.mj.tcs.api.v1.dto.communication;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wang Zhen
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TCSCommEntity<T> {

    private final T body;

    private String timestamp;

    public TCSCommEntity() {
        this(null);
    }

    public TCSCommEntity(T body) {
        this.body = body;

        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS").format(new Date());
    }

    public T getBody() {
        return body;
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
        TCSCommEntity<?> otherEntity = (TCSCommEntity<?>) other;
        return (ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.body);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");

        if (this.body != null) {
            builder.append(this.body);
        }
        if (this.timestamp != null) {
            builder.append(',')
                    .append(this.timestamp);
        }

        builder.append('>');
        return builder.toString();
    }
}
