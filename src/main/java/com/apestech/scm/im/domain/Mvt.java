package com.apestech.scm.im.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;


/**
 * 功能：移动类型
 *
 * @author xul
 * @create 2018-01-15 15:46
 */
@Entity
@Table(name = "im_mvt", indexes = {@Index(name = "idx_im_mvt_code", columnList = "code", unique = true)})
@Data
public class Mvt {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)

    @GenericGenerator(name = "id_gen", strategy = "enhanced-table",
            parameters = {@Parameter(name = "table_name", value = "enhanced_gen"),
                    @Parameter(name = "value_column_name", value = "next"),
                    @Parameter(name = "segment_column_name", value = "segment_name"),
                    @Parameter(name = "segment_value", value = "emp_seq"),
                    @Parameter(name = "increment_size", value = "10"),
                    @Parameter(name = "optimizer", value = "pooled-lo")
            })
    @GeneratedValue(generator = "id_gen")
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", nullable = false, length = 10) //, unique = true
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;//描述

//    @Column(name = "mvi")
//    private MvI mvi; //移动标识

    @Column(name = "sign", nullable = false, length = 2)
    private String sign; //记帐符号("S":"借方", "H":"贷方")

}
