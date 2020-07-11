package com.devops.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_package_record")
@Entity
@Data
public class PackageRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "branch")
    private String branch;

    @Column(name = "version", nullable = false)
    private String version;

    /**
     * 1:成功;2:失败
     */
    @Column(name = "status", nullable = false)
    private String status;

    
}