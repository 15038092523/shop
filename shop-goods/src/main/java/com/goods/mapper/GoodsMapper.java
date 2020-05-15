package com.goods.mapper;

import com.goods.dto.GoodsDto;
import com.goods.entity.Goods;
import com.shop.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class GoodsMapper extends EntityMapper<Goods, GoodsDto> {
}
