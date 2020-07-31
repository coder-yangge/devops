#!/bin/bash

APP_NAME=${appName}
APP_TAR=${appTar}
APP_PARENT_PATH=${deployPath}

<#noparse>
    if [ ! -d ${APP_PARENT_PATH}${APP_NAME} ]; then
    mkdir -p ${APP_PARENT_PATH}${APP_NAME}
    fi

    mv ${APP_TAR} ${APP_PARENT_PATH}${APP_NAME}

    cd ${APP_PARENT_PATH}${APP_NAME}

    if [ -d /bin/ ]; then
    cd /bin/
    bash app_control.bash stop
    rm -rf ${APP_PARENT_PATH}${APP_NAME}
    fi

    cd ${APP_PARENT_PATH}${APP_NAME}
    tar xvf ${APP_TAR}
    cd /bin && bash app_control.bash start
</#noparse>

#END