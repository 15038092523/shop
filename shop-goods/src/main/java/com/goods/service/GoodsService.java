package com.goods.service;

import com.goods.entity.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    Goods findById(Integer id);

    List<Goods> findAll();

    Goods save(Goods goods);

    void deleteById(Integer id);

    Map<String, Object> getInfo();
}
