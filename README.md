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
