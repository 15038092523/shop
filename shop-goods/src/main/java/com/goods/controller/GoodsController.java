package com.goods.controller;

import com.goods.dto.GoodsDto;
import com.goods.mapper.GoodsMapper;
import com.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 根据id查询商品
     *
     * @return
     */
    @GetMapping(value = "/{id}")
    public GoodsDto get(@PathVariable("id") Integer id) {
        return goodsMapper.entityToDto(goodsService.findById(id));
    }

    @PostMapping
    public GoodsDto create(@Valid @RequestBody GoodsDto goodsDto) {
        return goodsMapper.entityToDto(goodsService.save(goodsMapper.dtoToEntity(goodsDto)));
    }

    @PutMapping
    public GoodsDto save(@Valid @RequestBody GoodsDto goodsDto) {
        return goodsMapper.entityToDto(goodsService.save(goodsMapper.dtoToEntity(goodsDto)));
    }

    @GetMapping(value = "/list")
    public List<GoodsDto> list() {
        return goodsMapper.entitiesToDtos(goodsService.findAll());
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) {
        goodsService.deleteById(id);
    }

    @ResponseBody
    @GetMapping(value = "/getInfo")
    public Map<String, Object> getInfo() throws InterruptedException {
        Thread.sleep(1000);
        return goodsService.getInfo();
    }
}
