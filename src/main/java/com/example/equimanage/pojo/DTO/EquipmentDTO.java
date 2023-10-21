package com.example.equimanage.pojo.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class EquipmentDTO {
    private Integer id;
    private String name;
    private String category;
    private Date buy_time;
    private String number;
    private Integer state;
    private Integer is_receive;
    private Date receive_time;
    private Integer user_id;
    private String username;
    // 没有photo
    private String location;
    private String configuration;
}
