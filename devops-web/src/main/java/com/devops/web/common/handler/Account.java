package com.devops.web.common.handler;

import com.devops.web.common.annotation.XssFilter;
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

    @XssFilter
    private String userName;

    @XssFilter
    private String passWord;

    @XssFilter
    private String userId;
}
