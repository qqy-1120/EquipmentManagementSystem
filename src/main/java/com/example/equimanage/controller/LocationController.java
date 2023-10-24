package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Result;
import com.example.equimanage.pojo.Location;
import com.example.equimanage.service.LocationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Resource
    private LocationService locationService;


    /**
     * 新建位置
     * @param location
     * @return 状态码&错误信息
     */
    @PostMapping("/create")
    public Result createLocation(@RequestParam String location) {
        if(StringUtils.isBlank(location)) return Result.failure(Constants.CODE_400, "位置为空");
        else {
            QueryWrapper<Location> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("location" ,location);
            Location one = locationService.getOne(queryWrapper);
            if(one!=null) return Result.failure(Constants.CODE_400, "位置已存在");
            else {
                Location location1 = new Location();
                location1.setLocation(location);
                if(locationService.save(location1)==true) return Result.success();
                else return Result.failure(Constants.CODE_500, "服务器错误");
            }
        }
    }


    /**
     * 删除位置
     * @param location_id
     * @return 状态码&错误信息
     */
    @DeleteMapping("/{location_id}")
    public Result deleteLocation(@PathVariable Integer location_id) {
        if(locationService.removeById(location_id)) return Result.success();
        else return Result.failure(Constants.CODE_500, "服务器错误");
    }


    @GetMapping("/list")
    public Result findAll() {
        return Result.success(locationService.list());
    }
















}
