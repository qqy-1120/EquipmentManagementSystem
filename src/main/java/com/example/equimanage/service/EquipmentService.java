package com.example.equimanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.equimanage.pojo.Equipment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author qqy20001120
* @description 针对表【equipment】的数据库操作Service
* @createDate 2023-10-14 19:33:16
*/
public interface EquipmentService extends IService<Equipment> {
    String uploadById(Integer id, MultipartFile file) throws IOException;
}
