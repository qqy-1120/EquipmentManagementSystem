package com.example.equimanage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import com.example.equimanage.mapper.EquipmentMapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
* @author qqy20001120
* @description 针对表【equipment】的数据库操作Service实现
* @createDate 2023-10-14 19:33:16
*/
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
    implements EquipmentService{
}




