package com.devops.service;

import com.devops.common.dto.PageDTO;
import com.devops.dto.ClusterDTO;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ClusterService
 * @date 2020/7/14 18:44
 */
public interface ClusterService {

    PageDTO<ClusterDTO> getPage(ClusterDTO clusterDTO, PageDTO pageDTO);

    List<ClusterDTO> getClusterList();
}
