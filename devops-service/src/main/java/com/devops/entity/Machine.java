package com.devops.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_machine")
public class Machine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "ssh_user_name", nullable = false)
    private String sshUserName;

    @Column(name = "ssh_password", nullable = false)
    private String sshPassword;

    
}