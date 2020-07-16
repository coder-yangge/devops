package com.devops.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Table(name = "t_application")
@Data
@Entity
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`desc`")
    private String desc;

    @OneToOne
    @JoinColumn(name = "business_line_id")
    private BusinessLine businessLine;

    @OneToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "create_user")
    private String createUser;

    
}