package com.example.equimanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.pojo.Location;
import com.example.equimanage.service.LocationService;
import com.example.equimanage.mapper.LocationMapper;
import org.springframework.stereotype.Service;

/**
* @author qqy20001120
* @description 针对表【location(设备位置)】的数据库操作Service实现
* @createDate 2023-10-19 15:14:26
*/
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location>
    implements LocationService{

}




