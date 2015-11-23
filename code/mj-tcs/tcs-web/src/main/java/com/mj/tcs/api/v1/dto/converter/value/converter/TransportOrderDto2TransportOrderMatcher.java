/*******************************************************************************
 * mj-tcs copyright information:
 * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
 * All rights reserved.
 ******************************************************************************/

package com.mj.tcs.api.v1.dto.converter.value.converter;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.data.order.TransportOrder;

/**
 * @author Wang Zhen
 */
public class TransportOrderDto2TransportOrderMatcher implements DtoToEntityMatcher<TransportOrderDto, TransportOrder> {
    @Override
    public boolean match(TransportOrderDto dto, TransportOrder entity) {
        return dto.getId() == entity.getId();
    }
}
