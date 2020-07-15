package com.devops.web.controller;

import com.devops.dto.ClusterDTO;
import com.devops.service.ClusterService;
import com.devops.web.common.vo.ResponseVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ClusterController
 * @date 2020/7/15 10:01
 */
@RestController
@RequestMapping("/cluster")
@Api(tags = "集群管理")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @GetMapping("/list")
    public ResponseVo<List<ClusterDTO>> getClusterList() {
        List<ClusterDTO> clusterList = clusterService.getClusterList();
        return ResponseVo.ResponseBuilder.buildSuccess(clusterList);
    }
}
