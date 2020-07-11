package com.devops.common.acount;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangge
 * @version 1.0.0
 * @title: AccountContextHolder
 * @date 2020/4/16 11:12
 */
public class AccountContextHolder {

    private static final String ACCOUNT_KEY = "accountKey";

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void put(String key, Account account) {
        if (ObjectUtils.isNotEmpty(account)) {
            Map<String, Object> attribute = threadLocal.get();
            if (CollectionUtils.isEmpty(attribute)) {
                attribute = new HashMap<>();
                threadLocal.set(attribute);
            }
            attribute.put(key, account);
        }
    }

    public static Account getAccount() {
        return (Account) getAttribute(ACCOUNT_KEY);
    }

    public static Object getAttribute(String key) {
        Map<String, Object> attribute = threadLocal.get();
        return attribute.get(key);
    }

    public static void putAccount(Account account) {
        put(ACCOUNT_KEY, account);
    }
}
