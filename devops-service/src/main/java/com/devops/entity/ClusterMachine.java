package com.devops.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "t_cluster_machine")
public class ClusterMachine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "machine_id", nullable = false)
    private Integer machineId;

    @Column(name = "cluster_id", nullable = false)
    private Integer clusterId;

    
}