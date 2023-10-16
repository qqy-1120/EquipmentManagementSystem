package com.example.equimanage.pojo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class EquipmentDTO {
    private Integer id;
    private String name;
    @JsonProperty("class")
    private String category;
    private Date buy_time;
    private String number;
    private String user;
    private Date receive_time;
    private Integer state;
    // 没有photo
    private String location;
    private String configuration;
}
