# SSM_GUIGU
# 项目介绍
1、后台管理员系统使用单一架构开发。
2、前台管理系统使用分布式架构开发

## 期望
帮助创业者发布创业项目，向大众募集启动资金的融资平台
## 项目的总体目标
![image.png](http://42.194.206.10:8090/upload/2022/03/image-c87195e353aa4d28b7498bde9cccabd1.png)
## 项目的总体结构
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e5ff77557dcc4750b2f12e4652caee21.png)

---

# 项目开始
## 1、创建工程
- 选择简单的maven工程
- 创建完成的总体父工程以pom的形式打包(atcrowdfunding01-admin-parent)
```java
    <modules>
        <module>atcrowdfunding02-admin-webui</module>
        <module>atcrowdfunding03-admin-component</module>
        <module>atcrowdfunding04-admin-entity</module>
    </modules>
    <packaging>pom</packaging>
```
- 在总体父工程的目录下创建新的module,（atcrowdfunding02-admin-webui），为子工程。
- 在atcrowdfunding02-admin-webui子工程下创建web.xml文件
![image.png](http://42.194.206.10:8090/upload/2022/03/image-1b72c206c7ad4bc589a47994cbbf2d10.png)
- 创建其他子工程atcrowdfunding03-admin-component,atcrowdfunding04-admin-entity,打包形式为jar,工程与工程的依赖,依照**项目总体结构**
```java
	//atcrowdfunding02-admin-webui工程的
        <dependency>
            <groupId>com.lamb.crowd</groupId>
            <artifactId>atcrowdfunding03-admin-component</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

	//atcrowdfunding03-admin-component工程的
	<dependency>
            <groupId>com.lamb.crowd</groupId>
            <artifactId>atcrowdfunding04-admin-entity</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

	//
```
- 创建与总体父工程并列的工程
![image.png](http://42.194.206.10:8090/upload/2022/03/image-6843c45f2c78424ca78030ff1bf99335.png)

---

## 2、创建数据库数据表（物理建模）
1、创建管理员数据表
```mysql
CREATE database project_crowd CHARACTER set utf8;

use project_crowd;

drop TABLE if EXISTS t_admin;

CREATE TABLE t_admin(
id 							int not null auto_increment,#主键
login_acct 			VARCHAR(255)not null,#登录账号
user_pswd				CHAR(32)not null,#登录密码
user_name       varchar(255) not null,#昵称
email           varchar(255) not null,#邮件地址
create_time     char(19),#创建时间
PRIMARY KEY(id)
);

```


---
## 3、基于Maven的Mybatis逆向工程
步骤
### 3.1、在atcrowdfunding06-common-reverse独立工程下面导入以下依赖
```java
 <!--依赖Mybatis核心包-->
    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.2.8</version>
        </dependency>
    </dependencies>


    <build>
        <!--构建过程中用到的插件-->
        <plugins>
            <!--具体的插件-->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.0</version>

                <!--插件的依赖-->
                <dependencies>
                    <!--逆向工程的核心依赖-->
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.3.0</version>
                    </dependency>
                    <!--数据库连接池-->
                    <dependency>
                        <groupId>com.alibaba</groupId>
                        <artifactId>druid</artifactId>
                        <version>1.2.8</version>
                    </dependency>
                    <!--MySQL驱动-->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.12</version>
                    </dependency>
                </dependencies>
            </plugin>

        </plugins>
    </build>
```
### 3.2、在resourse文件目录下配置逆向工程有关的xml文件
![image.png](http://42.194.206.10:8090/upload/2022/03/image-b603cc8924774761b93b74262b832138.png)
```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="atguiguTables" targetRuntime="MyBatis3">
        <commentGenerator>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>

        <!-- 数据库链接URL、用户名、密码 -->
        <jdbcConnection
                driverClass="com.mysql.cj.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/project_crowd?serverTimezone=UTC"
                userId="root"
                password="root">
        </jdbcConnection>

        <!--
        默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer
            true，把JDBC DECIMAL 和 NUMERIC 类型解析为java.math.BigDecimal
        -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!--
        生成model模型，对应的包路径，以及文件存放路径(targetProject)，targetProject可以指定具体的路径,如./src/main/java，
        也可以使用“MAVEN”来自动生成，这样生成的代码会在target/generatord-source目录下
        -->
        <!--<javaModelGenerator targetPackage="com.joey.mybaties.test.pojo" targetProject="MAVEN">-->
        <javaModelGenerator targetPackage="net.seehope.crowd.po" targetProject=".\src\main\java">
            <!--是否让schema作为包的后缀-->
            <property name="enableSubPackages" value="false"/>
            <!-- 从数据库返回的值被清理前后的空格  -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <!--对应的mapper.xml文件  -->
        <sqlMapGenerator targetPackage="net.seehope.crowd.mapper" targetProject=".\src\main\resources">
            <!--是否让schema作为包的后缀-->
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>

        <!-- 对应的Mapper接口类文件 -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="net.seehope.crowd.mapper" targetProject=".\src\main\java">
            <!--是否让schema作为包的后缀-->
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>


        <table tableName="t_admin" domainObjectName="Admin" />
        <!-- 数据库表名与需要的实体类对应映射的指定 -->
        <!--        <table tableName="t_type" domainObjectName="TypePO" />-->
        <!--        <table tableName="t_tag" domainObjectName="TagPO" />-->
        <!--        <table tableName="t_project" domainObjectName="ProjectPO" />-->
        <!--        <table tableName="t_project_item_pic" domainObjectName="ProjectItemPicPO" />-->
        <!--        <table tableName="t_member_launch_info" domainObjectName="MemberLaunchInfoPO" />-->
        <!--        <table tableName="t_return" domainObjectName="ReturnPO" />-->
        <!--        <table tableName="t_member_confirm_info" domainObjectName="MemberConfirmInfoPO" />-->
        <!--<table tableName="t_order" domainObjectName="OrderPO" />
        <table tableName="t_address" domainObjectName="AddressPO" />
        <table tableName="t_order_project" domainObjectName="OrderProjectPO" />-->
    </context>
</generatorConfiguration>
```
### 3.3、在idea的右侧点击Maven按钮，执行命令（mvc mybatis-generator:generate）
![image.png](http://42.194.206.10:8090/upload/2022/03/image-68cbef09d8094e6eb354670c46e80e37.png)
### 3.4、效果,生成以下文件
![image.png](http://42.194.206.10:8090/upload/2022/03/image-2d6902dba0b74b92bc04773735aaf986.png)


---
## 4、父工程的依赖管理
```java
<properties>
        <!--对spring的版本进行统一管理-->
        <fall.spring.version>4.3.20.RELEASE</fall.spring.version>

        <!--对springsecurity的版本进行统一管理-->
        <fall.spring.security.version>4.2.10.RELEASE</fall.spring.security.version>
    </properties>


    <dependencyManagement>
        <dependencies>

            <!-- Spring 依赖 -->
            <!-- https://mvnrepository.com/artifact/org.springframework/spring-orm -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-orm</artifactId>
                <version>${fall.spring.version}</version>
            </dependency>
            <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>${fall.spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-test</artifactId>
                <version>${fall.spring.version}</version>
            </dependency>
            <!-- Spring AOP -->
            <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjweaver</artifactId>
                <version>1.9.2</version>
            </dependency>
            <!-- https://mvnrepository.com/artifact/cglib/cglib -->
            <dependency>
                <groupId>cglib</groupId>
                <artifactId>cglib</artifactId>
                <version>2.2</version>
            </dependency>

            <!-- 数据库依赖 -->
            <!-- MySQL 驱动 -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.15</version>
            </dependency>
            <!-- 数据源 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <!-- <version>1.0.31</version>-->
                <version>1.1.17</version>
            </dependency>
            <!-- MyBatis -->
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis</artifactId>
                <version>3.2.8</version>
            </dependency>
            <!-- MyBatis 与 Spring 整合 -->
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis-spring</artifactId>
                <version>1.2.2</version>
            </dependency>
            <!-- MyBatis 分页插件 -->
            <dependency>
                <groupId>com.github.pagehelper</groupId>
                <artifactId>pagehelper</artifactId>
                <version>4.0.0</version>
            </dependency>

            <!-- 日志 -->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>1.7.7</version>
            </dependency>
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
                <version>1.2.3</version>
            </dependency>
            <!-- 其他日志框架的中间转换包 -->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>jcl-over-slf4j</artifactId>
                <version>1.7.25</version>
            </dependency>
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>jul-to-slf4j</artifactId>
                <version>1.7.25</version>
            </dependency>

            <!-- Spring 进行 JSON 数据转换依赖 -->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-core</artifactId>
                <version>2.11.0</version>
            </dependency>
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>2.11.0</version>
            </dependency>

            <!-- JSTL 标签库 -->
            <dependency>
                <groupId>jstl</groupId>
                <artifactId>jstl</artifactId>
                <version>1.2</version>
            </dependency>

            <!-- junit 测试 -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>

            <!-- 引入 Servlet 容器中相关依赖 -->
            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>servlet-api</artifactId>
                <version>2.5</version>
                <scope>provided</scope>
            </dependency>

            <!-- JSP 页面使用的依赖 -->
            <dependency>
                <groupId>javax.servlet.jsp</groupId>
                <artifactId>jsp-api</artifactId>
                <version>2.1.3-b06</version>
                <scope>provided</scope>
            </dependency>
            <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
            <dependency>
                <groupId>com.google.code.gson</groupId>
                <artifactId>gson</artifactId>
                <version>2.8.5</version>
            </dependency>


            <!-- SpringSecurity 依赖配置 -->

            <!-- SpringSecurity 对 Web 应用进行权限管理 -->
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-web</artifactId>
                <version>${fall.spring.security.version}</version>
            </dependency>
            <!-- SpringSecurity 配置 -->
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-config</artifactId>
                <version>${fall.spring.security.version}</version>
            </dependency>
            <!-- SpringSecurity 标签库 -->
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-taglibs</artifactId>
                <version>${fall.spring.security.version}</version>
            </dependency>


        </dependencies>
    </dependencyManagement>
```
---
## 5、spring整合mybatis
### 5.1、在子工程加入搭建环境所需要的依赖，选择component工程,因为，atcrowdfunding02-admin-webui依赖于atcrowdfunding03-admin-component，可以用到component的所有依赖。
![image.png](http://42.194.206.10:8090/upload/2022/03/image-84e5f5e7de794e63a2c3515841acb82c.png)
```java
	<!-- Spring 依赖 -->
	        <!-- https://mvnrepository.com/artifact/org.springframework/spring-orm -->
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-orm</artifactId>
	            <exclusions>
	                <exclusion>
	                    <artifactId>commons-logging</artifactId>
	                    <groupId>commons-logging</groupId>
	                </exclusion>
	            </exclusions>
	        </dependency>
	        <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-webmvc</artifactId>
	        </dependency>
	        <!-- Spring AOP -->
	        <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
	        <dependency>
	            <groupId>org.aspectj</groupId>
	            <artifactId>aspectjweaver</artifactId>
	        </dependency>
	        <!-- https://mvnrepository.com/artifact/cglib/cglib -->
	        <dependency>
	            <groupId>cglib</groupId>
	            <artifactId>cglib</artifactId>
	        </dependency>
	
	        <!-- 数据库依赖 -->
	        <!-- MySQL 驱动 -->
	        <dependency>
	            <groupId>mysql</groupId>
	            <artifactId>mysql-connector-java</artifactId>
	        </dependency>
	        <!-- 数据源 -->
	        <dependency>
	            <groupId>com.alibaba</groupId>
	            <artifactId>druid</artifactId>
	        </dependency>
	        <!-- MyBatis -->
	        <dependency>
	            <groupId>org.mybatis</groupId>
	            <artifactId>mybatis</artifactId>
	        </dependency>
	        <!-- MyBatis 与 Spring 整合 -->
	        <dependency>
	            <groupId>org.mybatis</groupId>
	            <artifactId>mybatis-spring</artifactId>
	        </dependency>
	        <!-- MyBatis 分页插件 -->
	        <dependency>
	            <groupId>com.github.pagehelper</groupId>
	            <artifactId>pagehelper</artifactId>
	        </dependency>
	        <!-- Spring 进行 JSON 数据转换依赖 -->
	        <dependency>
	            <groupId>com.fasterxml.jackson.core</groupId>
	            <artifactId>jackson-core</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>com.fasterxml.jackson.core</groupId>
	            <artifactId>jackson-databind</artifactId>
	        </dependency>
	
	        <!-- JSTL 标签库 -->
	        <dependency>
	            <groupId>jstl</groupId>
	            <artifactId>jstl</artifactId>
	        </dependency>
	        <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
	        <dependency>
	            <groupId>com.google.code.gson</groupId>
	            <artifactId>gson</artifactId>
	        </dependency>
	
	        <dependency>
	            <groupId>junit</groupId>
	            <artifactId>junit</artifactId>
	            <scope>test</scope>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-test</artifactId>
	        </dependency>
	
	        <!-- 日志 -->
	        <dependency>
	            <groupId>org.slf4j</groupId>
	            <artifactId>slf4j-api</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>ch.qos.logback</groupId>
	            <artifactId>logback-classic</artifactId>
	        </dependency>
	        <!-- 其他日志框架的中间转换包 -->
	        <dependency>
	            <groupId>org.slf4j</groupId>
	            <artifactId>jcl-over-slf4j</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>org.slf4j</groupId>
	            <artifactId>jul-to-slf4j</artifactId>
	        </dependency>
	
	        <dependency>
	            <groupId>javax.servlet</groupId>
	            <artifactId>servlet-api</artifactId>
	        </dependency>
	
	        <!-- SpringSecurity 对 Web 应用进行权限管理 -->
	        <dependency>
	            <groupId>org.springframework.security</groupId>
	            <artifactId>spring-security-web</artifactId>
	        </dependency>
	
	        <!-- SpringSecurity 配置 -->
	        <dependency>
	            <groupId>org.springframework.security</groupId>
	            <artifactId>spring-security-config</artifactId>
	        </dependency>
	
	        <!-- SpringSecurity 标签库 -->
	        <dependency>
	            <groupId>org.springframework.security</groupId>
	            <artifactId>spring-security-taglibs</artifactId>
	        </dependency>
```
### 5.2、开始整合
- - 增加一个原本mybatis的核心配置文件(名存实亡)
![image.png](http://42.194.206.10:8090/upload/2022/03/image-b41877eb89434638a42060d72cb435a1.png)
```java
	<?xmlversion="1.0"encoding="UTF-8"?>
	<!--MyBatis的DTD约束-->
	<!DOCTYPEconfigurationPUBLIC"-//mybatis.org//DTDConfig3.0//EN""http://mybatis.org/dtd/mybatis-3-config.dtd">
	<!--configuration核心根标签-->
	<!--mybatis全局配置文件-->
	<configuration>

        </configuration>
```
- - 增加数据库的配置文件和spring整合mybatis的spring-mybatis文件
![image.png](http://42.194.206.10:8090/upload/2022/03/image-9dd8f95c7bb44b3aaa4ce4112e94cb72.png)
```java
//数据库配置文件
dbc.user=root
jdbc.password=root
jdbc.url=jdbc:mysql://localhost:3306/project_crowd?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC
jdbc.driverClass=com.mysql.cj.jdbc.Driver

```
```java
//spring-mybatis文件
<?xmlversion="1.0"encoding="UTF-8"?>
<beansxmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:aop="http://www.springframework.org/schema/aop"
xmlns:tx="http://www.alibaba.com/schema/stat"
xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
xsi:schemaLocation="http://www.springframework.org/schema/beanshttp://www.springframework.org/schema/beans/spring-beans.xsdhttp://www.springframework.org/schema/contexthttps://www.springframework.org/schema/context/spring-context.xsdhttp://www.springframework.org/schema/aophttp://www.alibaba.com/schema/stat.xsdhttp://mybatis.org/schema/mybatis-springhttp://mybatis.org/schema/mybatis-spring.xsd">

<!--专门整合spring和mybatis-->
<context:property-placeholderlocation="classpath:jdbc.properties"/>

<!--配置数据源-->
<beanid="dataSource"class="com.alibaba.druid.pool.DruidDataSource">
<propertyname="driverClassName"value="${jdbc.driverClass}"/>
<propertyname="url"value="${jdbc.url}"/>
<propertyname="password"value="${jdbc.password}"/>
<propertyname="username"value="${jdbc.user}"/>
</bean>


</beans>
```
- - 创建测试类测试数据源类
![image.png](http://42.194.206.10:8090/upload/2022/03/image-8ecd912b53c94877a4f62603d07cfb20.png)
```java

//创建Spring的Junit测试类
@RunWith(SpringJUnit4ClassRunner.class)
//导入spring中的bean
@ContextConfiguration(locations=("classpath:spring-mybatis.xml"))
Public class CrowdTest{

@Autowired
Private DataSourcedataSource;

@Test
Public void testDataSource() throws SQLException{
	Connection connection=dataSource.getConnection();
	System.out.println(connection);
}
}
```
---
## 6、声明式事务
### 6.1、目标：在框架的环境下通过一系列的配置又spring来管理通用事务，然后我们写代码就能享受框架提供的服务
```java
Try{

//开启事务(关闭自动提交)
	Connection.setAutoCommit(false);

//核心操作
	admindService.addAdmin(admin);
	
//事务提交
	Connection.commit();

}catch(Ecxception e){

//事务回滚
	Connection.roolBack();

}finally{

//释放数据库连接
	Connection.close();
}
```
### 6.2、代码
#### 6.2.1、创建spring专门管理事务的配置文件
![image.png](http://42.194.206.10:8090/upload/2022/03/image-6f9e685492aa4ba7b0234c423819970c.png)
#### 6.2.2、配置自动扫描的包和事务管理器的事务属性
```java
		<?xmlversion="1.0"encoding="UTF-8"?>
		<beansxmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:tx="http://www.springframework.org/schema/tx"
		xmlns:context="http://www.springframework.org/schema/context"
		xmlns:aop="http://www.springframework.org/schema/aop"
		xsi:schemaLocation="http://www.springframework.org/schema/beanshttp://www.springframework.org/schema/beans/spring-beans.xsd">
		
		<!--配置自动扫描的包：主要是为了把Service扫描到IOC容器中-->
		<context:component-scanbase-package="net.seehope.crowd.service"></context:component-scan>
		<!--开启事务注解支持-->
		
		<tx:annotation-driven/>
		<!--配置事务管理事务管理-->
		<bean id="txManager"class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!--装配数据源-->
		<property name="dataSource"ref="dataSource"></property>
		</bean>
		
		<!--配置事务切面-->
		<aop:config>
		<!--<aop:pointcutid="txPointCut"expression="execution(publicStringnet.seehope.crowd.service.impl.AdminService.saveAdmin(Admin))"/>-->
		
		<!--考虑到后面整合springsecurity，避免把UserDetailService加入事务控制，让切入点表达式定位到ServiceImpl-->
		<aop:pointcut id="txPointCut"expression="execution(**..*ServiceImpl.*(..))"/>
		
		<!--将切入点表达式和事务通知关联起来-->
		<aop:advisor advice-ref="txAdvice"pointcut-ref="txPointCut"></aop:advisor>
		</aop:config>
		
		<!--配置事务的通知-->
		<tx:advice id="txAdvice"transaction-manager="txManager">
		
		<!--配置事务的属性-->
		<tx:attributes>
		
		<!--查询方法：配置只读属性，让数据库知道这是一个查询操作，能够进行一定的优化-->
		<tx:method name="get*"read-only="true"/>
		<tx:method name="find*"read-only="true"/>
		<tx:method name="query*"read-only="true"/>
		<tx:method name="count*"read-only="true"/>
		
		<!--增删改方法：配置事务传播行为、回滚事务-->
		<!--
		REQUIRES_NEW:不管线程上面有没有这个事务，我们都在自己开的事务里面运行
		
		rollback-for：配置事务方法针对什么样的异常回滚
		默认：运行时异常回滚
		建议：编译时和运行时异常都回滚
		-->
		<tx:method name="save*"propagation="REQUIRES_NEW"rollback-for="java.lang.Exception"/>
		<tx:method name="update*"propagation="REQUIRES_NEW"rollback-for="java.lang.Exception"/>
		<tx:method name="delete*"propagation="REQUIRES_NEW"rollback-for="java.lang.Exception"/>
		<tx:method name="batch*"propagation="REQUIRES_NEW"rollback-for="java.lang.Exception"/>
		
		
		</tx:attributes>
		
		</tx:advice>
		</beans>
```
---
## 7、web.xml表述层的配置mvc
### 7.1、web.xml与spring配置文件的关系
![image.png](http://42.194.206.10:8090/upload/2022/03/image-2761533d77ed4525bcd77f655851f958.png)
### 7.2、目标
#### 7.2.1、handler中装配
#### 7.2.2、页面能够访问到Service
#### 7.2.3、页面->handler(@RequestMapping)->Service->Mapper->数据库
### 7.3、代码：
- - web.xml配置
![image.png](http://42.194.206.10:8090/upload/2022/03/image-3fa142eb0be740ecb9f0f03e044580a4.png)
```java
	<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath:spring-*.xml</param-value>
	</context-param>
	<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	
	
	<filter>
	<filter-name>CharacterEncodingFilter</filter-name>
	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	<!--指定字符集-->
	<init-param>
	<param-name>encoding</param-name>
	<param-value>UTF-8</param-value>
	</init-param>
	<!--强制请求设置字符集-->
	<init-param>
	<param-name>forceRequestEncoding</param-name>
	<param-value>true</param-value>
	</init-param>
	<init-param>
	<param-name>forceResponseEncoding</param-name>
	<param-value>true</param-value>
	</init-param>
	</filter>

	<!--这个Filter执行的顺序要在其他Filter前面-->
	<filter-mapping>
	<filter-name>CharacterEncodingFilter</filter-name>
	<url-pattern>/*</url-pattern>
	</filter-mapping>
	


	<servlet>
	<servlet-name>dispatcherServlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath:spring-web-mvc.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
	<servlet-name>dispatcherServlet</servlet-name>
	<!--/表示拦截所有请求-->
	<!--*.html
	优点:.css.js.png等等静态资源完全不经过SpringMVC,不需要特殊的处理
	缺点:不符合RESFul风格
	-->
	<url-pattern>*.html</url-pattern>
	<url-pattern>*.json</url-pattern>
	</servlet-mapping>
```
- - 编写spring-web-mvc.xml文件配置
![image.png](http://42.194.206.10:8090/upload/2022/03/image-38a0163c4252447184ff7b04c25a7faf.png)
```java
	<!--开MVC注解支持-->
	<mvc:annotation-driven></mvc:annotation-driven>
	
	<!--开启注解扫描:扫描Handler-->
	<context:component-scan base-package="net.seehope.crowd.mvc"/>
	
	<!--配置试图-->
	<beanclass="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix"value="/WEB-INF/"></property>
	<property name="suffix"value=".jsp"></property>
</bean>
```
- - 编写测试
```java
	//5.1在webui工程下面添加依赖
	<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>servlet-api</artifactId>
	</dependency>
	
	<dependency>
	<groupId>javax.servlet.jsp</groupId>
	<artifactId>jsp-api</artifactId>
	</dependency>
	
	//5.2、在webapp底下编写一个.jsp类型的超链接
	
	<%@page contentType="text/html;charset=UTF-8"language="java"%>
	<!DOCTYPEhtml>
	<html>
	<head>
	<metacharset="UTF-8">
	<title>Title</title>
	</head>
	<body>
	<%--超链接--%>
	<a href="${pageContext.request.contextPath}/test/ssm.html">测试SSM整合环境</a>
	
	</body>
	</html>
	
	
	
	//5.3、在WEB-INF下面创建java转发目标
	<%@pagecontentType="text/html;charset=UTF-8"language="java"%>
	<!DOCTYPEhtml>
	<html>
	<head>
	<metacharset="UTF-8">
	<title>Title</title>
	</head>
	<body>
	<h1>Success</h1>
	<p>
	${requestScope.adminList}
	</p>
	</body>
	</html>
	
	
	//5.4、编写测试类
		@Controller
		public class TestHandler{
		@Autowired
		private AdminServicea dminService;
		
		@RequestMapping("/test/ssm.html")
		public String testSsm(ModelMapmodelMap){
		List<Admin>adminList=adminService.getAllAdmin();
		modelMap.addAttribute("adminList",adminList);
		return"target";
		}
            }
```
## 8、SpringMvc下面的ajax请求
### 8.1、普通请求与ajax请求
![image.png](http://42.194.206.10:8090/upload/2022/03/image-48b585fd9c5a4ac49c379c1a3ae87438.png)
### 8.2、ajax请求的方法
![image.png](http://42.194.206.10:8090/upload/2022/03/image-bd4d6cc42ab24feb88fad29fbc27ce44.png)
*@RequestBody和ResponseBody想正常工作,当前环境就必须存在以下依赖*

```java
<!--Spring进行JSON数据转换依赖-->
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-core</artifactId>
</dependency>
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
</dependency>
//同时配置了 mvc:annotation-driven
```
### 8.3、@RequestBody注解的使用(配合ajax)
8.3.1、第一种，缺点: handler方法中接收数据时需要在请求参数名字后面多写一组“[]”
![image.png](http://42.194.206.10:8090/upload/2022/03/image-1fdb6d70fbb54d28849244653093f642.png)
```java
	<%@pagecontentType="text/html;charset=UTF-8"language="java"%>
	<! DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8">
	<title>Title</title>
	<%--http://localhost:8080/atcrowdfunding02_admin_webui_war/test/ssm.html--%>
	<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
	<script type="text/javascript"src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
	<script>
$(function(){
	$("#btn1").click(function(){
		$.ajax({
			"url":"send/array.html",//请求目标资源的地址
			"type":"post",//请求方式
			"data":{
			"array":[5,8,12]
				},//要发送的请求数据参数
			"dataType":"text",//如何对待服务端返回的数据
			"success":function(response){//服务器端成功处理请求后调用的回调函数，response是响应体数据
				alert(response);
				console.log(response);
					},
			"error":function(response){//服务器端处理请求失败后调用的回调函数
				alert(response);
				}
			});
		});
	});
</script>
</head>
<body>
<%--超链接，使用绝对路径,test前面不写斜杠--%>
<a href="test/ssm.html">测试SSM整合环境</a>
<br/>
<button id="btn1">Send[5,8,15]</button>
</body>
</html>

```
handler处理类代码
```java
@ResponseBody
@RequestMapping("/send/array.html")
public String requestData(@RequestParam("array[]") List<Integer>array){
	for(Integer number:array){
		System.out.println("number"+number);
		}
	return"success";
}
```
- - 8.3.2、第二种，比较方便
```java
$("#btn2").click(function(){
		
	Var array=[5,8,12];
	console.log(array.length);
			
	Var requestBody=JSON.stringify(array);
	console.log(requestBody.length);
	$.ajax({
		"url":"send/array/two.html",//请求目标资源的地址
		"type":"post",//请求方式
		"data":requestBody,//要发送的请求数据参数
		"contentType":"application/json;charset=UTF-8",//设置发送请求内容的编码格式
		"dataType":"text",//如何对待服务端返回的数据
		"success":
			function(response){//服务器端成功处理请求后调用的回调函数，response是响应体数据
			alert(response);
			console.log(response);
			},
		"error":
			function(response){//服务器端处理请求失败后调用的回调函数
			alert(response);
			}
	});
});
```
hadler处理代码
```java
@ResponseBody
@RequestMapping("/send/array/two.html")
public String requestDataTwo(@RequestBody List<Integer> array){
	for(Integer number:array){
	System.out.println("number"+number);
		}
	return"successTwo";
}
```
- - 8.3.2、第三种,ajax传复杂对象
```java
$("#btn3").click(function(){
	Var stuInfo={"stuName":"杨卓颖","stuAge":18,"address":"草潭镇","subjects":[{"subName":"数学","subScore":98},{"subName":"语文","subScore":80}]}
		Var requestBody=JSON.stringify(stuInfo);//将JSON对象转换为JSON字符串
		$.ajax({
			"url":"send/array/three.html",//请求目标资源的地址
			"type":"post",//请求方式
			"data":requestBody,//要发送的请求数据参数
			"contentType":"application/json;charset=UTF-8",//设置发送请求内容的编码格式
			"dataType":"text",//如何对待服务端返回的数据
			"success":function(response){//服务器端成功处理请求后调用的回调函数，response是响应体数据
						alert(response);
						console.log(response);
						},
			"error":function(response){//服务器端处理请求失败后调用的回调函数
						alert(response);
						}
				});
});
```
handler处理类代码
```java
@ResponseBody
@RequestMapping("/send/array/three.html")
public String requestDataThree(@RequestBody Student student){
		
	log.info("student:{}",student);
	for(Subjectsubject:student.getSubjects()){
		log.info("subject:{}",subject);
	}
		
	return"successThree";
}
		
	Student:
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class Student{
				Private String stuName;
				Private Integer stuAge;
				Private String address;
				Private List<Subject> subjects;
			}
			
	Subject:
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class Subject{
				Private String subName;
				Private Integer subScore;
			}
```
- - 8.3.4、对ajax请求返回结果进行规范
```java
public class JSONResult{
				
	public static final String SUCCESS="SUCCESS";
	public static final String FAILURE="FAILURE";
				
	//用来封装当前请求的结果是成功还是失败
	private String result;
				
	//请求处理失败返回的错误信息
	private String message;
				
	//要返回的数据
	private Object data;
				
	//请求处理成功且不需要返回信息和数据时
public static JSON ResultsuccessWithoutData(){
	return new JSONResult(SUCCESS,null,null);
	}
				
	//请求处理成功且不需要返回信息和数据时
public static JSONResultsuccessNeedData(Object data){
	return new JSONResult(SUCCESS,null,data);
	}
				
	//请求处理失败时返回错误信息
public static JSONResultFailureNeedMessage(String message){
	return new JSONResult(SUCCESS,message,null);
	}
				
				
public JSONResult(){
	}
				
public JSONResult(String result,String message,Object data){
	this.result=result;
	this.message=message;
	this.data=data;
	}
				
public String getResult(){
	return result;
	}
				
public void setResult(String result){
	this.result=result;
	}
				
public String getMessage(){
	returnmessage;
	}
				
public void setMessage(String message){
	this.message=message;
	}
				
public Object getData(){
	returndata;
	}
				
public void setData(Object data){
	this.data=data;
	}
}

```
### 8.4、ajax的异步请求和同步请求
- - 8.4.1、ajax异步请求

思路:
![QQ截图20220412123010.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220412123010-515347a985fc40ba961e80f9a0814360.png)

代码:
后端代码：
```java
@Controller
@Slf4j
public class TestHandler {

    //ajax异步请求
    @ResponseBody
    @RequestMapping("/test/ajax1.html")
    public String testAjax1() throws InterruptedException {
        Thread.sleep(3000);
        return "send ajax request";
    }

}
```

前端代码：
```java
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script>
        $(function () {
           
            $("#btn5").click(function () {
                console.log("发送异步请求前");
                $.ajax(

                    {
                        "url":"test/ajax1.html",
                        "type":"post",
                        "dataType":"text",
                        "success":function (response) {
                            console.log(response);
                        }
                    }
                )
                console.log("发送异步请求后");
            })
        });

    </script>
</head>
<body>
    <button id="btn5">ajax异步</button>
</body>
```

运行结果:(因为后端函数延迟了5秒，ajax异步先执行了后面的步骤，相当于两个线程)
![QQ截图20220412124632.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220412124632-072efb14835542d59b74455bbcd6b8da.png)
如果想让ajax里面的函数先执行完后执行后面代码，可以设置setTimeOut()
```java
$("#btn5").click(function () {
                console.log("发送异步请求前");
                $.ajax(

                    {
                        "url":"test/ajax1.html",
                        "type":"post",
                        "dataType":"text",
                        "success":function (response) {
                            console.log(response);
                        }
                    }
                );
                setTimeout(
                    function (){
                        console.log("发送异步请求后");
                    }
                ,5000)
                
            });
```
- - 8.4.2、ajax发同步请求
思路:
![QQ截图20220412131908.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220412131908-73f19e6b2396436ca305621cd8fce754.png)

前端代码:(修改前端代码，增加ajax的async属性,是原本的默认以为请求变为同步请求，实际上就一条线程)
```java
$("#btn5").click(function () {
                console.log("发送异步请求前");
                $.ajax(

                    {
                        "url":"test/ajax1.html",
                        "type":"post",
                        "dataType":"text",
                        "async":false,
                        "success":function (response) {
                            console.log(response);
                        }
                    }
                );

                console.log("发送异步请求后");
            });
```

## 9、异常映射
### 9.1、使用异常映射机制将整个项目的异常和错误进行统一的管理
### 9.2、思路
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e306f28bd9b140bd8f9f8e56af2ba673.png)
### 9.3、springmvc提供了基于xml和基于注解的异常映射
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e2ea7a12c0e843338a3cba57cbeea7db.png)
### 9.4、基于xml的（系统访问出现异常,会跳到system-error.jsp页面）
![image.png](http://42.194.206.10:8090/upload/2022/03/image-8edc0f1af8ea42bd87e422109de9b27e.png)
```java
<!--配置基于xml的异常映射-->
<bean id="simpleMappingExceptionResolver"class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
<!--配置异常类型和具体视图页面的对应关系-->
<property name="exceptionMappings">
<props>
<!--key属性指定异常全类名-->
<prop key="java.lang.Exception">system-error</prop>
</props>
</property>
</bean>
```
### 9.5、基于注解
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e09b3754ed644636a4d763f55d3ebfd8.png)
- - 9.5.1、编写判断是否为ajax请求的工具类
```java
public class CrowdUtil{
	public static boolean isAjaxRequestType(HttpServletRequestrequest){
		//1、获取请求消息头
		String acceptHeader=request.getHeader("Accept");
		String xRequestHeader=request.getHeader("X-Requested-With");
		
		//2、判断
		return((acceptHeader!=null&&acceptHeader.contains("application/json"))||(xRequestHeader!=null&&xRequestHeader.equals("XMLHttpRequest")));
	}
}
```
- - 9.5.2、编写异常处理类
```java
//@ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice
public class CrowdExceptionResolver{
		
@Autowired
private ObjectMapper objectMapper;
		
//@ExceptionHandler将一个具体的异常类型和一个方法关联起来
@ExceptionHandler(value=Exception.class)
public ModelAndViewre solveNullPointerException(Exception exception,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
	//1、判断当前请求的类型
	boolean judgeRequestType=CrowdUtil.isAjaxRequestType(request);
		
	//2、如果是ajax请求
	if(judgeRequestType){
	//3、创建JSONResult对象
	JSONResult jsonResult=JSONResult.FailureNeedMessage(exception.getMessage());
	//4、将异常信息传回浏览器
	response.setContentType("application/json;charset=UTF-8");
	response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
	}
		
	//5、如果不是Ajax请求则创建ModelAndView对象
	ModelAndView modelAndView=newModelAndView();
		
	//6、将Exception存入模型
	modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
		
	//7、设置对应的试图
	modelAndView.setViewName("system-error");
		
	//8、放回modelandview对象
	return modelAndView;
	}
}

```
- - 9.5.3、创建常量类
```java
public class CrowdConstant{
	Public staticfinalStringATTR_NAME_EXCEPTION="exception";
}
```

- - - 

## 10、创建管理员登录页面
### 10.1、静态文件的引入
![image.png](http://42.194.206.10:8090/upload/2022/03/image-66d64de0e4264980a6b96074771f49f9.png)
### 10.2、创建后台管理员登录页面（后台首项）
![image.png](http://42.194.206.10:8090/upload/2022/03/image-a3ff087b429547ccaf7b62b30e57f0a3.png)
### 10.3、配置view-controller,直接把请求地址和视图名称去关联起来，就不必写handler方法了
![image.png](http://42.194.206.10:8090/upload/2022/03/image-205a98c947174de596d3cdc615a287a3.png)
```java
	<!--配置view-controller,直接把请求地址和视图名称去关联起来，就不必写handler方法了-->
	<!--
	@RequestMapping("admin/to/login/page.html")
	publicStringLoginPage(){
	return"admin-login"
	}
	-->
<mvc:view-controller path="/admin/to/login/page.html" view-name="admin-login"></mvc:view-controller>
```
### 10.4、使用layer弹层组件
- - 10.4.1、
![image.png](http://42.194.206.10:8090/upload/2022/03/image-79e438c0249240b897a50f14f9ca7b28.png)
- - 10.4.2、在页面上引入layer环境
```java
<script type="text/javascript" src="layer/layer.js"></script>
```
- - 10.4.3、编写layer小弹窗
```java
	$("#btn4").click(function(){
		layer.msg("layer的小弹窗");
	});
	$("#btn4").click(function(){
		layer.msg("layer的小弹窗");
	});

```
### 10.5、点击返回上一层
```java
$(function(){
	$("button").click(function(){
	//相当于浏览器的后退按钮
		window.history.back();
	})
})
<button style="width:150px;margin:50px auto 0px auto" class="btnbtn-lgbtn-successbtn-block">点我返回上一层</button>

```
### 10.6、**管理员登录**
- - 10.6.1、目标:识别操作系统的人的身份，控制他的行为。
- - 10.6.2、思路：
![image.png](http://42.194.206.10:8090/upload/2022/03/image-fa0763a4987840fda3840b6203b34b84.png)
- - 10.6.3、代码：
- - 1. 创建工具方法,执行MD5加密
```java
//md5明文加密
Public static String md5(Stringsource){
	//1、判断source是否有效
	if(source==null||source.length()==0){
	//2、如果不是有效字符串，就抛异常
	Throw new RuntimeException(CrowdConstant.MASSAGE_STRING_INVALIDATE);
	}
			
	//3、获取MessageDigest对象
	String algorithm="md5";
	try{
	MessageDigest messageDigest=MessageDigest.getInstance(algorithm);
			
	//4、获取明文字符对应的字节数组
	byte[]input=source.getBytes();
			
	//5、执行加密
	byte[]output=messageDigest.digest(input);
			
	//6、创建BigInteger对象
	int signum=1;
	BigInteger bigInteger=new BigInteger(signum,output);
			
	//7、按照16进制将bigInteger的值转换为字符串
	intradix=16;
	String encoding=bigInteger.toString(radix);
			
	return encoding;
	}catch(NoSuchAlgorithmException e){
		e.printStackTrace();
	}
	return null;
}
```
- - 2.创建登录失败异常
 ![image.png](http://42.194.206.10:8090/upload/2022/03/image-6afb6198fd194917be266bb116ee2189.png)
```java
public class LoginFailedException extends RuntimeException{
	Private static final long serialVersionUID=1L;
		
	Public LoginFailedException(){
		}
		
	Public LoginFailedException(String message){
				super(message);
		}
		
	Public LoginFailedException(String message,Throwable cause){
				super(message,cause);
		}
		
	Public LoginFailedException(Throwable cause){
				super(cause);
		}
		
	Public LoginFailedException(String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace){
				super(message,cause,enableSuppression,writableStackTrace);
		}
	}

```
- - 3. 创建登录失败处理异常
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e289ee9b0b1a4b1697e292515753c847.png)![image.png](http://42.194.206.10:8090/upload/2022/03/image-9637435e14834a54acf5be4417b0b450.png)
```java
@Autowired
privateObjectMapperobjectMapper;
	
//@ExceptionHandler将一个具体的异常类型和一个方法关联起来
//登录失败异常处理
@ExceptionHandler(value=LoginFailedException.class)
Public ModelAndView resolveLoginFailException(LoginFailedException exception,
		HttpServletRequest request,HttpServletResponse response)throws Exception{
	
		//1、判断当前请求的类型
		Boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);
	
		//2、如果是ajax请求
		If (judgeRequestType){
		//3、创建JSONResult对象
		JSONResultjsonResult=JSONResult.FailureNeedMessage(exception.getMessage());
		//4、将异常信息传回浏览器
		response.setContentType("application/json;charset=UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
		}
	
		//5、如果不是Ajax请求则创建ModelAndView对象
		ModelAndViewmodelAndView=newModelAndView();
	
		//6、将Exception存入模型
		modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
	
		//7、设置对应的试图
		modelAndView.setViewName("admin-login");
	
		//8、放回modelAndView对象
		Return modelAndView;
	}
//转发到的页面
//<p>${requestScope.exception.message}</p>
```
- - 4. 创建AdminHandler类
```java
@Controller
public class AdminHandler{
	
	@Autowired
	privateAdminServiceadminService;
	
	@RequestMapping("/admin/do/login.html")
	Public String doLogin(@RequestParam("loginAcct")StringloginAcct,
					@RequestParam("userPswd")StringuserPswd,
					HttpSessionsession,HttpServletRequestrequest
				){
	System.out.println(request.getRequestURI());
	
	//调用service方法进行校验检查
	//这个方法能够返回admin对象说明登录成功，如果账号密码不正确则会抛出异常
	Admin admin=adminService.getAdminByLogAcct(loginAcct,userPswd);
	
	//登录成功后返回的admin对象存入Session域
	session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN_SESSION,admin);
	
	return"admin-main";
	}
}
```
```java
//前端表单，账号密码
<form action="admin/do/login.html" method="post" class="form-signin" role="form">
<input type="text" name="loginAcct" class="form-control"id="inputSuccess4"placeholder="请输入登录账号"autofocus>
<input type="text" name="userPswd" class="form-control" id="inputSuccess4" placeholder="请输入登录密码" style="margin-top:10px;">
```
- - 5. 创建AdminMapper操作数据库类(用账号查询用户)
![QQ截图20220402185152.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220402185152-583fa95142c746bbb665c3a4288987c7.png)
```java
@Select("SELECT * FROM t_admin where login_acct = #{login_acct}")
Admin getAdminByLogAcct(@Param("login_acct")String login_acct);
``` 
- - 6. 创建service业务处理层(验证用户的账号密码是否正确)
```java
 @Override
    public Admin getAdminByLogAcct(String loginAcct, String userPswd) {

        // 1、根据账号查询Admin对象
        Admin admin =adminMapper.getAdminByLogAcct(loginAcct);
        // 2、判断Admin对象是否为null

        if (admin==null){
            // 3、如果为空则抛出异常
            throw new LoginFailedException(CrowdConstant.MASSAGE_LOGIN_FAILED);
        }
        // 4、如果Admin对象不为null则将数据密码从Admin对象中取出
        String userPswd_DB =   admin.getUser_Pswd();

        // 6、对密码进行比较
        if (!passwordEncoder.matches(userPswd,userPswd_DB)){
            // 7、如果比较不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MASSAGE_LOGIN_FAILED);
        }
        // 8、如果一致则返回admin对象
        return admin;
    }
```
- - 7. 为了避免跳转到后台主页面再刷新浏览器导致重复提交登录表单,重定向到目标页面。所以handler的方法需要做相应的修改
```java
@Controller
public class AdminHandler{
		
	@Autowired
	private AdminServiceadminService;
		
	@RequestMapping("/admin/do/login.html")
	public String doLogin(@RequestParam("loginAcct")String loginAcct,
		@RequestParam("userPswd")String userPswd,
		HttpSession session,HttpServletRequest request
	){
		System.out.println(request.getRequestURI());
		
		//调用service方法进行校验检查
		//这个方法能够返回admin对象说明登录成功，如果账号密码不正确则会抛出异常
		Admin admin=adminService.getAdminByLogAcct(loginAcct,userPswd);
		
		//登录成功后返回的admin对象存入Session域
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN_SESSION,admin);
		
		//考虑到浏览器刷新，重定向，避免跳转到后台主页面再刷新浏览器导致重复提交登录表单
		return"redirect:/admin/to/main/page.html";
	}
}
``` 
![image.png](http://42.194.206.10:8090/upload/2022/03/image-dbee49b939ea47bb89a6bebea4315b17.png)
```java
//springmvc负责转发
<mvc:view-controller path="/admin/to/main/page.html" view-name="admin-main"/>
```
### 10.7、管理员用户维护-退出登录
- - 10.7.1、前端页面
![image.png](http://42.194.206.10:8090/upload/2022/03/image-527394828e854a9baefc2fcf8fc48474.png)

```java
<li><a href="admin/do/lagout.html"><i class="glyphiconglyphicon-off"></i>退出系统</a></li>
```
- - 10.7.2、后端系统
```java
//退出系统
@RequestMapping("admin/do/lagout.html")
Public String logOut(HttpSessionsession){
	
	//强制session失效
	session.invalidate();
	
	return"redirect:/admin/to/login/page.html";
}
```
- - 1. 重定向到登录页面
```java
<mvc:view-controller path="/admin/to/main/page.html" view-name="admin-main"></mvc:view-controller>
```
### 10.8、管理员用户维护-分页
- - 10.8.1、任务
1）分页显示Admin数据
不带关键词分页
带关键词分页
2）新增Admin
3）更新Admin
4）单条删除Admin
- - 10.8.2、分页
1）目标
将数据库中的Admin数据在页面上以分页形式显示。在后端将“带关键词”和“不带关键词”的分页合并为一套代码
2）思路
![image.png](http://42.194.206.10:8090/upload/2022/03/image-2e04f70ddfab41878265c3f8ddc57a07.png)![image.png](http://42.194.206.10:8090/upload/2022/03/image-75fd00939c1445a5a43f22ee83acb487.png)
- - 10.8.3、代码
- - 1. 引入PageHelper,加入依赖
```java
<!--MyBatis分页插件-->
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
</dependency>
```
- - 2. 在SqlSessionFactoryBean中配置分页插件 
![image.png](http://42.194.206.10:8090/upload/2022/03/image-6f8c60a025bb4ccb804730ab951da373.png)
```java
<!--配置插件-->
<property name="plugins">
		<array>
		<!--配置PageHelper插件-->
		<bean class="com.github.pagehelper.PageHelper">
		<property name="properties">
		<props>
		<!--配置数据库方言,告诉PageHelper当前使用的数据库-->
		<prop key="dialect">mysql</prop>
		
		<!--配置页码的合理化修正,在1~总页数之间修正页码-->
		<prop key="reasonable">true</prop>
		</props>
		</property>
		
		</bean>
		</array>
</property>
```
- - 3. 在AdminMapper.xml中编写mysql语句
![image.png](http://42.194.206.10:8090/upload/2022/03/image-9c8c6fb7452a43b1a6f90f6991fedbe7.png)
```java
<!--查找符合关键字匹配的数据（没有关键字则默认查找全部数据）-->
<select id="selectAdminByKeyword"resultMap="BaseResultMap">
	select
	<includerefid="Base_Column_List"/>
	fromt_admin
		Where 
			login_acct like CONCAT("%",#{keyword},"%")or
			user_name like CONCAT("%",#{keyword},"%")or
			Email like CONCAT("%",#{keyword},"%")
</select>
``` 
- - 4. 编写AdminMapper
![image.png](http://42.194.206.10:8090/upload/2022/03/image-1416d3b287ea4242abbcffebf4b2c90e.png)
```java
List<Admin> selectAdminByKeyword(String keyword);
```
- - 5. 编写AdminService接口
```java
PageInfo<Admin> getPageInfo(String keyWord,Integer pageNum,Integer pageSize);
```
- - 6. 编写AdminServiceImpl实现类
```java
@Override
public PageInfo<Admin>getPageInfo(String keyWord,Integer pageNum,Integer pageSize){
	//1、调用PageHelper的静态方法开启分页功能
	//这里充分体现了PageHandler的“非侵入式”设计;原本要做的查询不必有任何修改
	PageHelper.startPage(pageNum,pageSize);

	//2、执行查询
	List<Admin>adminList=adminMapper.selectAdminByKeyword(keyWord);
	
	//3、封装到PageInfo对象中
	return new PageInfo<>(adminList);
}

```
- - 7. 编写AdminHandler层
```java
//用户维护功能
@RequestMapping("/admin/get/page.html")
public String getPageInfo(
		@RequestParam(value="keyword",defaultValue="")Stringkeyword,
	
		//pageNum默认使用1，去到第一页
		@RequestParam(value="pageNum",defaultValue="1")IntegerpageNum,
	
		//pageSize,每一页显示五条数据
		@RequestParam(value="pageSize",defaultValue="5")IntegerpageSize,
		ModelMapmodelMap
	
	){
		//调用Service方法获取PageInfo对象
		PageInfo<Admin>pageInfo=adminService.getPageInfo(keyword,pageNum,pageSize);
	
		//将pageInfo对象传到管理员进行用户维护的页面，存入模型
		modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
	return"admin-page";
}
```
- - 8. 编写前端业务层
![image.png](http://42.194.206.10:8090/upload/2022/03/image-dff175c43ecf4ec2ae45870ef01a2dbd.png)
```java
	<table class="tabletable-bordered">
	<thead>
	<tr>
	<th width="30">#</th>
	<th width="30"><inputtype="checkbox"></th>
	<th>账号</th>
	<th>名称</th>
	<th>邮箱地址</th>
	<thwidth="100">操作</th>
	</tr>
	</thead>
	<tbody>
	<c:if test="${emptyrequestScope.pageInfo.list}">
	<tr>
	<td colspan="6"align="center">抱歉!没有查询到您要的数据</td>
	</tr>
	</c:if>
	<c:if test="${!emptyrequestScope.pageInfo.list}">
	<c:forEach items="${requestScope.pageInfo.list}"var="admin" varStatus="myStatus">
	<tr>
	<%--循环的计数--%>
	<td>${myStatus.count}</td>
	<td><inputtype="checkbox"></td>
	<td>${admin.login_Acct}</td>
	<td>${admin.user_Name}</td>
	<td>${admin.email}</td>
	<td>
	<button type="button"class="btnbtn-successbtn-xs"><I class="glyphiconglyphicon-check"></i></button>
	<button type="button"class="btnbtn-primarybtn-xs"><I class="glyphiconglyphicon-pencil"></i></button>
	<button type="button"class="btnbtn-dangerbtn-xs"><I class="glyphiconglyphicon-remove"></i></button>
	</td>
	</tr>
	</c:forEach>
	</c:if>
	</tbody>
	<tfoot>
	<tr>
	<td colspan="6"align="center">
	<ul class="pagination">
	<li class="disabled"><ahref="#">上一页</a></li>
	<li class="active"><ahref="#">1<span class="sr-only">(current)</span></a></li>
	<li><ahref="#">2</a></li>
	<li><ahref="#">3</a></li>
	<li><ahref="#">4</a></li>
	<li><ahref="#">5</a></li>
	<li><ahref="#">下一页</a></li>
	</ul>
	</td>
	</tr>
	
	</tfoot>
	</table>

```
### 10.9、管理员用户维护-分页导航
- - 1. 在页面上使用Pagenation实现页码导航条
1）加入pagenation的环境(导入jquery.pagination文件)
2）在有需要的页面引入,注意先后顺序，jquery.pagination.js文件要在jquery的后面
3)jquery.pagination.js文件需要整改的地方
![QQ截图20220329165754.png](http://42.194.206.10:8090/upload/2022/03/QQ%E6%88%AA%E5%9B%BE20220329165754-9fcb26e83b7b4273baa2410ad1db14af.png)

```java
<%--引入分页导航插件pagination和pagination插件的css--%>
<linkrel="stylesheet"href="../css/pagination.css">
<script type="text/javascript"src="../jquery/jquery.pagination.js"></script>


	//使用Pagination要求idv标签替换原有的页码部分
	// 旧代码
	<tfoot>
	<tr>
	<td colspan="6" align="center">
	<ul class="pagination">
	<li class="disabled"><ahref="#">上一页</a></li>
	<li class="active"><ahref="#">1<span class="sr-only">(current)</span></a></li>
	<li><a href="#">2</a></li>
	<li><a href="#">3</a></li>
	<li><a href="#">4</a></li>
	<li><a href="#">5</a></li>
	<li><a href="#">下一页</a></li>
	</ul>
	</td>
	</tr>
	
	</tfoot>
	
	//新代码
	<tfoot>
	<tr>
	<td colspan="6"align="center">
	<div id="Pagination" class="pagination"><!--这里显示分页 --></div>
	</td>
	</tr>
</tfoot>
```
- - 2. 分页导航文件(完整版)
![QQ截图20220401143324.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401143324-37f8cf3552064a6da1e172ec68095dbb.png)
```java

<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<%--引入分页导航插件pagination和pagination插件的css--%>
<link rel="stylesheet" href="css/pagination.css">
<%--引入分页导航插件pagination的js--%>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script>
    $(function () {
        // 调用后面声明的行数对页码导航条进行初始化操作
        initPagination();
    });

    function initPagination() {
        // 获取服务器查询的admin的总记录数
        var totalRecord = ${requestScope.pageInfo.total};

        // 声明一个JSON对象存储Pagination要设置的属性
        var properties = {
            num_edge_entries: 3,                                // 边缘页数
            num_display_entries: 5,                             // 主体页，(夹在边缘页的中间)
            callback: pageSelectCallback,                     // 用户点击“1,2,3”这样的页码时调用这个行数对这个函数实现跳转
            items_per_page:${requestScope.pageInfo.pageSize},   // 当前一页显示的页面数量
            current_page:${requestScope.pageInfo.pageNum-1},    // 当前显示的页面
            prev_text:"上一页",
            next_text:"下一页"
        };
        //生成页码导航条
        $("#Pagination").pagination(totalRecord,properties);
    }
    //用户点击“1,2,3”这样的页码时调用这个行数对这个函数实现跳转
    function pageSelectCallback(pageIndex,jQuery){
        // 根据pageIndex计算得到pageNum
        var pageNum =pageIndex + 1;

        // 跳转页面
        window.location.href = "admin/get/page.html?pageNum=" +pageNum;

        // 由于每一个页码都是超链接,所以在这个函数最好取消超链接的默认行为
        return false;
    }
</script>
<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='add.html'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${empty requestScope.pageInfo.list}">
                                <tr>
                                    <td colspan="6" align="center">抱歉!没有查询到您要的数据</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty requestScope.pageInfo.list}">
                                <c:forEach items="${requestScope.pageInfo.list}" var="admin" varStatus="myStatus">
                                    <tr>
                                        <%--循环的计数--%>
                                        <td>${myStatus.count}</td>
                                        <td><input type="checkbox"></td>
                                        <td>${admin.login_Acct}</td>
                                        <td>${admin.user_Name}</td>
                                        <td>${admin.email}</td>
                                        <td>
                                            <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                            <button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
                                            <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>


                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6"align="center">
                                    <div id="Pagination" class="pagination"><!--这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</body>
</html>

```
### 10.10、管理员用户维护-关键词查询(单点删除保持页面删除)
- 页面上调整查询表单
![QQ截图20220329213215.png](http://42.194.206.10:8090/upload/2022/03/QQ%E6%88%AA%E5%9B%BE20220329213215-01c6d3fadd3e473cb3753d275dc2201e.png)
![QQ截图20220329213258.png](http://42.194.206.10:8090/upload/2022/03/QQ%E6%88%AA%E5%9B%BE20220329213258-9a6b8c56a8644e3c957076bbfffba8ff.png)
- 关键词查询
```java

    //用户维护功能,关键词查询/用户页面显示
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,

            //pageNum默认使用1，去到第一页
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,

            //pageSize,每一页显示五条数据
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ) {
        //调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        //将pageInfo对象传到管理员进行用户维护的页面，存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }
```
- 调整表单,使关键字搜索的同时删除页面和轮翻页面的时候关键字搜索也会生效
![QQ截图20220329215052.png](http://42.194.206.10:8090/upload/2022/03/QQ%E6%88%AA%E5%9B%BE20220329215052-4498cb8ba36f4f8b81a587ba628a762a.png)
![QQ截图20220329214833.png](http://42.194.206.10:8090/upload/2022/03/QQ%E6%88%AA%E5%9B%BE20220329214833-e6422e8c068e497fbf0522d547f7b631.png)
**param可以获取当前请求的参数**
此请求+关键词请求=一个请求
后端代码：
```java
 //管理员用户单点删除
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public  String removeAdmin(@PathVariable ("adminId") Integer adminId,
                               @PathVariable("pageNum")Integer pageNum,
                               @PathVariable("keyword") String keyword
    ){
        adminService.deleteByPrimaryKey(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
```
### 10.11、管理员用户维护-添加用户
- 页面调整，添加一个添加成功后跳转的页面
![QQ截图20220401143532.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401143532-5a73d5e001ba470587cf5581126631cf.png)
```jsp
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/18
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("button").click(function (){
                //相当于浏览器的后退按钮
                window.history.back();
            });
        });
    </script>
    <style>
    </style>
</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">


    <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in" style="text-align: center;"></i> 尚筹网系统消息</h2>


    <h3 style="text-align: center;">${requestScope.SuccessTip}</h3>
    <%--
        requestScope对应的是存放request域数据的Map
        requestScope.exception相当于request.getAttribute("exception")
        requestScope.exception相当于exception.getMassage()
    --%>
    <button style="width: 150px;margin: 50px auto 0px auto"  class="btn btn-lg btn-success btn-block">点我返回上一层</button>
</div>

</body>
</html>

```
- 在管理用户主页面修改新增按钮，将按钮改为超链接a标签
![QQ截图20220401143938.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401143938-1df029d845ba4d868299a55190d8d237.png)

![QQ截图20220401144010.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401144010-391bf333f08c474d9a2a763803e48192.png)
- 点击新增向后端发请求跳转页面
- 后端使用视图<<mvc:biew-controller/>>标签进行相应的跳转
![QQ截图20220401192827.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401192827-4027ac749248410d92b3928fbf6ccf9a.png)
- 后端mvc代码
```java
<mvc:view-controller path="/admin/to/add.html" view-name="admin-add"></mvc:view-controller>
```
- 进入添加用户表单（前端填表单），修改一下两个超链接
![QQ截图20220401201706.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401201706-5910fa050b3948eea034be7bbc0d8f14.png)
![QQ截图20220401201731.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401201731-ff7c2bc370ca453da34961578ec49852.png)
![QQ截图20220401201719.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401201719-e8d0bc4d3b74420cb5a676ce9c4beb30.png)
- 跳转的目标视图（添加用户表单）（前端填表单）
![QQ截图20220401193031.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401193031-bc9e0781e936402c9c4b319b73efe44f.png)
- 修改目标视图的表单
![QQ截图20220401194646.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401194646-85fa94b2c5fc48eab0523c14303c4b71.png)
- 目标视图代码（前端填表单代码）
```java
 <form   action="admin/to/addAdmin.html" method="post" role="form">
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆账号</label>
                            <input  name="login_Acct" type="text" class="form-control" id="loginAcct" placeholder="请输入账号长度为5-12个数字">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆密码</label>
                            <input name="user_Pswd" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入登陆密码">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">用户名称</label>
                            <input name="user_Name" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入用户名称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">邮箱地址</label>
                            <input name="email" type="email" class="form-control" id="exampleInputEmail1" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button id="addSuccess" type="submit" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                        <button type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
                    </form>
```
- 表单提交，后端处理流程（前端->controller->service->mapper）
- - 表单提交过来（把提交过来的表单封装成一个对应的管理用户对象）,为了安全起见，将前端传进来的json对象封装再转换为真实对象存储，根据传进来的用户账号进行判断，若果已存在此账号,则抛出异常，如果该用户账号不存在则取出其密码进行加密，最终存入数据库，跳转页面告知用户添加成功。
![QQ截图20220401200131.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401200131-f24564a0bf704bdcbbca3e0f529101ff.png)![QQ截图20220401200524.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401200524-fa8eee20809349d2b0c6924bad0508af.png)
- - Controller代码
```java
    @RequestMapping(value = "/admin/to/addAdmin.html",method = RequestMethod.POST)
    public String addAdmin( @Valid AdminVo adminVo,ModelMap modelMap){

        boolean state=false;

        Admin admin =new Admin();

        // 为了安全起见，将前端传进来的json对象封装再转换为真实对象存储
        BeanUtils.copyProperties(adminVo,admin);

        state=adminService.saveAdmin(admin);

	//携带信息跳转到成功页面
        if (state){
            modelMap.addAttribute("SuccessTip","添加用户成功");
        }

        return "system-success";
    }
```
- - Service代码，更改一下原来的加密方式
- - 1. 在spring容器中加入BCryptPasswordEncoder的bean
![QQ截图20220402182343.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220402182343-2d36baa4b39e4470b09560ba773fa2cf.png)
- - 2. java代码
```java
 <bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"></bean>
```


```java
        //数据加密BCryptPasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean saveAdmin(Admin admin) {

        if (adminMapper.getAdminByLogAcct(admin.getLogin_Acct())!=null){
            //抛出的异常将通过CrowdExceptionResolver类进行异常的显示处理
            throw new LoginAcctExist(CrowdConstant.ADMIN_ALREADY_IN);
        }
        //传进来的密码加密处理
        String bpecCpswd= passwordEncoder.encode(admin.getUser_Pswd());
        admin.setUser_Pswd(md5Pswd);
        adminMapper.insert(admin);
        return true;
    }
```
- CrowdExceptionResolver异常处理类
- ![QQ截图20220402185447.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220402185447-082596aa9b654814a9cf7ab8b1573a4e.png)
(//@ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice)
```java
 @ExceptionHandler(value = LoginAcctExist.class)
    public ModelAndView resolveAcctExist(LoginAcctExist exception, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //1、判断当前请求的类型
        boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);

        //2、如果是ajax请求
        if (judgeRequestType) {
            //3、创建JSONResult对象
            JSONResult jsonResult = JSONResult.FailureNeedMessage(exception.getMessage());
            //4、将异常信息传回浏览器
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        }

        //5、如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //6、将Exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //7、设置对应的试图
        modelAndView.setViewName("admin-add");

        //8、返回modelandview对象
        return modelAndView;
    }
```
### 10.12、管理员用户维护-更新用户
- 目标
修改现有Admin的数据。不修改密码，不修改创建时间

- 思路
![QQ截图20220408121821.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408121821-8f935e280e97490d8132824fd7195a99.png)
- 代码
- -  1. 前端点击编辑按钮，把当前用户id传给后端服务器
![QQ截图20220408122307.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408122307-d51e771a84a640cf96950efd5661ab9c.png)
![QQ截图20220408122345.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408122345-a7e297d39e11451abbc667603f867fe3.png)
```java
 <a href="admin/update/${admin.id}.html" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>
```
- - 2.  后端拿到前端的id数据查询到该用户并返回给前端编辑页面
![QQ截图20220408123835.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408123835-e63703b099fe4cac98d542e21795d3f7.png)
```java
// 更新用户，根据用户id显示用户信息
    @RequestMapping(value = "/admin/update/{adminId}.html")
    public String updateAdmin(@PathVariable("adminId") Integer adminId,ModelMap modelMap){

        Admin admin = adminService.selectByPrimaryKey(adminId);

        modelMap.addAttribute("Admin",admin);

        return "admin-update";
    }
```

![QQ截图20220408124250.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408124250-441a6a22597041289eaf5433d5b720d1.png)
```java
@Override
    public Admin selectByPrimaryKey(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin;
    }
```
![QQ截图20220408124835.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408124835-5cbad880923241c5839b41aeef983eb9.png)
![QQ截图20220408125601.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408125601-5036077bb59f4cec8a78409139652732.png)

```java
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>

<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%--
          Created by IntelliJ IDEA.
          User: lamp
          Date: 2022/3/19
          Time: 15:18
          To change this template use File | Settings | File Templates.
        --%>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <!DOCTYPE html>
        <html lang="zh-CN">
        <%@include file="include-head.jsp" %>

        <body>
        <%@include file="include-nav.jsp" %>

        <div class="container-fluid">
            <div class="row">

                <%@include file="include-siderbar.jsp" %>

                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <ol class="breadcrumb">
                        <li><a href="admin/to/main/page.html">首页</a></li>
                        <li><a href="admin/get/page.html">数据列表</a></li>
                        <li class="active">新增</li>
                    </ol>
                    <div class="panel panel-default">
                        <div class="panel-heading">表单数据 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                        <div class="panel-body">

                            <form   action="admin/to/updateAdmin.html" method="post" role="form">
                                <input name="id" type="hidden" value="${requestScope.Admin.id}"/>
                                <p>${requestScope.exception.message}</p>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">登陆账号</label>
                                    <input  name="login_Acct" type="text" class="form-control" id="loginAcct" placeholder="请输入账号长度为5-12个数字" value="${requestScope.Admin.login_Acct}">
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1">用户名称</label>
                                    <input name="user_Name" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入用户名称" value="${requestScope.Admin.user_Name}">
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputEmail1">邮箱地址</label>
                                    <input name="email" type="email" class="form-control" id="exampleInputEmail1" placeholder="请输入邮箱地址" value="${requestScope.Admin.email}">
                                    <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                                </div>
                                <%--提交表单--%>
                                <button id="updateSuccess" type="submit" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 修改</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title" id="myModalLabel">帮助</h4>
                    </div>
                    <div class="modal-body">
                        <div class="bs-callout bs-callout-info">
                            <h4>测试标题1</h4>
                            <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                        </div>
                        <div class="bs-callout bs-callout-info">
                            <h4>测试标题2</h4>
                            <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script >
            //用js提示用户，规范用户账号
            function LoginAcct(){
                /* 获取用户名值，一般用id=“username” */
                var loginAcct=$("#loginAcct").val();
                /* 使用正则表达式规范用户名长度 6-24个字母和数字组合*/
                var reg_username=/^[1-9][0-9]{4,10}$/;
                /* 判断用户名是否符合要求的类型 */
                var flag=reg_username.test(loginAcct);
                if(flag){
                    //用户名合法
                    $("#loginAcct").css("border","");
                }else{
                    //用户名非法
                    $("#loginAcct").css("border","1px solid red");
                }
                return flag;
            }
            //当某一个组件失去焦点时，调用对应的校验方法
            $("#loginAcct").blur(LoginAcct);
        </script>
        </body>
        </html>


        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

        </div>
    </div>
</div>
</div>

</body>
</html>
```

- - - 
## 11、管理员角色维护（权限控制）
- 目标:管理用户行为，保护系统功能
- **建立关联关系：**
权限->资源:单向多对多（java类之间单向:从权限实体类可以获取到资源对象的集合，但是通过资源获取不了权限。数据库之间多对多）
角色->权限:单向多对多（java类之间单向:从角色实体类可以获取到权限对象的集合，但是通过权限获取不了角色。数据库之间多对多）
用户->角色:双向多对多（可以通过用户获取它具备的角色，也可以看一下角色下包含哪些用户）
- **多对多数据表表示**
- - 1. 没有中间表
![QQ截图20220408201952.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408201952-fe0a938fddf94eb19d42ba7bededf92f.png)
如果只能在一个外键列上存储关联关系数据，那么现在这种情况无法使用SQL语句进行关联查询
- - 2. 有中间表
![QQ截图20220408203358.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408203358-ef666fd6715d4ed5810ba4f6651ab69c.png)
```MySQL
select t_student.id,t_student.name from t_student left join t_inner on t_student.id=t_inner.student_id left join t_subject on t_inner.subject_id=t_subject.id where t_subject.id=1
```
### 11.1、RBAC权限模型
- 概念：鉴于权限控制的核心是**用户**通过**角色**与**权限**进行关联（基于角色的访问控制）
在RBAC模型中，一个用户可以对应多个角色,一个角色拥有多个权限，权限具体定义用户可以做哪些事情
### 11.2、角色维护-分页
   **逆向生成资源：略（参考admin的）**

**思路：**
	![QQ截图20220416180013.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416180013-3f0b38b0d746431a99b51785847b3fc8.png)

#### 后端:

1、编写关键字查询的sql
![QQ截图20220416161034.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416161034-7356a4763c7f40c8b47cd21b82a962fd.png)  

```java

<select id="selectRoleByKeyword" resultMap="BaseResultMap">
    select id,name from t_role
    where name like concat("%",#{keyword},"%")
  </select>

```
2、编写关键词搜索分页RoleService
```java
@Autowired
    private RoleMapper roleMapper;


    @Override
    public PageInfo<Role> getPageInfo(String keyWord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List <Role> list = roleMapper.selectRoleByKeyword(keyWord);
        return new  PageInfo<>(list);
    }
```
3、编写关键词搜索分页RoleHandler
```java
    @Autowired
    private RoleService roleService;

    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public JSONResult<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize

    ){
        // 调用Service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(keyword, pageNum, pageSize);

        // 封装到JSONResult对象中返回（如果上面操作抛出异常，交给异常映射机制处理）
        return JSONResult.successNeedData(pageInfo);
    }
```
4、mvc负责前端向目标页面跳转
![QQ截图20220416180817.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416180817-437253c298be449aa6c9a4d67adcd060.png)
```java
<mvc:view-controller path="/role/to/page.html" view-name="role-page"></mvc:view-controller>
```
#### 前端:
1、点击跳转role-page模块
![QQ截图20220416181158.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416181158-2d724a89b13441308f1f6d0db306329e.png)

![QQ截图20220416181009.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416181009-bb4ddf39a55f4e3d9ff06e1711a68e59.png)
```java
<li style="height:30px;">
     <a href="role/to/page.html"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
</li>
```
2、role-page页面自动加载role的数据
```java
<script>
    $(function () {

        // 1、为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
	// 调用生成页面数据
        generatePage();

        // 2、关键词查询，修改全的window.keyword即可
        $("#searchBtn").click(function () {
            window.pageNum = 1;
            window.keyword = $("#keywordInput").val();
            generatePage();
        });
    });

</script>
```
3、generatePage()调用的函数装配在单独的js文件
```java

// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage(){
    // 1、获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2、填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取pageInfoRemote数据
function getPageInfoRemote() {
   var ajaxResult = $.ajax({
        "url":"role/get/page/info.json",
        "type":"post",
        "data":{
            "keyword":window.keyword,
            "pageNum":window.pageNum,
            "pageSize":window.pageSize

        },
        // 同步请求
        "async":false,
        // 服务端响应返回的数据处理方式为json格式
        "dataType":"json"
    });
   // 如果当前响应状态码不是200，说明发生了错误或其他意外情况，显示提示消息。让当前函数停止执行
    if (ajaxResult.status != 200){
        layer.msg("Failure!FailureCode:"+ajaxResult.status);
        return null;
    }
    console.log(ajaxResult);
    // 判断后端处理逻辑是否正确
    var  JSONResult = ajaxResult.responseJSON;
    if (JSONResult.result =="FAILURE"){
        layer.msg(JSONResult.message);
        return null;
    }

    if (JSONResult.result =="SUCCESS"){
       var pageInfo = JSONResult.data
        return pageInfo;
    }

    
}

// 填充表格
function fillTableBody(pageInfo){

    $("#rolePageBody").empty();
    $("#Pagination").empty();
    // 判断pageInfo对象是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null ||pageInfo.list.length ==0){
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有查询到您搜索的数据！</td></tr>")
        return ;
    }
    // 使用pageInfo的list属性填充tbody
    for (var i=0;i<pageInfo.list.length;i++){
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";

        var checkBtn ="<button type='button' class='btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>";
        var addBtn ="<button class='btn btn-primary btn-xs'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn ="<button class='btn btn-danger btn-xs'><i class=' glyphicon glyphicon-remove'></i></button>";
        var btnTd ="<td>"+checkBtn+"&nbsp;"+addBtn+"&nbsp;"+removeBtn+"</td>";
        var tr ="<tr>"+numberTd+checkboxTd+roleNameTd+btnTd+"</tr>"
        $("#rolePageBody").append(tr);
    }

    // 生成分页导航条
    generateNavigator(pageInfo);
}

//生成分页页码导航条
function generateNavigator(pageInfo) {
    // 获取服务器查询的admin的总记录数
    var totalRecord = pageInfo.total;

    // 声明一个JSON对象存储Pagination要设置的属性
    var properties = {
        num_edge_entries: 3,                                // 边缘页数
        num_display_entries: 5,                             // 主体页，(夹在边缘页的中间)
        callback: paginationCallBack,                     // 用户点击“1,2,3”这样的页码时调用这个行数对这个函数实现跳转
        items_per_page: pageInfo.pageSize,   // 当前一页显示的记录数量
        current_page: pageInfo.pageNum-1,    // 当前显示的页面
        prev_text:"上一页",
        next_text:"下一页"
    };
    // 生成页码导航条
    $("#Pagination").pagination(totalRecord,properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex,jQuery) {
    // 修改全局变量的pageNum
    window.pageNum = pageIndex+1;

    // 调用分页函数
    generatePage();

    return false;
}

```
4、role-page需要填充的主体
```java
 <tbody id="rolePageBody">
   <%--js填充--%>
 </tbody>
 <tfoot>
        <tr >
             <td colspan="6" align="center">
                 <div id="Pagination" class="pagination"></div>
             </td>
        </tr>

</tfoot>
```
5、关键词查询role-page页面
![QQ截图20220416183628.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220416183628-227403dd45294525b57a71cc46b5796f.png)
```java
<form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput"  class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
</form>
```
6、关键词查询role-page添加js代码
```java
<script>
    $(function () {

        // 1、为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        generatePage();

        // 2、关键词查询，修改全局变量的window.keyword即可
        $("#searchBtn").click(function () {
            window.pageNum = 1;
            window.keyword = $("#keywordInput").val();
            generatePage();
        });

    });

</script>
```
### 11.3、角色维护-角色新增
- 采用模态框，用户点击新增，弹出模态框提供用户新增页面，点击模态框里的保存按钮，执行保存操作
- 大概效果：
![QQ截图20220418193905.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418193905-f99520ca1ac64d3aadaa899ba11a7eed.png)
![QQ截图20220418193914.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418193914-09a3cb14e5024e248462b5634931cc4d.png)

#### 前端：
1、modal-role-add.jsp模板导入,打开方式：id
```java
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="addModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">添加角色</h4>
            </div>

            <div class="modal-body">
                <form  class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input type="text" name="roleName" class="form-control"  placeholder="请输入角色的名称" autofocus>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="saveRoleBtn" type="button" class="btn btn-primary">确认保存</button>
            </div>
        </div>
    </div><
</div>
```
2、role-page.jsp页面
2.1、引入模态框
![QQ截图20220418201914.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418201914-4fd028e8a173494b9142b283407ce1fa.png)
```java
<%@include file="modal-role-add.jsp"%>
```
```java
<button type="button"  id="showAddModalBtn"  class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
```
编写js：点击新增按钮弹出模态框，点击模态框里的保存按钮，执行保存操作
```java
 // 2、新增角色
        // 打开模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        // 给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            // 1、获取用户在文本框中输入的角色名称
            // $("#addModal [name=roleName]").val()表示在addModal的id下面的标签里面的name属性
            // $.trim（）去掉前空格
            var roleName =$.trim($("#addModal [name=roleName]").val());

            // 发送ajax请求
            $.ajax({
                "url":"role/save.json",
                "type":"post",
                "data":{
                    "name":roleName
                },
                "dataType":"json",
                "success":function (response) {
                    var  result = response.result;

                    if (result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);
                    }

                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框
            $("#addModal [name=roleName]").val("");

            // 重新加载分页数据,将页码定位到最后一页
            window.pageNum = 9999999;
            generatePage();

        });
```
#### 后端:
1、RoleService
```java
@Override
    public int insert(Role record) {
        return roleMapper.insert(record);
    }
```
2、RoleHandler
```java
@ResponseBody
    @RequestMapping("/role/save.json")
    public JSONResult<String> saveRole(Role role){
        roleService.insert(role);
        return JSONResult.successWithoutData();
    }
```
### 11.4、角色维护-角色更新
- 采用模态框，用户点击编辑，弹出模态框并且携带当前角色信息，用户修改角色信息执行保存
- 大概效果:
![QQ截图20220418203830.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418203830-e1540c75b4ff480c831f64e0cbbbedcf.png)
![QQ截图20220418203841.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418203841-50660265ca30479eb9021fbb7160bd01.png)
#### 前端:
1、modal-role-update.jsp模态框导入
![QQ截图20220418205042.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418205042-a0464a3492b64d759429d16d9572cc1a.png)
```java
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="updateModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">更新角色</h4>
            </div>

            <div class="modal-body">
                <form  class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input  type="text" name="roleName" class="form-control"  placeholder="请输入角色的名称" autofocus>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="updateRoleBtn" type="button" class="btn btn-primary">确认更新</button>
            </div>
        </div>
    </div><
</div>
```
2、role-page.jsp导入模态框
![QQ截图20220418201914.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220418201914-2271df97fedf420f9ef85ff9099276b7.png)
```java
<%@include file="modal-role-update.jsp"%>
```
3、因为编辑按钮的标签是动态生成的，用传统的解决不了点击触发问题，所以做了修改
```java
 // 4、更新角色
        // $(".updateRole").click(function () {
        //     $("#updateModal").modal("show");
        // })
        // 以上的打开模态框形式在翻页之后就不起作用了
        // 更改
        // 1、on()函数的第一个参数是事件类型
        // 2、on()函数的第二个参数是找到真正要绑定事件的元素的选择器
        // 3、on()函数的第三个参数是事件的响应函数
        $("#rolePageBody").on("click",".updateRole",function () {

            // 打开模态框
            $("#updateModal").modal("show");

            // 获取表格当前行中的角色名称
            // 标签在my-role.js文件中
            // button的父亲是td，当前td的上一个兄弟元素就是 var roleNameTd = "<td>"+roleName+"</td>"，它里面的文本就是text
            var roleName = $(this).parent().prev().text();

            $("#updateModal [name = roleName]").val(roleName);

            // 获取当前标签的id的值
            var roleId = this.id;
            window.roleId = roleId;
        });
        $("#updateRoleBtn").click(function () {
            var role={"id":window.roleId,"name":$("#updateModal [name = roleName]").val()};
            var roleJSON = JSON.stringify(role);
            $.ajax({
                "url":"role/update.json",
                "type":"post",
                "data":roleJSON,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    if (result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }

            });
            // 关闭模态框
            $("#updateModal").modal("hide");

            // 清理模态框
            $("#updateModal [name=roleName]").val("");
            generatePage();
        });

```
#### 后端:
1、RoleService
```java
// 根据主键id更新角色
    int updateByPrimaryKey(Role record);
```

2、RoleServiceImpl
```java
@Override
    public int updateByPrimaryKey(Role record) {
        return roleMapper.updateByPrimaryKey(record);
    }
```
3、RoleHandler
```java
   @ResponseBody
    @RequestMapping("/role/update.json")
    public JSONResult<String> updateRole(@RequestBody Role role){

        roleService.updateByPrimaryKey(role);

        return JSONResult.successWithoutData();
    }
```
### 11.5、角色维护-角色删除

- 考虑到单点删除和批量删除的复杂性，我们把它们的模态框写到统一的js文件，调用此模态框，角色数组就会把角色的名字动态添加到模态框显示出来。
![QQ截图20220419004805.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419004805-b281fd149634414785449169dc1fdb75.png)
```java
// 声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {
    // 打开模态框
    $("#removeModal").modal("show");

    // 清除旧的数据
    $("#roleNameDiv").empty();

    window.arrayRoleId = [];
    // 遍历数组
    for (var i = 0 ;i< roleArray.length; i++){
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"<br/>");
        window.arrayRoleId.push(role.roleId);
    }
}
```

##### 单点删除前端，因为单点删除的按钮也是前端动态生成的，所以，绑定也用$("#a").on("click",".b",function (){})
1、modal-role-rmove模态框的导入
```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="removeModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">删除角色</h4>
            </div>

            <div class="modal-body">
                <h5>请确认是否要删除下面的角色:</h5>
                <div id="roleNameDiv" style="text-align: center" ></div>
            </div>

            <div class="modal-footer">
                <button id="removeRoleBtn" type="button" class="btn btn-primary">确认删除</button>
            </div>
        </div>
    </div><
</div>
```
2、在role-page.jsp引入modal-role-remove.jsp模板
```java
<%@include file="modal-role-remove.jsp"%>
```
3、在role-page.jsp页面编写js触发完成请求
```java
// 5、删除角色
        // 开启 单点删除模态框
        $("#rolePageBody").on("click",".removeRole",function () {
            var roleArray =[{
                roleId:this.id,
                roleName:$(this).parent().prev().text()
            }];
            showConfirmModal(roleArray);
            generatePage();
        });
        $("#removeRoleBtn").click(function () {
            var requestBody = JSON.stringify(window.arrayRoleId);
            $.ajax({
                "url":"role/delete/by/role/id/array.json",
                "type":"post",
                "data": requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    if (response.result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (response.result =="FAILURE"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+""+response.statusText);
                }
            });
            // 关闭模态框
            $("#removeModal").modal("hide");

            // 清理模态框
            $("#removeModal [name=roleName]").val("");
            generatePage();
        });
```
##### 批量删除前端js
```java

        // 6、批量删除
        // 给总的checkbox绑定单击响应函数
        $("#summaryBox").click(function () {

            // 1、获取当前多选框自身的状态
            var currentStatus = this.checked;

            // 2、用当前多选框的状态设置其他的多选框
            $(".itemBox").prop("checked",currentStatus);

        });

        // 全部的反向操作
        $("#rolePageBody").on("click",".itemBox",function () {

            // 获取当前已经选中的.itemBox的数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 获取全部.itemBox的数量
            var totalBoxCount = $(".itemBox").length;

            // 使用二者的比较结果设置总的checkbox
            $("#summaryBox").prop("checked",checkedBoxCount == totalBoxCount);
            
        });
        // 给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 创建一个数组对象用来存放后面获取到的角色对象
            var arrayRole = [];

            // 遍历当前选中的多选框
            $(".itemBox:checked").each(function () {
                // 使用this引用当前遍历得到的多选框
                var roleId = this.id;

                // 通过DOM操作获取角色名称
                var roleName = $(this).parent().next().text();

                arrayRole.push({
                    "roleId":roleId,
                    "roleName":roleName
                });
            });

            // 检查roleArray的长度是否为0
            if (arrayRole.length == 0){
                layer.msg("请至少选择一个执行删除");
                return;
            }

            // 调用专门的函数打开模态框
            // 开启 删除模态框
            showConfirmModal(arrayRole);
            $("#removeRoleBtn").click(function () {
                var requestBody = JSON.stringify(window.arrayRoleId);
                $.ajax({
                    "url":"role/delete/by/role/id/array.json",
                    "type":"post",
                    "data":requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (response) {
                        if (response.result == "SUCCESS"){
                            layer.msg("操作成功");
                        }
                        if (response.result =="FAILURE"){
                            layer.msg("操作失败");
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status+""+response.statusText);
                    }
                });
            });
            // 关闭模态框
            $("#removeModal").modal("hide");

            // 清理模态框
            $("#removeModal [name=roleName]").val("");
            generatePage();
        });

```
##### 单点,批量删除后端:
1、RoleService
```java
// 传入数组，根据数组里面的id进行角色删除
    void removeRole(List<Integer> role);
````
2、RoleServiceImpl
```java
@Override
    public void removeRole(List<Integer> role) {
        RoleExample roleExample = new RoleExample();
       RoleExample.Criteria criteria = roleExample.createCriteria();
       // delete from t_role where id in (5,8,12)
       criteria.andIdIn(role);
        roleMapper.deleteByExample(roleExample);
    }
```

- - - 


## 12、菜单维护
**图示效果**：（约定整个树形结构节点的层次最多只能有3级）
![QQ截图20220419140202.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419140202-2febad9ffa6f432692ec1f16e4c03595.png)
### 12.1、数据库创建
1、创建数据库表
```mysql
create table t_menu
(
	id int(11) not NULL auto_increment,
	pid int(11),
	NAME VARCHAR(200),
	url VARCHAR(200),
	icon VARCHAR(200),
	primary key(id)
)
```
2、插入数据
```java
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('1',NULL,' 系统权限菜单','glyphicon glyphicon-th-list',NULL);
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('2','1',' 控 制 面 板 ','glyphicon glyphicon-dashboard','main.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('3','1','权限管理','glyphicon glyphicon glyphicon-tasks',NULL);
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('4','3',' 用 户 维 护 ','glyphicon glyphicon-user','user/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('5','3',' 角 色 维 护 ','glyphicon glyphicon-king','role/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('6','3',' 菜 单 维 护 ','glyphicon glyphicon-lock','permission/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('7','1',' 业 务 审 核 ','glyphicon glyphicon-ok',NULL);
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('8','7',' 实名认证审核','glyphicon glyphicon-check','auth_cert/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('9','7',' 广 告 审 核 ','glyphicon glyphicon-check','auth_adv/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('10','7',' 项 目 审 核 ','glyphicon glyphicon-check','auth_project/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('11','1',' 业 务 管 理 ','glyphicon glyphicon-th-large',NULL);
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('12','11',' 资 质 维 护 ','glyphicon glyphicon-picture','cert/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('13','11',' 分 类 管 理 ','glyphicon glyphicon-equalizer','certtype/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('14','11',' 流 程 管 理 ','glyphicon glyphicon-random','process/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('15','11',' 广 告 管 理 ','glyphicon glyphicon-hdd','advert/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('16','11',' 消 息 模 板 ','glyphicon glyphicon-comment','message/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('17','11',' 项 目 分 类 ','glyphicon glyphicon-list','projectType/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('18','11',' 项 目 标 签 ','glyphicon glyphicon-tags','tag/index.htm');
insert into `t_menu` (`id`, `pid`, `name`, `icon`, `url`) values('19','1',' 参 数 管 理 ','glyphicon glyphicon-list-alt','param/index.htm');
```
### 12.2、在java类中表示树形结构


 **1、基本方式**
- 在Menu类中使用List< Menu >children属性存储当前节点的子节点。

**2、为了配合zTree所需要添加的属性**
pid属性:找到父节点
name属性:作为节点名称
icon属性:当前节点使用的图标
open属性:控制节点是否默认打开
url属性:点击节点时跳转的位置


### 12.3、菜单维护-页面显示
- **1、目标**
将数据库中查询得到的数据到页面显示出来
- **2、思路**
数据库——>Java对象——>页面上使用zTree显示
- **3、代码：逆向工程**
![QQ截图20220419153138.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419153138-7acf81fc09c2413da521d440208ba691.png)

**逆向生成的实体类作出了调整**
![QQ截图20220419153314.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419153314-9ab24fd3be2749049951dce162a81f17.png)
```java
<table tableName="t_menu" domainObjectName="Menu" />
```

**创建MenuService、MenuServiceImpl、MenuHandler**
#### 后端:获取数据库的整个菜单
**1、MenuServiceImpl**
```java
 List<Menu> getAll();
```
**2、MenuService**
```java
    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }
```
**3、MenuHandler**
// 获取整个菜单，将根节点作为data返回（返回根节点也就是返回了整棵树）
```java
    @ResponseBody
    @RequestMapping("/menu/do/get.json")
    public JSONResult<Menu> getWholeTree(){

        // 1、通过Service层方法得到全部Menu对象的List
        List<Menu> menuList = menuService.getAll();

        // 2、声明一个Menu对象root，用来存放找到的根节点
        Menu root = null;

        // 3、使用map表示每一个菜单与id的对应关系
        Map<Integer,Menu> menuMap =new HashMap<>();

        // 4、将菜单id与菜单对象以k-v对模式存入map
        for (Menu menu: menuList) {
            menuMap.put(menu.getId(),menu);
        }
        for (Menu menu: menuList) {

            Integer pid = menu.getPid();

            if (pid == null){

                // 5、pid为null表示该menu是根节点
                root = menu;

                // 6、如果当前节点时根节点，那么肯定没有父节点，停止循环继续执行下一个循环
                continue;
            }

            // 7、如果pid不为null，说明当前节点有父节点，通过当前遍历的menu的pid得到其父节点
            Menu father = menuMap.get(pid);
            father.getChildren().add(menu);
        }

        // 8、将根节点作为data返回（返回根节点也就是返回了整棵树）
        return JSONResult.successNeedData(root);
    }
```

#### 前端:使用ztree展示整个页面结构
**1、配置mvc直接访问视图**
![QQ截图20220419205018.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419205018-370b667486874b1d82dea0a0c792d343.png)
```java
    <!--menu-->
    <mvc:view-controller path="/menu/to/page.html" view-name="menu-page"></mvc:view-controller>
```
**2、创建menu-page.jsp模板**
![QQ截图20220419205302.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220419205302-fad4f14742814a31b9225bc971d478ff.png)
```java
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>

<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"> </ul>>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</body>
</html>

```
**3、在menu-page页面引入ztree环境**
![QQ截图20220420182437.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220420182437-388b463b32544ec3a4004ce7ad48198b.png)
在menu-page.jsp菜单页面引入
![QQ截图20220420182706.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220420182706-d9e992b1ffc247e2b913adc9d4612ddf.png)
```java
<link href="ztree/zTreeStyle.css" rel="stylesheet" />
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>- 
```
**4、编写js，ajax请求后端拿到菜单数据显示页面**
效果：
<image src="http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423185343-f90b7d1f435045b5a58d07197601cbab.png" width="500px" height="100%">
```java
// 生成树形结构的函数
function generateTree() {

    // 2、准备生成树形结构的数据
    var zNodes =[];
    $.ajax({
        "url": "menu/do/get.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            if (response.result == "SUCCESS") {
                zNodes = response.data;
                //console.log(zNodes);
                // 1、创建JSON对象用于存储zTree所做的设置
                var setting = {
                   
                };
                // 3、初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);

            }
            if (response.result == "FAILURE") {
                layer.msg(response.message);
            }

        }
    });
}
```
**5、编写js更改原始的图标（原始图标存储在数据库中）**
**查看源代码：**
![QQ截图20220423192307.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423192307-ff010dfbf62f499c88b1be832d641d3c.png)
![QQ截图20220423191807.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423191807-e1c0944e65724b8389824210e409860b.png)

发现图标代码的id是一个拼接的字符串,这个拼接的字符串是**有规律**的：

**控制台输出**：认真观察发现**treeNode**是整棵树的输出结果，它的属性tId正是需要更改原始图标的id，认真观察发现class是图标的值，只要更改class的值就可以改变图标
![QQ截图20220423193148.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423193148-caff9ac443ce430aa6cbf39f65966336.png)
![QQ截图20220423192950.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423192950-b2f5d7d35a72401db5e99ce7149deaa3.png)
**更改原始图标前端代码：**
```java
function myAddDiyDom(treeId,treeNode) {

    // treeId是整个树形结构附着的ul标签的id
    console.log("treeId?="+treeId);
    // 当前全部节点的数据都在里面
    //console.log(treeNode);
    console.log(treeNode);
    // zTree生成id的规则
    // 例子：treeDemo_4_ico
    // 解析: ul标签的id_当前节点的序号_功能
    // 提示:  ul标签的id_当前节点的序号，部分可以通过访问treeNode的tId属性得到
    // 根据id的生成规则拼接出来span标签的id
    var spanId = treeNode.tId+"_ico";

    // 根据控制图标的span标签的id找到这个span标签
    // 删除旧的class
    $("#"+spanId).removeClass();
    // 添加新的class
    $("#"+spanId).addClass(treeNode.icon);
}
```
**在原来前端初始化菜单前端代码中添加更改图标方法，更其生效：**
```java
var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom
			}
                    };
```
```java
function generateTree() {

    // 2、准备生成树形结构的数据
    var zNodes =[];
    $.ajax({
        "url": "menu/do/get.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            if (response.result == "SUCCESS") {
                zNodes = response.data;
                //console.log(zNodes);
                // 1、创建JSON对象用于存储zTree所做的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom
			}
                    };
                // 3、初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);

            }
            if (response.result == "FAILURE") {
                layer.msg(response.message);
            }

        }
    });
}

```
**6、在鼠标移入节点范围时添加按钮组，离开时删除按钮组**
**效果：**
![QQ截图20220423200923.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423200923-b4086e33f964436c840867866808d8be.png) 
![QQ截图20220423201242.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423201242-f0edde2d0f5247f28f07e8ececd04a4f.png)

**解析:** 通过观察整个控制面板以及它的按钮组源码，发现他们都是在span标签中,并且span标签又**附着在a标签后面**
![QQ截图20220423202604.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423202604-bc17e5cbc20d4c8e8672055704450193.png)
**思路:** 
1、通过treeNode去拿到有规律的treeNode.tId,去拼接span的id。
2、因为根节点只能增加、分支节点能编辑和添加、叶子节点能增删改。
**添加按钮组代码:**
```java
	// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {
    // 1、按钮组的标签结构:<span><a><i></i></a><a><i>/i<></a></span>
    // 2、按钮组出现的位置：节点中treeDemo_n_a超链接的后面
    // 5、为了在需要移除按钮组的时候能够精确定位到按钮组所在span，需要给span设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 6、判断一下以前是否已经添加了按钮组
    if ($("#"+btnGroupId).length>0){
        return;
    }

    // 9、准备各个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn ="<a id='"+treeNode.id+"' class=' removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";


    // 10、获取当前节点的级别数据
    var level = treeNode.level;

    // 11、拼接装好的按钮代码
    var btmHTML = "";

    // 12、判断当前节点的级别
    if (level == 0){
        // 级别为0时是根节点，只能添加子节点
        btmHTML=addBtn;
    }
    if (level == 1){
        // 级别为1时是分支节点，可以添加子节点，修改
        btmHTML =addBtn +" "+editBtn;

        // 获取当前节点的子节点数量
        var length = treeNode.children.length

        // 如果没有子节点，可以删除
        if (length == 0){
            btmHTML = btmHTML + " " + removeBtn;
        }
    }
    if (level == 2){
        // 级别为2时，可以修改删除
        btmHTML = editBtn + " " +removeBtn;
    }


    // 3、找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";

    // 4、执行在超链接后面附加span元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btmHTML+"</span>");
}

```
**删除按钮组代码**
```java
// 在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {
    // 7、拼接按钮组的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 8、移除对应的元素
    $("#"+btnGroupId).remove();
}
```
**在原来前端初始化菜单前端代码中添加更改图标方法，更其生效：**
```java
var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
```

```java
function generateTree() {

    // 2、准备生成树形结构的数据
    var zNodes =[];
    $.ajax({
        "url": "menu/do/get.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            if (response.result == "SUCCESS") {
                zNodes = response.data;
                //console.log(zNodes);
                // 1、创建JSON对象用于存储zTree所做的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
                    "data": {
                        // 让菜单点击实现不跳转
                        "key": {
                            "url": "maomi"
                        }
                    }
                };
                // 3、初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);

            }
            if (response.result == "FAILURE") {
                layer.msg(response.message);
            }

        }
    });
}
```

#### 前后端的增删改
##### 前端：
![QQ截图20220423210310.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220423210310-b07c099633e14eba8ed4b689d290644d.png)
```java
<script>
    $(function () {
        // 初始化显示页面
        generateTree();

        // 给添加子节点按钮绑定单击响应函数
        $("#treeDemo").on("click",".addBtn",function () {
            // 将当前节点的id作为新节点的pid保存到全局变量
            window.pid = this.id;
            // 打开模态框
            $("#menuAddModal").modal("show");

            return false;
        });
        
        // 给添加子节点的模态框中的 保存按钮 绑定单击响应函数
        $("#menuSaveBtn").click(function () {
            // 手机表单项中用户输入的数据
           var name =  $.trim($("#menuAddModal [name = name]").val());
           var url = $.trim($("#menuAddModal [name = url]").val());

           // 单选按钮要定位到“被选中”的那一个
           var icon = $("#menuAddModal [name = icon]:checked").val();

           // 发送ajax请求
            $.ajax({
                "url":"menu/save.json",
                "type":"post",
                "data":{
                    "pid":window.pid,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                "dataType":"json",
                "success":function (response) {
                    var result =response.result;
                    if (result == "SUCCESS"){
                        layer.msg("操作成功！");
                        // 重新加载树形结构
                        generateTree();
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            $("#menuAddModal").modal("hide");
            // 清空表单
            // jQuery调用click()函数，里面不传任何参数，相当于用户点击了一下
            $("#menuResetBtn").click();
        });




        // 给更新节点按钮绑定单击响应函数
        $("#treeDemo").on("click",".editBtn",function () {
            // 将当前节点的id保存到全局变量中
            window.id = this.id;

            // 打开模态框
            $("#menuEditModal").modal("show");

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            // 根据id属性查询节点对象
            // 用来搜索节点的属性名
            var key = "id";

            // 用来搜索节点的属性值
            var value = window.id;

            var currentNode = zTreeObj.getNodeByParam(key,value);

            // 回显表单数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);

            // 回显radio可以这样理解:被选中的radio的value属性可以组成一个数组
            // 然后再用这个数组设置回radio，就能够把对应的值选中
            $("#menuEditModal [name=icon]").val([currentNode.icon]);
            return false;
        });

        $("#menuEditBtn").click(function () {
            // 收集表单数据
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();

            // 发送Ajax请求
            $.ajax({
                "url":"menu/update.json",
                "type":"post",
                "data":{
                    "id":window.id,
                    "name":name,
                    "url":url,
                    "icon":icon
                },
                "dataType":"json",
                "success":function (response) {
                    var result = response.result;

                    if (result == "SUCCESS"){
                        layer.msg("操作成功");

                        generateTree();
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);

                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });
            $("#menuEditModal").modal("hide");
        });

        // 给“x”按钮绑定单击响应函数
        $("#treeDemo").on("click",".removeBtn",function () {
            // 将当前节点的id保存到全局变量中
            window.id = this.id;

            // 打开模态框
            $("#menuConfirmModal").modal("show");

            // 获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

            // 根据id属性查询节点对象
            // 用来搜索节点的属性名
            var key = "id";

            // 用来搜索节点的属性值
            var value = window.id;

            var currentNode = zTreeObj.getNodeByParam(key,value);

            // $("#removeNodeSpan").text(currentNode.name);
            $("#removeNodeSpan").html("<i class='"+ currentNode.icon+"'></i>"+currentNode.name)

            return false;
        });
        $("#confirmBtn").click(function () {
            $.ajax({
                "url":"menu/remove.json",
                "type":"post",
                "data":{
                    "id":window.id
                },
                "dataType":"json",
                "success":function (response) {
                    var result = response.result;

                    if (result == "SUCCESS"){
                        layer.msg("操作成功");

                        generateTree();
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);

                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });
            $("#menuConfirmModal").modal("hide");
        })
    });
</script>	
```
##### 后端：
handler
```java
    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public JSONResult<String> removeMenu(Menu menu){

        menuService.removeMenu(menu);

        return JSONResult.successWithoutData();
    }


    @ResponseBody
    @RequestMapping("/menu/update.json")
    public JSONResult<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return JSONResult.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public JSONResult<String> saveMenu(Menu menu){
        Integer menuPid =(Integer)menu.getPid();
        menu.setPid(menuPid);
        menuService.insert(menu);
        return JSONResult.successWithoutData();
    }
```
Service
```java
    @Autowired
    private MenuMapper menuMapper;


    @Override
    public int insert(Menu record) {
        return menuMapper.insert(record);
    }

    @Override
    public void updateMenu(Menu menu) {
        // 由于pid没有传入，一定要使用有选择更新，保证“pid”字段不会被置空
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void removeMenu(Menu menu) {
        Integer id = menu.getId();
        menuMapper.deleteByPrimaryKey(id);
    }
```

## 13、角色分配Admin-role
- 思路：
<image src="http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426004545-165d7adb0ccf46e798b73ddcd38b9fd9.png" height="600px"></image>
- 代码：
### 13.1、数据库设计
- 用户和角色是多对多的关系
- 多对多关系，要建立用户与角色的中间表inner_admin_role
```java
CREATE TABLE `inner_admin_role` (
  	`id` int NOT NULL AUTO_INCREMENT,
  	`admin_id` int DEFAULT NULL,
  	`role_id` int DEFAULT NULL,
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
```
- 效果
![QQ截图20220426010446.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426010446-06fea9e876ff4a7fb9340d410f0297a7.png)
### 13.2、修改admin-page页面，角色分配按钮
![QQ截图20220426005153.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426005153-83d29eb5a4aa407e92e68a0252f9ea24.png)

![QQ截图20220426005543.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426005543-0f9611e58da5496ca7b1f3540425d0d9.png)
```java
 <a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></a>
```
- 要求携带参数：pageNum,keyword,adminId
- 分析:
- - 1. 携带当前的pageNum，keyword是为了在保存分配完角色后更好跳转到之前的页面
- - 2. 携带adminId，是为了根据用户id获取当前用户在inner_admin_role所对应有多少个角色
（select id,name from t_role where id in (select role_id from inner_admin_role where admin_id =#{adminId})）

### 13.3、用户点击分配按钮

![QQ截图20220426003917.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426003917-a14e2eb7e378433394057328dab8abe4.png)

**前端:**
- 1、修改超链接
![QQ截图20220426005153.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426005153-83d29eb5a4aa407e92e68a0252f9ea24.png)

![QQ截图20220426005543.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426005543-0f9611e58da5496ca7b1f3540425d0d9.png)
```java
 <a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></a>
```
- 2、准备跳转目标的资源assign—page.jsp
```java
<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#rightBtn").click(
            function () {
                // 把左边选中的option添加到右边
                $("select:eq(0)>option:selected").appendTo("select:eq(1)");
                // 删除左边的已选中的option元素
                $("select:eq(0)>option:selected").prop();
            }
        );
        $("#leftBtn").click(
            function () {
                // 把右边边选中的option添加到左边
                $("select:eq(1)>option:selected").appendTo("select:eq(0)");
                // 删除右边的已选中的option元素
                $("select:eq(0)>option:selected").prop();
            }
        );
        $("#submitBtn").click(
            function () {
                // option有一个选中标签，selected，设置它的属性为selected就可以选中它
                $("select:eq(1)>option").prop("selected","selected")
        })

    })
</script>
<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post"  role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select  class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="rightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="leftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option  value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button id="submitBtn" type="submit" style="width: 150px;margin: 50px auto 0px auto"  class="btn btn-lg btn-success btn-block">保存</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</body>
</html>


```
**后端1:**

- 目标：携带查询当前用户的请求参数和请求结果跳转到操作分配页面显示出来
- 效果：![QQ截图20220426003951.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426003951-503823dc2e134347b955db3e1485d404.png)

- 代码:
```java
assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}
```
**AssignHandler：**
```java
 // 根据管理员的AdminId显示它所分配的角色和未分配的角色
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam("adminId") Integer adminId,
                                    ModelMap modelMap
    ){
        // 1、查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 2、查询未分配的角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);

        // 3、存入模型
        modelMap.addAttribute("assignedRoleList",assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList",unAssignedRoleList);
        return "assign-role";
    }
```

**RoleServiceImpl:**
```java
 // 获取已分配的角色(assign)
    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return roleMapper.selectAssignedRole(adminId);
    }

    // 获取未分配的角色(assign)
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignedRole(adminId);
    }
```

**RoleMapper:**
```java
List <Role> selectAssignedRole(Integer adminId);

List<Role> selectUnAssignedRole(Integer adminId);
```

**RoleMapper.xml**
```java
<!--根据adminId查询没有分配的角色-->
  <select id="selectAssignedRole" resultMap="BaseResultMap">
    select id,name from t_role where id in (select role_id from inner_admin_role where admin_id =#{adminId})
  </select>

  <!--根据adminId查询已经分配的角色-->
  <select id="selectUnAssignedRole" resultMap="BaseResultMap">
    select id,name from t_role where id not in (select role_id from inner_admin_role where admin_id =#{adminId})
  </select>
```
**后端2:**
- 目标：用户在分配角色时，后端先接收Admin用户Id，把原先的之前已经分配角色的先删除，把现在预分配的角色执行保存，最后依靠请求中的pageNum，keyword定位到原始页面。


- 效果：
![QQ截图20220426004006.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426004006-401629c7b535494d946a484dc06a0b93.png)

- 代码：

**RoleHandler:**
```java
    // 先删除已分配的角色，再执行保存要分配的角色
    @RequestMapping("/assign/do/role/assign.html")
    public String doAssignRole(
                    @RequestParam("pageNum") Integer pageNum,
                    @RequestParam("keyword") String keyword,
                    @RequestParam("adminId") Integer adminId,

                    // required=false,设置请求的roleIdList的值可以为空
                    @RequestParam(value = "roleIdList",required = false) List<Integer> integerList
    ){
        
        roleService.deleteOldRoleToSaveNewRoleForAdmin(adminId,integerList);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
}
```
**RoleServiceImpl:**
```java
   // 先删除旧的角色在保存新的角色(assign)
    @Override
    public void deleteOldRoleToSaveNewRoleForAdmin(Integer adminId, List<Integer> integerList) {
        // 1、先根据Admin的id删除旧的角色
        roleMapper.deleteOldRoleForAdmin(adminId);
        if (integerList!=null){

            // 2、对新填写的新的角色执行保存
            roleMapper.saveNewRoleForAdmin(adminId,integerList);
        }

    }
```
**RoleMapper:**
```java
    void deleteOldRoleForAdmin(Integer adminId);

    void saveNewRoleForAdmin(@Param("adminId") Integer adminId, @Param("roleIdList") List<Integer> roleIdList);
```
**RoleMapper.xml**
```java
  <!--根据adminId删除旧的已经分配的角色-->
  <delete id="deleteOldRoleForAdmin">
    delete from inner_admin_role where admin_Id=#{adminId}
  </delete>

  <!--根据adminId把它所携带的roleId保存到数据库中-->
  <insert id="saveNewRoleForAdmin">
    insert into inner_admin_role (admin_Id,role_Id) values
    <foreach collection="roleIdList" separator="," item="roleId">
      (#{adminId},#{roleId})
    </foreach>
  </insert>
```
## 14、权限分配Role-Auth
- **思路：**
![QQ截图20220426231320.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220426231320-6a723736958444698c3d8d40e5d42dc8.png)
- **代码:**
### 14.1、数据库设计
创建**t_auth**表
```java
CREATE TABLE t_auth (
	id int(11) NOT NULL AUTO_INCREMENT,
	name varchar(200) DEFAULT NULL,
	title varchar(200) DEFAULT NULL,
	category_id int(11) DEFAULT NULL,

	PRIMARY KEY (id)
);
```
插入数据
```java
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(1,'','用户模块',NULL);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(2,'user:delete','删除',1);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(3,'user:get','查询',1);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(4,'','角色模块',NULL);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(5,'role:delete','删除',4);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(6,'role:get','查询',4);
INSERT INTO t_auth(id,`name`,title,category_id) VALUES(7,'role:add','新增',4);
```
分析字段
1)name字段：给资源分配权限或给角色分配权限时使用的具体值，将来做权限验证也是使用name字段的值来进行对比。建议使用英文
2)title字段：在页面显示，让用户便于查看的值。建议使用中文
3)category_id字段：关联到当前权限所属的分类。这个关联不是到其他表关联，而是就在当前表内部进行关联，关联其他记录

### 14.2、前端
点击权限分配菜单弹起模态框，显示角色已经分配的权限
![QQ截图20220428193719.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428193719-c39e925d13c747108378caa172e088c6.png)
![QQ截图20220428193754.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428193754-40316438ad7347998449b15b747bc46c.png)
**1)** 引入模态框
![QQ截图20220428194843.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428194843-821edf3bf5f0413c8b4b576da6190c4a.png)
```java
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="assignModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">分配权限信息</h4>
            </div>
            <div class="modal-body delete-body">
                <ul id="authTreeDemo" class="ztree">

                </ul>
            </div>
            <div class="modal-footer">
                <button id="assignAuthBtn" type="button" class="btn btn-success">
                    执行分配</button>
            </div>
        </div>
    </div>
</div>
```
**2)** 在role-page页面引入模态框，权限分配按钮是动态生成的，得到按钮的类名，显示模态框
![QQ截图20220428194927.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428194927-a5dae2622bd54cf2baff9444770820b6.png)
![QQ截图20220428195531.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428195531-a529c47c72f34a9f953ac505d4c7183a.png)
```java
	$("#rolePageBody").on("click",".checkBtn",function () {
            window.roleId = this.id;
            $("#assignModal").modal("show");
            generateAuthTree();
        });
```
**3)** generateAuthTree()函数的展开,显示树形结构，根据id回显已分配的权限
![QQ截图20220428195903.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220428195903-08aeed87617d4ed6996690c8a8b41976.png)
```java
	function generateAuthTree() {
    var ajaxReturn = $.ajax({
        "url":"assign/get/tree.json",
        "type":"post",
        "async":false,
        "dataType":"json"
    });
    if (ajaxReturn.status!=200){
        layer.msg("error!status:"+ajaxReturn.status+"errorMessage"+ajaxReturn.statusText);
    }

    var resultEntity = ajaxReturn.responseJSON;

    if (resultEntity.result == "FAILURE"){
        layer.msg("操作失败！"+resultEntity.message);
    }

    if (resultEntity.result == "SUCCESS"){
        var authList = resultEntity.data;
        // 将服务端查询的list交给zTree自己组装

        var setting = {
            data:{
                // 开启简单JSON功能
                simpleData: {
                    enable: true,
                    // 通过pIdKey属性设置父节点的属性名，而不使用默认的pId
                    pIdKey:"categoryId"
                },
                key: {
                    // 设置在前端显示的节点名是查询到的title，而不是使用默认的name
                    name:"title"
                }
            },
            // 开启模态框的勾选选项
            check: {
                enable: true
            }
        };

        // 生成树形结构信息
        $.fn.zTree.init($("#authTreeDemo"),setting,authList);

        // 设置节点默认是展开的
        // 1 得到zTreeObj
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
        // 2 设置默认展开
        zTreeObj.expandAll(true);

        // ----------回显----------

        // 回显（勾选）后端查出的匹配的权限
        ajaxReturn = $.ajax(
            {
                "url":"assign/get/checked/auth/id.json",
                "type":"post",
                "dataType":"json",
                "async":false,
                "data":{
                   "roleId":window.roleId
                }
            }
        );
        if (ajaxReturn.status != 200){
            layer.msg("请求出错！错误码："+ ajaxReturn.status + "错误信息：" + ajaxReturn.statusText);
            return ;
        }

        resultEntity = ajaxReturn.responseJSON;

        if (resultEntity.result == "FAILED"){
            layer.msg("操作失败！"+resultEntity.message);
        }

        if (resultEntity.result == "SUCCESS"){
            var authIdArray = resultEntity.data;

            // 遍历得到的authId的数组
            // 根据authIdArray勾选对应的节点
            for (var i = 0; i < authIdArray.length;i++){
                var authId = authIdArray[i];

                // 通过id得到treeNode
                var treeNode = zTreeObj.getNodeByParam("id",authId);

                // checked设置为true，表示勾选节点
                var checked = true;

                // checkTypeFlag设置为false，表示不联动勾选
                // 即父节点的子节点未完全勾选时不改变父节点的勾选状态
                // 否则会出现bug：前端只要选了一个子节点，传到后端后，下次再调用时，发现前端那个子节点的所有兄弟节点也被勾选了，
                // 因为在子节点勾选时，父节点也被勾选了，之后前端显示时，联动勾选，导致全部子节点被勾选
                var checkTypeFlag = false;

                // ztreeObj的checkNode方法 执行勾选操作
                zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
            }
        }
    }
}
```
**4)** 模态框的保存按钮,保存勾选的数据
```java
	// 给分配权限的模态框中的提交按钮设置单击响应函数
        $("#assignAuthBtn").click(
            function () {
           // 声明一个数组，用来存放被勾选的auth的id
           var authIdArray =[];

           // 拿到zTreeObj
           var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

           // 通过getCheckedNodes方法拿到被选中的option信息
           var authArray = zTreeObj.getCheckedNodes();

           for (var i =0;i<authArray.length;i++){
               // 从被选中的auth中遍历得到每一个auth的id
               var authId = authArray[i].id;
               // 通过push方法将得到的id存入authIdArray
               authIdArray.push(authId);
           }

           var requestBody ={
               // 为了后端取值方便，两个数据都用数组格式存放，后端统一用List<Integer>获取
               "roleId":[window.roleId],
               "authIdList":authIdArray
           }
           requestBody = JSON.stringify(requestBody);

           $.ajax({
               "url":"assign/do/save/role/auth/relationship.json",
               "type":"post",
               "data":requestBody,
               "contentType":"application/json;charset=UTF-8",
               "success":function (response) {
                   if (response.result == "SUCCESS"){
                       layer.msg("操作成功!");
                   }
                   if (response.result == "FAILURE"){
                       layer.msg("操作失败！提示信息："+response.message);
                   }
               },
               "error":function (response) {
                   layer.msg(response.status+" "+response.statusText);
               }

           });

           // 关闭模态框
           $("#assignModal").modal("hide");

        });
```
### 14.3、后端
**1)AuthService**
```java
public interface AuthService {
    List<Auth> getAuthList();

    List<Integer> getAuthByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);
}
```

**2)AuthServiceImpl**
```java
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    // 获取所有权限节点，获取auth实体的整体信息
    @Override
    public List<Auth> getAuthList() {
        return authMapper.selectByExample(new AuthExample());
    }

    // 根据角色id获取它所拥有的权限
    @Override
    public List<Integer> getAuthByRoleId(Integer roleId) {
        return authMapper.getAuthByRoleId(roleId);
    }

    // 接收前端的角色的id和所做出的分配的权限执行保存
    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 通过键值获取roleId、authList
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        List<Integer> authList = map.get("authIdList");
        // 1、先根据roleId 删除之前存在的权限
        authMapper.removeOldRoleAuth(roleId);
        // 2、根据roleId、和authList在对应的表中插入数据
        if (authList!=null&&authList.size()>0){
            authMapper.addNewRoleAuth(roleId,authList);
        }

    }
}
```
**3)AssignHandler**
```
@Controller
public class AssignHandler {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    // 给角色分配权限
    @ResponseBody
    @RequestMapping("/assign/do/save/role/auth/relationship.json")
    public JSONResult<String> saveRoleAuthRelationship(
            // 用一个map接收前端发来的数据
            @RequestBody Map<String,List<Integer>> map
    ){
        // 保存更改后的Role与Auth关系
        authService.saveRoleAuthRelationship(map);

        return JSONResult.successWithoutData();

    }

    // 获得被勾选的auth信息，提供给前端回显
    @ResponseBody
    @RequestMapping("/assign/get/checked/auth/id.json")
    public JSONResult<List<Integer>> getAuthByRoleId(Integer roleId){
        List<Integer> authIdList = authService.getAuthByRoleId(roleId);
        return JSONResult.successNeedData(authIdList);
    }

    // 获取auth实体的整体信息
    @ResponseBody
    @RequestMapping("/assign/get/tree.json")
    public JSONResult<List<Auth>> getAuthTree(){
        List<Auth> authList = authService.getAuthList();
        return JSONResult.successNeedData(authList);
    }

}
```
4)AuthMapper.xml
```
  <!--从inner_role_auth查找匹配roleId的auth_id-->
  <select id="getAuthByRoleId" resultType="int">
    select auth_id from inner_role_auth
    where role_id = #{roleId}
  </select>

  <delete id="removeOldRoleAuth">
    delete from inner_role_auth where role_id = #{roleId}
  </delete>

  <insert id="addNewRoleAuth" >
    insert into inner_role_auth (role_id,auth_id) values
    <foreach collection="authList" separator="," item="authId">
      (#{roleId},#{authId})
    </foreach>
  </insert>
```

## 15、登录-状态检查
- 目标：
	将部分资源保护起来,让没有登录的请求不能访问
- 思路：
![image.png](http://42.194.206.10:8090/upload/2022/03/image-e6d74e99cecb49ac88bab5a6c745708f.png)
- 代码：
	创建拦截器的类
```java
//登录拦截器，要去mvc注册才生效
Public class LoginInterceptor extends HandlerInterceptorAdapter{
		
	@Override
	Public booleanpreHandle(HttpServletRequest request,HttpServletResponse response,Object handler)throws Exception{
			//1、通过request对象获取Session对象
			HttpSession session=request.getSession();
		
			//2、尝试从Session域中获取Admin对象
			Admin admin=(Admin)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN_SESSION);
		
			//3、判断Session域中是否有Admin对象
			if(admin==null){
			Throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
			}
			//如果Admin对象不为null，则放行
			Return true;
			}
}
```
注册拦截器
```java
<!--注册拦截器-->
<mvc:interceptors>
		<mvc:interceptor>
		<!--配置要拦截的资源
		/*对应一层路径
		/**所有路径
		-->
		<mvc:mappingpath="/**"/>
		<!--不用拦截的资源-->
		<mvc:exclude-mappingpath="/admin/to/login/page.html"/>
		<mvc:exclude-mappingpath="/admin/do/login.html"/>
					
		<!--配置拦截资源的类-->
		<beanclass="net.seehope.crowd.interceptor.LoginInterceptor"></bean>
		</mvc:interceptor>
</mvc:interceptors>
```
![QQ截图20220401201731.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220401201731-50123156e8db47ffb9dc004d8d3e5464.png)![QQ截图20220408122345.png](http://42.194.206.10:8090/upload/2022/04/QQ%E6%88%AA%E5%9B%BE20220408122345-be84f3f1880f46d88789bcda84dd8de1.png)
