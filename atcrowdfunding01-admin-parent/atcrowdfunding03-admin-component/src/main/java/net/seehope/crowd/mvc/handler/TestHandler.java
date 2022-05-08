package net.seehope.crowd.mvc.handler;

import lombok.extern.slf4j.Slf4j;
import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.entity.Student;
import net.seehope.crowd.entity.Subject;
import net.seehope.crowd.service.api.AdminService;
import net.seehope.crowd.util.CrowdUtil;
import net.seehope.crowd.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JoinYang
 * @date 2022/3/13 10:09
 */
@Controller
@Slf4j
public class TestHandler {

    @Autowired
    private AdminService adminService;

    //ajax异步请求
    @ResponseBody
    @RequestMapping("/test/ajax1.html")
    public String testAjax1() throws InterruptedException {
        Thread.sleep(3000);
        return "send ajax request";
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request) {
        boolean jungeRequestType = CrowdUtil.isAjaxRequestType(request);
        log.info("当前请求是否为ajax请求:{}", jungeRequestType);
        System.out.println(10 / 0);//基于异常处理机制的
        List<Admin> adminList = adminService.getAllAdmin();
        modelMap.addAttribute("adminList", adminList);
//        String a =null;
//        System.out.println(a.length());
        return "target";
    }

    @ResponseBody
    @RequestMapping("/send/array/one.html")
    public String requestDataOne(@RequestParam("array[]") List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number" + number);
        }
        return "successOne";
    }

    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String requestDataTwo(@RequestBody List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number" + number);
        }
        return "successTwo";
    }

    @ResponseBody
    @RequestMapping("/send/array/three.json")
    public JSONResult requestDataThree(@RequestBody Student student, HttpServletRequest request) throws Exception {
        boolean jungeRequestType = CrowdUtil.isAjaxRequestType(request);
        log.info("当前请求是否为ajax请求:{}", jungeRequestType);
        log.info("student:{}", student);
        for (Subject subject : student.getSubjects()) {
            log.info("subject:{}", subject);
        }
        String a = null;
        System.out.println(a.length());
        return JSONResult.successNeedData(student);
    }

}
