package com.apestech.framework.jpa.dynamic;

import lombok.Data;

/**
 * 功能：查询过滤条件
 *
 * @author xul
 * @create 2018-01-13 16:01
 */
@Data
public class Filter {

    public String field; //字段名
    public Object value;  //操作值
    public String operator; //运算符

}
