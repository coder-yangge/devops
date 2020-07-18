package com.devops.common.acount;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangge
 * @version 1.0.0
 * @title: Account
 * @date 2020/4/16 11:08
 */
@Data
public class Account implements Serializable {

    private String userName;

    private String passWord;

    private String userId;

    private String webSocketSessionId;
}
