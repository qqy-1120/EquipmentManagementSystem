package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import com.example.equimanage.pojo.Category;
import com.example.equimanage.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    /**
     * 新建类别
     * @param category
     * @return 状态码&错误信息
     */
    @PostMapping("/category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result createCategory(@RequestParam String category) {
        if(StringUtils.isBlank(category)) return Result.failure(new Response.RequestParameterError());
        else {
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category", category);
            Category one = categoryService.getOne(queryWrapper);
            if(one!=null) return Result.failure(Constants.CODE_400, "This category already exists.");
            else {
                Category category1 = new Category();
                category1.setCategory(category);
                if(categoryService.save(category1)) return Result.success();
                else return Result.failure(new Response.RecordCreateError());
            }
        }
    }


    /**
     * 删除类别
     * @param category_id
     * @return 状态码&错误信息
     */
    @DeleteMapping("/category/{category_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result deleteCategory(@PathVariable Integer category_id) {
        if(categoryService.removeById(category_id)) return Result.success();
        else return Result.failure(new Response.RecordRemoveError());
    }


    /**
     * 查询所有类别
     * @return 类别List
     */
    @GetMapping("/categories")
    public Result findAll() {
        return Result.success(categoryService.list());
    }
}
