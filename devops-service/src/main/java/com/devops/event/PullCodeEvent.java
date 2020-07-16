package com.devops.event;

import com.devops.dto.ApplicationDto;
import com.devops.dto.RepositoryDto;
import org.springframework.context.ApplicationEvent;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PullCodeEvent
 * @date 2020/7/16 17:07
 */
public class PullCodeEvent extends ApplicationEvent {

    private ApplicationDto applicationDto;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     */
    public PullCodeEvent(ApplicationDto applicationDto) {
        super(applicationDto);
        this.applicationDto = applicationDto;
    }

    public ApplicationDto getApplicationDto() {
        return applicationDto;
    }

}
