package com.devops.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "t_business_line")
@Entity
@Data
public class BusinessLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user")
    private String createUser;

    
}