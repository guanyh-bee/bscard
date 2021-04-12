package com.lmm.card.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Operator {
    private Integer id;
    private String operatorName;
    private String password;
    private Integer school;
}
