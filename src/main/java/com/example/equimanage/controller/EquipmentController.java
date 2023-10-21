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
     * @return id
     */
    @PostMapping("/create")
    public Result createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        // todo: 要保证哪些字段不空？
        if(StringUtils.isBlank(equipmentDTO.getName())||StringUtils.isBlank(equipmentDTO.getCategory())
                ||StringUtils.isBlank(equipmentDTO.getNumber())){
            return Result.failure(Constants.CODE_400, "参数不足");
        } else {
            Equipment equipment=copyProperties(equipmentDTO);
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
     * @return id
     */
    @PutMapping("/update")
    public Result updateEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment = copyProperties(equipmentDTO);
        Equipment one = equipmentService.getById(equipment.getId());
        if(one == null) {
            return Result.failure(Constants.CODE_400, "参数错误");
        }
        else {
            equipmentService.updateById(equipment);
            return Result.success(equipment.getId());
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
     * 多条件分页查询
     * @param pageNum
     * @param pageSize
     * @param equipmentDTO
     * @return 设备list
     */
    @GetMapping("/querypage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody EquipmentDTO equipmentDTO) {
        IPage<Equipment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Equipment> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(equipmentDTO.getCategory())){
            wrapper.eq("category", equipmentDTO.getCategory());
        }
        if(!StringUtils.isBlank(equipmentDTO.getUsername())){
            wrapper.like("username", equipmentDTO.getUsername());
        }
        if(equipmentDTO.getState() != null){
            wrapper.eq("state", equipmentDTO.getState());
        }
        if(!StringUtils.isBlank(equipmentDTO.getLocation())){
            wrapper.eq("location", equipmentDTO.getLocation());
        }
        wrapper.orderByAsc("state");
        wrapper.orderByAsc("receive_time");

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
        equipment.setState(equipmentDTO.getState());
        equipment.setIs_receive(equipmentDTO.getIs_receive());
        equipment.setReceive_time(equipmentDTO.getReceive_time());
        equipment.setUser_id(equipmentDTO.getUser_id());
        equipment.setUsername(equipmentDTO.getUsername());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConfiguration(equipmentDTO.getConfiguration());
        return equipment;
    }
}
