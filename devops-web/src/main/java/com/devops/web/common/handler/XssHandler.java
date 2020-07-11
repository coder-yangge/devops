package com.devops.web.common.handler;

import com.devops.web.common.annotation.XssFilter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yangge
 * @version 1.0.0
 * @title: XssHandler
 * @description: xss过滤
 * @date 2020/4/21 17:38
 */
@Slf4j
public class XssHandler {

    public static final Whitelist DEFAULT_WHITE_LIST = Whitelist.basicWithImages().addAttributes("all", "style");

    public static final Document.OutputSettings DEFAULT_OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    public static final int DEFAULT_CLEAN_DEPTH = 5;

    private static final String STANDARD_CLASS = "com.standard";

    public Object xssCleanObject(Object result) throws Exception {
        return xssCleanObject(0, DEFAULT_CLEAN_DEPTH, null, result);
    }

    private Object xssCleanObject(int currentTime, int maxCleanDepth, Field parentField, Object result) throws Exception {
        if (result == null) {
            return null;
        }

        if (currentTime >= maxCleanDepth) {
            return result;
        }

        final int nextDepth = currentTime + 1;
        Class<?> resultClass = result.getClass();
        if (String.class.isAssignableFrom(resultClass)) {
            if (resultClass.isAnnotationPresent(XssFilter.class) || (parentField != null && parentField.isAnnotationPresent(XssFilter.class))) {
                return Jsoup.clean((String) result, "", DEFAULT_WHITE_LIST, DEFAULT_OUTPUT_SETTINGS);
            }
        } else if (List.class.isAssignableFrom(resultClass)) {
            wrapperNewObjList(parentField, (List<Object>) result, nextDepth);
            return result;

        } else if (Set.class.isAssignableFrom(resultClass)) {
            wrapperNewObjSet(parentField, (Set<Object>) result, nextDepth);
            return result;

        } else if (Map.class.isAssignableFrom(resultClass)) {
            wrapperNewObjMap(parentField, (Map<Object, Object>) result, nextDepth);
            return result;

        } else if (!isStandardClass(result)) {
            return result;
        }

        Field[] declaredFields = findAllDeclaredFields(resultClass);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = field.get(result);
            if (value == null) {
                continue;
            }

            Class<?> clazz = value.getClass();
            if (clazz.isAssignableFrom(Object.class)) {
                if (isStandardClass(value)) {
                    field.set(result, xssCleanObject(nextDepth, maxCleanDepth, null, value));
                }
            } else if (String.class.isAssignableFrom(clazz)) {
                if (field.isAnnotationPresent(XssFilter.class)) {
                    field.set(result, Jsoup.clean((String) value, "", DEFAULT_WHITE_LIST, DEFAULT_OUTPUT_SETTINGS));
                }
            } else if (List.class.isAssignableFrom(clazz)) {
                wrapperNewObjList(field, (List<Object>) value, nextDepth);

            } else if (Set.class.isAssignableFrom(clazz)) {
                wrapperNewObjSet(field, (Set<Object>) value, nextDepth);

            } else if (Map.class.isAssignableFrom(clazz)) {
                wrapperNewObjMap(field, (Map<Object, Object>) value, nextDepth);

            } else if (isStandardClass(value)) {
                field.set(result, xssCleanObject(nextDepth, maxCleanDepth, field, value));
            }
        }

        return result;
    }

    private Field[] findAllDeclaredFields(Class<?> resultClass) {
        Set<Field> fields = new HashSet<>();
        Class<?> currentClass = resultClass;
        do {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null && isStandardClass(currentClass));
        return fields.toArray(new Field[0]);
    }

    private void wrapperNewObjList(Field parentField, List<Object> valueList, int nextDepth) throws Exception {
        for (int i = 0; i < valueList.size(); i++) {
            try {
                valueList.set(i, xssCleanObject(nextDepth, DEFAULT_CLEAN_DEPTH, parentField, valueList));
            } catch (UnsupportedOperationException e) {
                log.error("value:{} class:{} is unModify!", valueList, valueList.getClass().getSimpleName());
                return;
            }
        }
    }

    private void wrapperNewObjMap(Field parentField, Map<Object, Object> objectMap, int nextDepth) throws Exception {
        for (Object key : objectMap.keySet()) {
            try {
                objectMap.put(key, xssCleanObject(nextDepth, DEFAULT_CLEAN_DEPTH, parentField, objectMap));
            } catch (UnsupportedOperationException e) {
                log.error("value:{} class:{} is unModify!", objectMap, objectMap.getClass().getSimpleName());
            }
        }
    }

    private void wrapperNewObjSet(Field parentField, Set<Object> objectSet, int nextDepth) throws Exception {
        List<Object> objectList = new LinkedList<>();
        for (Object obj : objectSet) {
            objectList.add(xssCleanObject(nextDepth, DEFAULT_CLEAN_DEPTH, parentField, obj));
        }
        try {
            objectSet.clear();
        } catch (UnsupportedOperationException e) {
            log.error("value:{} class:{} is unModify!", objectSet, objectSet.getClass().getSimpleName());
        }
    }

    boolean isStandardClass(Object result) {
        Class<?> clazz;
        if (result instanceof Class) {
            clazz = (Class<?>) result;
        } else {
            clazz = result.getClass();
        }
        return clazz.getName().startsWith(STANDARD_CLASS);
    }


    public static void main(String[] args) {
        Account account = new Account();
        account.setUserName("<script>alert(2132)</script>");
        XssHandler handler = new XssHandler();
        System.out.println(account);
        try {
            account = (Account) handler.xssCleanObject(account);
            System.out.println(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
