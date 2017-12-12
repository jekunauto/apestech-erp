package com.apestech.scm.model;

import lombok.*;

import javax.persistence.*;

/**
 * 功能：订单类
 *
 * @author xul
 * @create 2017-11-17 14:58
 */

@Entity
@Table(name = "t_order")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    @Column(name = "user_name", length = 100)
    private String userName;

    public Order() {
    }
}
