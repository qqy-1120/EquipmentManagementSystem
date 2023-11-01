package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.equimanage.common.Result;
import com.example.equimanage.common.ErrorReport;
import com.example.equimanage.pojo.DTO.EquipmentDTO;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/api/equipment")
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
    //fixme: post为什么还要create?
    @PostMapping("/create")
    public Result createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        //fixme: create怎么可以用指定的ID呢，现在是前端指定啥ID就啥ID
        // todo: 要保证哪些字段不空？
        if(StringUtils.isBlank(equipmentDTO.getName())||StringUtils.isBlank(equipmentDTO.getCategory())
                ||StringUtils.isBlank(equipmentDTO.getNumber())){
            return Result.failure(ErrorReport.RequestParameterError.CODE, ErrorReport.RequestParameterError.MESSAGE);
        }
        else {
            Equipment equipment = new Equipment(equipmentDTO);
            //should handle failures
            if(equipmentService.save(equipment)) {
                return Result.success(equipment.getId());
            }
            return Result.failure(ErrorReport.RecordCreateError.CODE, ErrorReport.RecordCreateError.MESSAGE);
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
        Equipment equipment = new Equipment(equipmentDTO);
        Equipment one = equipmentService.getById(equipment.getId());
        if(one == null) {
            return Result.failure(ErrorReport.RequestParameterError.CODE, ErrorReport.RequestParameterError.MESSAGE);
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

        if(!equipmentService.removeById(id)) {
            return Result.failure(ErrorReport.RecordRemoveError.CODE, ErrorReport.RecordRemoveError.MESSAGE);
        }
        else {
            return Result.success(true);
        }
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
     * @param
     * @return 设备list
     */
    @GetMapping("/querypage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestParam(required = false) List<String> categories,
                             @RequestParam(required = false) List<String> usernames, @RequestParam(required = false) List<Integer> states,
                             @RequestParam(required = false) List<String> locations) {
        IPage<Equipment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Equipment> wrapper = new QueryWrapper<>();
        if (categories != null && !categories.isEmpty()) {
            wrapper.in("category", categories);
        }
        if(usernames != null && !usernames.isEmpty()) {
            wrapper.in("username", usernames);
        }
        if(states != null && !states.isEmpty()) {
            wrapper.in("state", states);
        }
        if(locations != null && !locations.isEmpty()) {
            wrapper.in("location", locations);
        }
//        if(!StringUtils.isBlank(category)) {
//            wrapper.eq("category", category);
//        }
//        if(!StringUtils.isBlank(username)){
//            wrapper.like("username", username);
//        }
//        if(state != null){
//            wrapper.eq("state", state);
//        }
//        if(!StringUtils.isBlank(location)){
//            wrapper.eq("location", location);
//        }
        wrapper.orderByAsc("state");
        wrapper.orderByAsc("receive_time");

        return Result.success(equipmentService.page(page,wrapper));
    }


}
