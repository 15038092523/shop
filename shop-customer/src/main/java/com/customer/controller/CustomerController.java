package com.customer.controller;

import com.goods.client.GoodsClient;
import com.goods.dto.GoodsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private GoodsClient goodsClient;

    // private final static String PRE_HOST = "http://SHOP-GOODS";

    @PostMapping(value = "/goods")
    private GoodsDto save(@RequestBody GoodsDto goodDto) {
        return goodsClient.save(goodDto);
    }


    @GetMapping(value = "/goods/list")
    public List<GoodsDto> list() {
        return goodsClient.list();
    }

    @GetMapping(value = "/getInfo")
    @ResponseBody
    public Map<String, Object> getInfo() {
        return goodsClient.getInfo();
    }
}

