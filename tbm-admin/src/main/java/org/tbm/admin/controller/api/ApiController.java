package org.tbm.admin.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tbm.admin.Response;
import org.tbm.admin.access.SystemInfoMapper;
import org.tbm.common.bean.SystemInfo;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jason.Xia on 17/7/13.
 */
@Controller
@RequestMapping(value = "/api", produces = "text/html;charset=UTF-8")
public class ApiController {

    @Inject
    private SystemInfoMapper systemInfoMapper;

    @RequestMapping(value = "/system/sign-up", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request, String name, String group, int env) {
        systemInfoMapper.insert(new SystemInfo(System.currentTimeMillis(), name, group, env));
        return new Response().toString();
    }

    @RequestMapping(value = "/system/logout", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request, String id) {
        systemInfoMapper.remove(Long.valueOf(id));
        return new Response<>().toString();
    }


}
