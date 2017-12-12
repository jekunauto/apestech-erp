package com.apestech.framework.stateflow.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 功能：状态流转定义实体类
 *
 * @author xul
 * @create 2017-11-30 18:47
 */
@Entity
@Table(name = "sf_transition")
@Data
public class Transition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;  //状态流编号

    @Column(name = "state_process_id")
    @NotNull
    private Integer stateProcessId;  //状态流编号

    @Column(name = "type")
    @NotNull
    private int type; //流转类型（1：开始；2：结束）

    @Column(name = "source_state", length = 30)
    @NotNull
    private String sourceState;  //源状态

    @Column(name = "target_state", length = 30)
    @NotNull
    private String targetState; //目标状态

    @Column(name = "condition", length = 120)
    private String condition; //条件

    @Column(name = "process_flag", length = 30)
    @NotNull
    private int processFlag;  //是否流程任务（0：否；1：是）

}
