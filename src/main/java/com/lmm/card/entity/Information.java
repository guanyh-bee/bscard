package com.lmm.card.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Information {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String number;
    private String department;
    private Integer status;
    private Integer school;
    private String operator;
    private LocalDateTime applyTime;
    private LocalDateTime handleTime;




}
