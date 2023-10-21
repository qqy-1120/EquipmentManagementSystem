package com.example.equimanage.mapper;

import com.example.equimanage.pojo.Location;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author qqy20001120
* @description 针对表【location(设备位置)】的数据库操作Mapper
* @createDate 2023-10-19 15:14:26
* @Entity com.example.equimanage.pojo.Location
*/

@Mapper
public interface LocationMapper extends BaseMapper<Location> {

}




