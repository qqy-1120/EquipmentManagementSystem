package com.example.equimanage.service;

import com.example.equimanage.pojo.Equipment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
* @author qqy20001120
* @description 针对表【equipment】的数据库操作Service
* @createDate 2023-10-20 16:02:02
*/
public interface EquipmentService extends IService<Equipment> {

    String uploadById(Integer id, MultipartFile file) throws IOException;
}
