package com.devops.handler;

import com.devops.dto.DeployDTO;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DeployHandler
 * @date 2020/7/30 19:40
 */
public interface DeployHandler {

    void deployApp(DeployDTO deployDTO);
}
