package com.apestech.framework.jpa.dynamic;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：查询条件容器
 *
 * @author xul
 * @create 2018-01-13 15:24
 */
public class Criteria<T> implements Specification<T> {
    private List<Criterion> criteria = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (!criteria.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (Criterion c : criteria) {
                predicates.add(c.toPredicate(root, query, builder));
            }
            // 将所有条件用 and 联合起来
            if (predicates.size() > 0) {
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return builder.conjunction();
    }

    /**
     * 增加简单条件表达式
     *
     */
    public Criteria add(Criterion criterion) {
        if (criterion != null) {
            criteria.add(criterion);
        }
        return this;
    }
}