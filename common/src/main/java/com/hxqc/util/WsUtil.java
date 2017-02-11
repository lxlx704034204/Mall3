package com.hxqc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WsUtil {

    /**
     * 转换
     *
     * @param <T>
     * @param c
     * @param data
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static < T > T toJavaBean(Class< T > c, Map< String, String > data) throws InstantiationException,
            IllegalAccessException {
        T t = c.newInstance();
        Method[] methods = t.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Class< ? >[] clazz = method.getParameterTypes();
                    String type = clazz[0].getName();
                    switch (type) {
                        case "java.lang.String":
                            method.invoke(t, data.get(field));
                            break;
                        case "java.util.Date":
                            method.invoke(t, Date.parse(data.get(field)));
                            break;
                        case "java.lang.Integer":
                            method.invoke(t, Integer.valueOf(data.get(field)));
                            break;
                        case "java.lang.Double":
                            method.invoke(t, Double.valueOf(data.get(field)));
                            break;
                    }
                }
            } catch (Exception e) {
            }
        }
        return t;
    }

    /**
     * 转换
     *
     * @param javabean
     * @param data
     * @return
     */
    public static Object toJavaBean(Object javabean, Map< String, String > data, String keySpace, int i) {
        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1) + keySpace + i;
                    method.invoke(javabean, data.get(field));
                }
            } catch (Exception e) {
            }
        }
        return javabean;
    }

    /**
     * 将javaBean转换成Map
     *
     * @param javaBean
     *         javaBean
     * @return Map对象
     */
    public static Map< String, String > toMap(Object javaBean) {
        Map< String, String > result = new HashMap< String, String >();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 将javaBean转换成Map JSON格式
     *
     * @param javaBean
     *         javaBean
     * @return Map对象
     */
    public static Map< String, String > toMapJOSN(Object javaBean) {
        Map< String, String > result = new HashMap< String, String >();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put("\"" + field + "\"", null == value ? "\"\"" : "\"" + value.toString() + "\"");
                }
            } catch (Exception e) {
            }
        }

        return result;
    }


    /**
     * 将不需要的属性值设置为null
     *
     * @param obj
     * @param list
     *         需要的属性列表
     * @throws Exception
     */
    public static void filterProperty(Object obj, List< String > list) throws Exception {
        Field[] FieldArr = obj.getClass().getDeclaredFields();
        for (Field aFieldArr : FieldArr) {
            String name = aFieldArr.getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            Field f = aFieldArr;
            f.setAccessible(true); // 设置些属性是可以访问的
            if (!list.contains(name)) {
                f.set(obj, null);
            }
        }

    }
}
