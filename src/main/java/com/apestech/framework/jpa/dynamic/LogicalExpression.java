package com.apestech.framework.jpa.dynamic;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：逻辑条件表达式 用于复杂条件时使用，如单属性多对应值的OR查询等
 *
 * @author xul
 * @create 2018-01-13 15:29
 */
public class LogicalExpression implements Criterion {
    /**
     * 逻辑表达式中包含的表达式
     */
    private Criterion[] criteria;
    /**
     * 计算符
     */
    private Operator operator;

    public LogicalExpression(Criterion[] criteria, Operator operator) {
        this.criteria = criteria;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (int i = 0; i < this.criteria.length; i++) {
            predicates.add(this.criteria[i].toPredicate(root, query, builder));
        }
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            default:
                return null;
        }
    }

}