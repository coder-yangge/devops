package com.devops.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "t_repository")
public class Repository implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;

    /**
     * 1:git;2:svn;3:本地
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pass_word")
    private String passWord;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user")
    private String createUser;

    
}