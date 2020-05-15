package com.goods.service.impl;

import com.goods.dao.GoodsDao;
import com.goods.entity.Goods;
import com.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Goods findById(Integer id) {
        return goodsDao.findOne(id);
    }

    @Override
    public List<Goods> findAll() {
        return goodsDao.findAll();
    }

    @Override
    public Goods save(Goods goods) {
        return goodsDao.save(goods);
    }

    @Override
    public void deleteById(Integer id) {
        goodsDao.delete(id);
    }

    @Override
    public Map<String, Object> getInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 200);
        map.put("info", "业务数据");
        return map;
    }
}
