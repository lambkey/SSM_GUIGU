package net.seehope.crowd;


import lombok.extern.slf4j.Slf4j;
import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.mapper.AdminMapper;
import net.seehope.crowd.service.api.AdminService;
import net.seehope.crowd.service.api.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author JoinYang
 * @date 2022/3/6 21:30
 */
@Slf4j
//创建Spring的Junit测试类
@RunWith(SpringJUnit4ClassRunner.class)
//导入spring中的bean
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //测试数据源是否正常
    @Test
    public void testDataSource() throws SQLException {
       Connection connection= dataSource.getConnection();
        System.out.println(connection);
    }

    //测试简单的数据库接口操作
    @Test
    public void testMapper(){
        Admin admin =new Admin(null,"123456789","123456","小咩","2374785659.com",null);

        //打印日志
        log.info("受影响行数{}",adminMapper.insert(admin));
        System.out.println(adminMapper.insert(admin));
    }



}
