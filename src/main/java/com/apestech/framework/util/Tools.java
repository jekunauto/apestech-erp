package com.apestech.framework.util;

import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：普通工具类
 *
 * @author xul
 * @create 2017-12-02 16:28
 */
public class Tools {

    /**
     * 此方法检查一个字符串是否为空。 <p>
     *
     * @param s String - 检查的对象字符串。<p>
     * @return boolean - 如果是空串，则返回true，否则返回false。
     */
    public static boolean isNull(String s) { //检查传入字符串是否为空
        return (s == null) || s.trim().equals("");
    }

    public static boolean isNull(Object o) {
        return (o == null) || String.valueOf(o).equals("");
    }


    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static final Map<String, Object> toMap(Object bean) {
        try {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
            return returnMap;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a map to a JavaBean.
     *
     * @param type type to convert
     * @param map  map to convert
     * @return JavaBean converted
     */
//    public static final <T> T toBean(Class<?> type, Map<String, ? extends Object> map){
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(type);
//            Object obj = type.newInstance();
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//            for (int i = 0; i < propertyDescriptors.length; i++) {
//                PropertyDescriptor descriptor = propertyDescriptors[i];
//                String propertyName = descriptor.getName();
//                if (map.containsKey(propertyName)) {
//                    Object value = map.get(propertyName);
//                    Object[] args = new Object[1];
//                    args[0] = value;
//                    descriptor.getWriteMethod().invoke(obj, args);
//                }
//            }
//            return (T) obj;
//        } catch (IntrospectionException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static <T> T toBean(Class<?> type, Map<String, ? extends Object> row){
        try {
            T bean = (T) type.newInstance();
            for (Map.Entry<String, ?> entry : row.entrySet()) {
                BeanUtils.setProperty(bean, entry.getKey(), entry.getValue());
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 两个Bean之间属性对拷
     * @param <T>
     * @param dest  现将要设置新值的对象
     * @param source     源数据对象
     */
    public static <T> void  beanConvert(T dest,T source){
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String replace(String s){
        return ((PropertiesUtils) SpringManager.getBean("propertiesUtils")).replace(s);
    }
}
