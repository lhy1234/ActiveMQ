# Active MQ的安全机制

### web控制台安全

 1、管理后台不要对外公开，不能让外网访问 

 例如禁用端口号等 

 2、设置后台访问的密码，密码要复杂一点，不要公开 

 /conf/jetty.xml将配置项authenticate值改为 true，现在ActiveMQ新版本默认authenticate是设置的true，所以也不用改 

```xml
 <bean id="adminSecurityConstraint" class="org.eclipse.jetty.util.security.Constraint">
        <property name="name" value="BASIC" />
        <property name="roles" value="admin" />
         <!-- set authenticate=false to disable login -->
        <property name="authenticate" value="true" />
    </bean>
```

 ● /conf/jetty-realm.properties 设置用户名和密码，格式为-->用户名:密码,角色名 

admin: admin, admin
user: user, user



eg，我发现改apache-activemq-5.16.0\conf\users.properties 也好使

格式是：用户名=密码



### 消息安全机制

 主要是添加访问的用户名和密码，实现方式有两种： 

### 1.在conf/activemq.xml文件中的内添加访问密码

位置在</shutdownHooks>下面

```xml
<plugins>
<simpleAuthenticationPlugin>
    <users>
        <!--一般我们就配置第一个即可，下面两个了解-->
        <authenticationUser username="system" password="123456" groups="users,admins"/>
        <authenticationUser username="user" password="123456"  groups="users"/>
        <authenticationUser username="guest" password="123456" groups="guests"/>
    </users>
</simpleAuthenticationPlugin>
</plugins>
```

 重启ActiveMQ服务 

(1) 基于JMS开发的消息传送需要修改的地方 

```java
ActiveMQConnectionFactory cf = new 			ActiveMQConnectionFactory("system","123456",brokerURL);
```

(2) 集成Spring框架需要修改的地方 

```xml
<!-- 配置一个连接工厂 -->
<bean id="connectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
    <property name="brokerURL" value="tcp://192.168.235.128:61616"/>
    <property name="userName" value="system"/>
    <property name="password" value="123456"/>
</bean>
```

(3) 集成SpringBoot框架需要修改的地方 

```properties
#用户名
spring.activemq.user=system
#密码
spring.activemq.password=123456
```

### 2、使用JAAS身份验证和授权

 Java Authentication Authorization Service(JAAS，Java验证和授权服务)，它可以通过插件的方式集成到你的应用程序中，提供验证和授权服务。我们在开发的时候基本上不会用到这种方式，ActiveMQ集成了这种验证方式，所以我们直接配置使用即可。 

注意：ActiveMQ配置文件如果有中文，有的时候会报错，所以我们在配置的时候将中文注释删除掉 

 ActiveMQ加入JAAS身份验证，共需要对4个文件进行配置： 

● activemq.xml

● login.config

● groups.properties

● users.properties

(1) 在conf/activemq.xml文件中加上

如果配置以下内容，需要将第一种方式配置的内容删除掉

```xml
<plugins>
        <!--use JAAS to authenticate using the login.config file on the classpath to configure JAAS -->
<jaasAuthenticationPlugin configuration="activemq" />
        <!--  lets configure a destination based authorization mechanism -->
<authorizationPlugin>
<map>
    <authorizationMap>
        <authorizationEntries>
            <!-->表示通配符,例如USERS.>表示以USERS.开头的主题,>表示所有主题,read表示读的权限,write表示写的权限，admin表示角色组-->
            <authorizationEntry queue=">" read="admins" write="admins" admin="admins" />
            <authorizationEntry topic=">" read="admins" write="admins" admin="admins" />
            <authorizationEntry queue="ActiveMQ.Advisory.>" read="admins" write="admins" admin="admins" />
            <authorizationEntry topic="ActiveMQ.Advisory.>" read="admins" write="admins" admin="admins" />
        </authorizationEntries>
    </authorizationMap>
</map>
</authorizationPlugin>
        </plugins>
```

 (2) 配置conf/login.config(默认是正确的，所以不需要修改) 

 注意：activemq名字和activemq.xml配置的名字要一直 

```properties
ctivemq {  
    org.apache.activemq.jaas.PropertiesLoginModule required  
    org.apache.activemq.jaas.properties.user="users.properties"  
    org.apache.activemq.jaas.properties.group="groups.properties";  
}; 
```

 (3) 配置conf/groups.properties 

```properties
#group=userName  
 admins=system 
```

 (4) 配置conf/users.properties 

```properties
#userName=password   
system=123456 
```



