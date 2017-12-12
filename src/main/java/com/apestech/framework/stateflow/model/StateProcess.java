package com.apestech.framework.stateflow.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 功能：状态流程定义实体类
 *
 * @author xul
 * @create 2017-11-30 18:46
 */
@Entity
@Table(name = "sf_process")
@Data
public class StateProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;  //状态流编号

    @Column(name = "business_id", length = 30)
    @NotNull
    private String businessId;  //业务编号

    @Column(name = "process_id", length = 30)
    private String processId;  //流程编号


    public StateProcess() {
    }
}


