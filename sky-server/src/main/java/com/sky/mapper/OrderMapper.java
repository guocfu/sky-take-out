package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    /**
     * 订单表中插入一条数据, 返回主键值
     * @param order
     */
    void insert(Orders order);
}
