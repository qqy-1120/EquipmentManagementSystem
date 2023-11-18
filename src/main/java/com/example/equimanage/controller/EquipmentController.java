package com.example.equimanage.controller;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.mapper.EquipmentMapper;
import com.example.equimanage.pojo.DTO.EquipmentDTO;
import com.example.equimanage.pojo.Equipment;
import com.example.equimanage.service.EquipmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/api")
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
    // fixed
    @PostMapping("/equipment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        System.out.println("helloworld");
        //fixme: create怎么可以用指定的ID呢，现在是前端指定啥ID就啥ID？？
        // 那就保存之前清空ID
        // 一般要把sanity check单独放到一个函数里
        if(StringUtils.isBlank(equipmentDTO.getName())||StringUtils.isBlank(equipmentDTO.getCategory())
                ||StringUtils.isBlank(equipmentDTO.getNumber())){
            throw new RequestHandlingException(new Response.RequestParameterError());
        }
        else {
            Equipment equipment = new Equipment(equipmentDTO);
            equipment.setId(null);
            //fixme: should we handle exceptions?
            // unchecked exception
            if(equipmentService.save(equipment)) {
                return Result.success(equipment.getId());
            }
            throw new RequestHandlingException(new Response.RecordCreateError());
        }
    }


    /**
     * 上传图片
     * @param id
     * @param file
     * @return 图片url
     * @throws IOException
     */
    private final static Log LOG = Log.get();
    @PostMapping("/equipment/image/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result uploadImage(@PathVariable Integer id, @RequestParam MultipartFile file) throws IOException {
        //fixme: IOException谁处理，能正确处理吗？
        // 还不会处理，暂时只能返回错误信息，等遇到再改。。。
        //return Result.success(equipmentService.uploadById(id, file));
        String res;
        try{
            res=equipmentService.uploadById(id,file);
        }catch (Exception e){
            return Result.failure(Constants.CODE_500, e.getMessage());
        }
        return Result.success(res);
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
    @PutMapping("/equipment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result updateEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment(equipmentDTO);
        //fixme: 是不是应该处理数据库的exception
        Equipment one;
        try {
           one = equipmentService.getById(equipment.getId());
        } catch (Exception e) {
            throw new RequestHandlingException(new Response.RecordRetrieveError());
        }
        if(one == null) {
            return Result.failure(new Response.RequestParameterError());
        }
        else {
            //fixme: 是不是应该处理数据库的exception
            // fixed
            try {
                equipmentService.updateById(equipment);
            } catch (Exception e) {
                throw new RequestHandlingException(new Response.RecordCreateError());
            }
            return Result.success(equipment.getId());
        }
    }


    /**
     * 删除设备
     * @param id
     * @return 1 or 0
     */
    @DeleteMapping("/equipment/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result deleteEquipment(@PathVariable Integer id) {
        //fixme: 是不是应该处理数据库的exception
        if(!equipmentService.removeById(id)) {
            return Result.failure(new Response.RecordRemoveError());
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
    @GetMapping("/equipment/page/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        //fixme: 是不是应该处理数据库的exception
        IPage<Equipment> page = new Page<>(pageNum, pageSize);
        IPage<Equipment> res;
        try {
            res = equipmentService.page(page);
        }catch (Exception e) {
            throw new RequestHandlingException(new Response.RecordRetrieveError());
        }
        return Result.success(res);
    }


    /**
     * 多条件分页查询
     * @param pageNum
     * @param pageSize
     * @param
     * @return 设备list
     */
    @GetMapping("/equipment/querypage/{pageNum}/{pageSize}")
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
        wrapper.orderByAsc("state");
        wrapper.orderByAsc("receive_time");

        return Result.success(equipmentService.page(page,wrapper));
    }

    @Resource
    private EquipmentMapper equipmentMapper;
    @GetMapping("/equipments")
    public Result findAll(){
        //List res = equipmentService.list();
        List res = equipmentMapper.findAll();
        if(res == null) {
            throw new RequestHandlingException(new Response.RecordRetrieveError());
        }
        return Result.success(res);
    }


}
