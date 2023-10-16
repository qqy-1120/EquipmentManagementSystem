package com.example.equimanage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.equimanage.pojo.Equipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author qqy20001120
* @description 针对表【equipment】的数据库操作Mapper
* @createDate 2023-10-14 19:33:16
* @Entity com.example.equimanage.pojo.Equipment
*/

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
}




