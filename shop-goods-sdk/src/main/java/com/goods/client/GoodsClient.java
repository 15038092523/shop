package com.goods.client;

import com.goods.dto.GoodsDto;
import com.goods.fallback.GoodsClientFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "SHOP-GOODS", fallbackFactory = GoodsClientFallbackFactory.class)
@RequestMapping("/goods")
public interface GoodsClient {

    @GetMapping(value = "/{id}")
    GoodsDto get(@PathVariable("id") Integer id);

    @PostMapping
    GoodsDto create(@RequestBody GoodsDto goodsDto);

    @PutMapping
    GoodsDto save(@RequestBody GoodsDto goodsDto);

    @GetMapping(value = "/list")
    List<GoodsDto> list();

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value = "/getInfo")
    Map<String, Object> getInfo();
}

