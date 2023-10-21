package com.example.equimanage.mapper;

import com.example.equimanage.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-10-14 18:10:47
* @Entity com.example.equimanage.pojo.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{text}")
    User findUserByName(String text);
}




