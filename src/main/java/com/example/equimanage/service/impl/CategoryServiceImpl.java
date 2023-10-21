package com.example.equimanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.pojo.Category;
import com.example.equimanage.service.CategoryService;
import com.example.equimanage.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author qqy20001120
* @description 针对表【category(设备类别)】的数据库操作Service实现
* @createDate 2023-10-19 15:13:22
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




