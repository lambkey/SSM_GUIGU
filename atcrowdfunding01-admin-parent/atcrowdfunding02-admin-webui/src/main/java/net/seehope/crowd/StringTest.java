package net.seehope.crowd;

import lombok.extern.slf4j.Slf4j;
import net.seehope.crowd.service.api.AdminService;
import net.seehope.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author JoinYang
 * @date 2022/3/19 14:11
 */
@Slf4j
//创建Spring的Junit测试类
@RunWith(SpringJUnit4ClassRunner.class)
//导入spring中的bean
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-tx.xml"})
public class StringTest {
    @Autowired
    private AdminService adminService;

    @Test
    public void md5(){
        String psw="123123";
        log.info("加密前{}",psw);
       String pswed= CrowdUtil.md5(psw);
       log.info("加密后{}",pswed);
    }

    @Test
    public void testByAcct(){
        System.out.println(adminService.getAdminByLogAcct("123", "12345"));
    }
}
