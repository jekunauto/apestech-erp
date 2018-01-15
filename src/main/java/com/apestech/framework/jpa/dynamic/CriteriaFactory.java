package com.apestech.framework.jpa.dynamic;

import com.apestech.framework.util.Tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 功能：查询条件容器工厂类
 *
 * @author xul
 * @create 2018-01-13 15:54
 */
public class CriteriaFactory {

    public static <T> Criteria<T> toCriteria(final List rows) {
        List<Filter> filters = getFilters(rows);
        return toCriteria(filters, true);
    }

    private static List<Filter> getFilters(List<Map> o) {
        List<Filter> rows = new ArrayList();
        for (Map<String, Object> row : o) {
            rows.add(Tools.toBean(Filter.class, row)); //数据类型可以不一致
        }
        return rows;
    }

    public static <T> Criteria<T> toCriteria(final List<Filter> filters, boolean ignoreNull) {
        Criteria<T> criteria = new Criteria<>();
        for (Filter filter : filters) {
            switch (filter.operator) {
                case "EQ":
                    criteria.add(Restrictions.eq(filter.field, filter.value, ignoreNull));
                    break;
                case "NE":
                    criteria.add(Restrictions.ne(filter.field, filter.value, ignoreNull));
                    break;
                case "LIKE":
                    criteria.add(Restrictions.like(filter.field, (String) filter.value, ignoreNull));
                    break;
                case "GT":
                    criteria.add(Restrictions.gt(filter.field, filter.value, ignoreNull));
                    break;
                case "LT":
                    criteria.add(Restrictions.lt(filter.field, filter.value, ignoreNull));
                    break;
                case "GTE":
                    criteria.add(Restrictions.gte(filter.field, filter.value, ignoreNull));
                    break;
                case "LTE":
                    criteria.add(Restrictions.lte(filter.field, filter.value, ignoreNull));
                    break;
                case "IN":
                    criteria.add(Restrictions.in(filter.field, (Collection) filter.value, ignoreNull));
                    break;
                case "IS_NULL":
                    criteria.add(Restrictions.isNull(filter.field, false));
                    break;
                case "IS_NOT_NULL":
                    criteria.add(Restrictions.isNotNull(filter.field, false));
                    break;
            }
        }
        return criteria;
    }

}
