package com.example.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/geo")
public class GeoDemoController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * [{"name":"xijiade", "x":121.547491,"y":38.8936},{"name":"jiangxiaoyu","x":121.544949,"y":38.896369}, {"name":"hefeng", "x":121.558091,"y":38.895376}]
     *
     * @param localtions
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@RequestBody List<Location> localtions){
        List<RedisGeoCommands.GeoLocation> geos = localtions.stream().map(location ->{
            Point point = new Point(location.getX(),location.getY());
            RedisGeoCommands.GeoLocation<String> geoLocation = new RedisGeoCommands.GeoLocation(location.getName(),point);
            return geoLocation;
        }).collect(Collectors.toList());
        redisTemplate.opsForGeo().add("ruanjianyuan", geos);
        return geos;
    }


    @GetMapping("/geoByDistance")
    @ResponseBody
    public Object get(@RequestParam("radius")  double radius){
        Point num22Building = new Point(121.551138, 38.894927);
        Distance distance = new Distance(radius, Metrics.NEUTRAL);
        Circle circle = new Circle(num22Building, distance);

        return redisTemplate.opsForGeo().radius("ruanjianyuan", circle);
    }
}
