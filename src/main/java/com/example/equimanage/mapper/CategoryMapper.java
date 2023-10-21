package com.example.equimanage.mapper;

import com.example.equimanage.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author qqy20001120
* @description 针对表【category(设备类别)】的数据库操作Mapper
* @createDate 2023-10-19 15:13:22
* @Entity com.example.equimanage.pojo.Category
*/

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




