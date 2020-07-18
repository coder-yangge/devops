package com.devops.event;

import com.devops.common.acount.Account;
import com.devops.dto.ApplicationDto;
import com.devops.dto.BuildDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author yangge
 * @version 1.0.0
 * @title: CompileEvent
 * @date 2020/7/18 16:12
 */
public class CompileEvent extends ApplicationEvent {

    private ApplicationDto applicationDto;

    private Account account;

    private BuildDTO buildDTO;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     */
    public CompileEvent(ApplicationDto applicationDto, Account account, BuildDTO buildDTO) {
        super(applicationDto);
        this.applicationDto = applicationDto;
        this.account = account;
        this.buildDTO = buildDTO;
    }

    public ApplicationDto getApplicationDto() {
        return applicationDto;
    }

    public Account getAccount() {
        return account;
    }

    public BuildDTO getBuildDTO() {
        return buildDTO;
    }
}
