package com.example.equimanage.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import com.example.equimanage.mapper.EquipmentMapper;
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
* @createDate 2023-10-20 16:02:02
*/
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
    implements EquipmentService{

    @Value("${imgUploadPath}")
    private String imgUploadPath;

    @Value("${server.port}")
    private String port;

    @Value("${imgAddressPrefix}")
    private String adr;

    @Resource
    private EquipmentMapper equipmentMapper;

    private static final List<String> SUPPORTED_IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");

    @Override
    public String uploadById(Integer id, MultipartFile file) throws IOException {
        // id和uploadpath存在
        //fixme: should we handle exceptions during selectById???
        // 这里不应该出错，是unchecked exception，应该报修
        Equipment one;
        try{
            one = equipmentMapper.selectById(id);
        } catch (Exception e) {
            throw new RequestHandlingException(new Response.RecordRetrieveError());
        }

        if (one == null || file.equals("")) throw new RequestHandlingException(new Response.RequestParameterError());
        File uploadFileDir = new File(imgUploadPath);

        if (!uploadFileDir.exists()) {
            //fixme: this should not happen, when it happens, it is an internal error
            // 如果通过配置文件指定新的path呢？
            uploadFileDir.mkdirs();
        }

        // 获取文件类型
        String fileExtension = getFileExtension(file.getOriginalFilename());
        //String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));

        // 检查文件是否为支持的图片格式
        if (!SUPPORTED_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new RequestHandlingException(new Response.UnsupportedFileFormatError());
        }
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + fileExtension;
        File uploadFile = new File(imgUploadPath + fileUUID);

        // 存储文件
        file.transferTo(uploadFile);

        // 存入数据库
        // fixme:不要写死！！！ 存储功能和service无关，应该定义在common util下面
        // fixed，格式固定因为要在url和file之间实现映射
        String url = adr + ":" + port + "/img/" + fileUUID;

        one.setPhoto_url(url);
        //fixme: should we handle exceptions during updateById???
        // fixed
        try{
            equipmentMapper.updateById(one);
        } catch (Exception e) {
            throw new RequestHandlingException(new Response.RecordCreateError());
        }

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
}




