package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Result;
import com.example.equimanage.pojo.DTO.EquipmentDTO;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    @Value("${imgUploadPath}")
    private String imgUploadPath;


    /**
     * 新建设备（不带图片）
     * @param equipmentDTO
     * @return 设备ID
     */
    @PostMapping("/create")
    public Result createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment=copyProperties(equipmentDTO);
        if(StringUtils.isBlank(equipmentDTO.getName())||StringUtils.isBlank(equipmentDTO.getCategory())
                ||StringUtils.isBlank(equipmentDTO.getNumber())||StringUtils.isBlank(equipmentDTO.getUser())){
            return Result.failure(Constants.CODE_400, "参数不足");
        } else {
            equipmentService.save(equipment);
            return Result.success(equipment.getId());
        }
    }


    /**
     * 上传图片
     * @param id
     * @param file
     * @return 图片url
     * @throws IOException
     */
    @PostMapping("/upload/{id}")
    public Result uploadImage(@PathVariable Integer id, @RequestParam MultipartFile file) throws IOException {
        return Result.success(equipmentService.uploadById(id, file));
    }


//    /**
//     * 下载图片
//     */

//    @GetMapping("/{fileUUID}")
//    public void imageDownload(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
//        // 根据文件唯一标识符获取文件
//        File uploadFile = new File(imgUploadPath+fileUUID);
//        // 设置输出流的格式
//        ServletOutputStream os = response.getOutputStream();
//        response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileUUID, StandardCharsets.UTF_8));
//        response.setContentType("application/octet-stream");
//
//        // 读取文件的字节流
//        os.write(FileUtil.readBytes(uploadFile));
//        os.flush();
//        os.close();
//    }



    /**
     * 修改设备信息
     * @param equipmentDTO
     * @return 1 or 0
     */
    @PutMapping("/update")
    public Result updateEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment = copyProperties(equipmentDTO);
        Equipment one = equipmentService.getById(equipment.getId());
        if(one == null) {
            return Result.failure(Constants.CODE_400, "参数错误");
        }
        else {
            return Result.success(equipmentService.updateById(equipment));
        }
    }


    /**
     * 删除设备
     * @param id
     * @return 1 or 0
     */
    @DeleteMapping("/{id}")
    public Result deleteEquipment(@PathVariable Integer id) {
        return Result.success(equipmentService.removeById(id));
    }


    /**
     * 分页查询，不带搜索项
     * @param pageNum
     * @param pageSize
     * @return 设备list
     */
    @GetMapping("/page/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {

        IPage<Equipment> page = new Page<>(pageNum, pageSize);
        return Result.success(equipmentService.page(page));
    }


    /**
     * 分页查询，模糊匹配
     * @param pageNum
     * @param pageSize
     * @param searchTerm
     * @return 设备list
     */
    @GetMapping("/page/{pageNum}/{pageSize}/{searchTerm}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String searchTerm) {
        IPage<Equipment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Equipment> wrapper = new QueryWrapper<>();
        wrapper.like("name", searchTerm)
                .or()
                .like("class", searchTerm)
                .or()
                .like("number", searchTerm)
                .or()
                .like("user", searchTerm)
                .or() // 没有按状态查询
                .like("location", searchTerm)
                .or()
                .like("configuration", searchTerm);

        return Result.success(equipmentService.page(page,wrapper));
    }


    /**
     * 把equipmentDTO的属性copy到Equipment实例
     * @param equipmentDTO
     * @return Equipment实例
     */
    private Equipment copyProperties(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setId(equipmentDTO.getId()); // 需要考虑为空
        equipment.setName(equipmentDTO.getName());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setBuy_time(equipmentDTO.getBuy_time());
        equipment.setNumber(equipmentDTO.getNumber());
        equipment.setUser(equipmentDTO.getUser());
        equipment.setReceive_time(equipmentDTO.getReceive_time());
        equipment.setState(equipmentDTO.getState());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConfiguration(equipmentDTO.getConfiguration());
        return equipment;
    }
}
