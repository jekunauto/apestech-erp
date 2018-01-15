package com.apestech.framework.jpa.dynamic;

/**
 * 功能：查询条件接口
 *
 * @author xul
 * @create 2018-01-13 15:23
 */
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Criterion {
    enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR, IS_MEMBER, IS_NOT_MEMBER, IS_NULL, IS_NOT_NULL
    }

    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}