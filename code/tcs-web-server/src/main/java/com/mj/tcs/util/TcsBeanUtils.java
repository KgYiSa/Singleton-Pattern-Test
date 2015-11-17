package com.mj.tcs.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * BeanUtils Extension
 *
 * @author Wang Zhen
 */
public class TcsBeanUtils extends BeanUtils {

    /**
     * Copy from one class object (or a Map object) to another class object.
     *
     * @param dst
     * @param src
     */
    public static void copyProperties(Object dst, Object src) throws RuntimeException {
        try {
            if (src instanceof Map) {
                Iterator names = ((Map) src).keySet().iterator();
                while (names.hasNext()) {
                    String name = (String) names.next();
                    if (PropertyUtils.isWriteable(dst, name)) {
                        Object value = ((Map)src).get(name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dst, name, value);
                        }
                    }
                }
            } else {
                Field[] fields = src.getClass().getDeclaredFields();
                for (int i=0; i<fields.length; i++) {
                    String name = fields[i].getName();
                    if (PropertyUtils.isReadable(src, name) && PropertyUtils.isWriteable(dst, name)) {
                        Object value = PropertyUtils.getSimpleProperty(src, name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dst, name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("copyProperties error from " + src.toString() + " to " + dst.toString(),
                    e);
        }
    }

    public static boolean checkObjProperty(Object src, Map dst) throws RuntimeException {
        try {
            Field[] fields = src.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (!dst.containsKey(name)) {
                    if (PropertyUtils.isReadable(src, name)) {
                        Object value = PropertyUtils.getSimpleProperty(src, name);
                        if (value == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("checkObjProperty error", e);
        }
    }
}
