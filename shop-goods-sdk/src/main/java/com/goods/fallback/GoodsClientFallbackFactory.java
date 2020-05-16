package com.goods.fallback;

import com.goods.client.GoodsClient;
import com.goods.dto.GoodsDto;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoodsClientFallbackFactory implements FallbackFactory<GoodsClient> {
    @Override
    public GoodsClient create(Throwable throwable) {
        return new GoodsClient() {

            @Override
            public GoodsDto save(GoodsDto goodsDto) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<GoodsDto> list() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Map<String, Object> getInfo() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", 500);
                map.put("info", "系统出错，稍后重试");
                return map;
            }

            @Override
            public GoodsDto get(Integer id) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public GoodsDto create(GoodsDto goodsDto) {
                return null;
            }

            @Override
            public void delete(Integer id) {
                // TODO Auto-generated method stub
            }
        };
    }
}
