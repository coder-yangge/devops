package com.devops.web.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PageController
 * @description: TODO
 * @date 2020/7/5 1:08
 */
@Api(tags = "页面管理")
@Controller
public class PageController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @GetMapping("/business/line/index")
    public String businessLine() {
        return "/devops/businessLine";
    }

    @GetMapping("/business/line/savePage")
    public String saveBusinessPage() {
        return "/devops/businessLineAdd";
    }

    @GetMapping("/business/line/editPage")
    public String editBusinessPage(@RequestParam("id") Integer businessLineId, ModelMap modelMap) {
        modelMap.put("id", businessLineId);
        return "/devops/businessLineEdit";
    }

    @GetMapping("/service/index")
    public String service() {
        return "/devops/service/service";
    }

    @GetMapping("/service/savePage")
    public String saveServicePage() {
        return "/devops/service/serviceAdd";
    }

    @GetMapping("/service/editPage")
    public String editServicePage(@RequestParam("id") Integer serviceId, ModelMap modelMap) {
        modelMap.put("id", serviceId);
        return "/devops/service/serviceEdit";
    }

    @GetMapping("/application/index")
    public String application() {
        return "/devops/application/application";
    }

    @GetMapping("/application/savePage")
    public String applicationSavePage() {
        return "/devops/application/applicationAdd";
    }

}
