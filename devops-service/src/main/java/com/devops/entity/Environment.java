package com.devops.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "t_environment")
public class Environment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 1:服务器;2:集群
     */
    @Column(name = "deploy_type", nullable = false)
    private Integer deployType;

    @Column(name = "deploy_id", nullable = false)
    private Integer deployId;

    @Column(name = "deploy_path", nullable = false)
    private String deployPath;

    @Column(name = "start_script")
    private String startScript;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user")
    private String createUser;

    
}