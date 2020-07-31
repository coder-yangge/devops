#!/bin/bash

APP_NAME=${appName}
APP_TAR=${appTar}
APP_PARENT_PATH=${deployPath}
APP_TARGET_PATH=${deployPath}${appName}

<#noparse>
    if [ ! -e ${APP_PARENT_PATH}${APP_NAME} ]; then
    mkdir -p ${APP_PARENT_PATH}${APP_NAME}
    echo ${APP_PARENT_PATH}${APP_NAME}
    fi

    if [ -d bin/ ]; then
    cd bin/
    bash app_control.bash stop
    rm -rf ${APP_PARENT_PATH}${APP_NAME}/*
    fi

    mv ${APP_TAR} ${APP_TARGET_PATH}

    cd ${APP_PARENT_PATH}${APP_NAME}
    tar xvf ${APP_TAR}
    source /etc/profile
    cd bin && sh app_control.sh start
</#noparse>

#END