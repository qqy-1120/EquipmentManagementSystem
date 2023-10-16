package com.example.equimanage.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.common.Constants;
import com.example.equimanage.exception.ServiceException;
import com.example.equimanage.mapper.EquipmentMapper;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
* @author qqy20001120
* @description 针对表【equipment】的数据库操作Service实现
* @createDate 2023-10-14 19:33:16
*/
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
    implements EquipmentService{

    @Value("${imgUploadPath}")
    private String imgUploadPath;

    @Resource
    private EquipmentMapper equipmentMapper;

    private static final List<String> SUPPORTED_IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");

    @Override
    public String uploadById(Integer id, MultipartFile file) throws IOException {
        // id和uploadpath存在
        Equipment one = equipmentMapper.selectById(id);
        if(one == null||file.equals("")) throw new ServiceException(Constants.CODE_400, "参数错误");
        File uploadFileDir = new File(imgUploadPath);
        if(!uploadFileDir.exists()) {
            uploadFileDir.mkdirs();
        }

        // 获取文件类型
        String fileExtension = getFileExtension(file.getOriginalFilename());
        //String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));

        // 检查文件是否为支持的图片格式
        if (!SUPPORTED_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new ServiceException(Constants.CODE_400, "文件格式不符");
        }
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + fileExtension;
        File uploadFile = new File(imgUploadPath+fileUUID);

        // 存储文件
        file.transferTo(uploadFile);

        // 存入数据库
        String url = "http://localhost:9090/upload/"+fileUUID;
        one.setPhoto_url(url);
        equipmentMapper.updateById(one);
        return url;
    }



    /**
     * 获取文件扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
    /**
     *     public String uploadById(Integer paperid, MultipartFile file) throws IOException{
     *         Paper one = paperMapper.selectById(paperid);
     *         File uploadFileDir = new File(fileUploadPath);
     *         if(!uploadFileDir.exists()) {
     *             uploadFileDir.mkdirs();
     *         }
     *         // 定义一个文件唯一的标识码
     *         String uuid = IdUtil.fastSimpleUUID();
     *         String fileUUID = uuid+".pdf";
     *         File uploadFile = new File(fileUploadPath+fileUUID);
     *         // 存储文件
     *         file.transferTo(uploadFile);
     *
     *         // 存入数据库
     *         // pdf存的是文件url
     *         String url = "http://localhost:9090/paper/"+fileUUID;
     *         one.setPdf(url);
     *         paperMapper.updateById(one);
     *         return url;
     *     }
     */
}




