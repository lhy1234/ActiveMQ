# ActiveMQ持久化消息与非持久化消息

### KahaDB存储

![1609597201733](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609597201733.png)

KahaDB是默认的持久化策略，所有消息顺序添加到一个日志文件中，同时另外有一个索引文件记录指向这些日志的存储地址，还有一个事务日志用于消息回复操作。是一个专门针对消息持久化的解决方案,它对典型的消息使用模式进行了优化。

在dapache-activemq-5.16.0\data\kahadb这个目录下，会生成四个文件，来完成消息持久化 

1. db.data 它是消息的索引文件，本质上是B-Tree（B树），使用B-Tree作为索引指向db-*.log里面存储的消息
2. db.redo 用来进行消息恢复 
3. db-*.log 存储消息内容。新的数据以APPEND的方式追加到日志文件末尾。属于顺序写入，因此消息存储是比较 快的。默认是32M，达到阀值会自动递增 
4. lock文件 锁，写入当前获得kahadb读写权限的broker ，用于在集群环境下的竞争处理

#### 配置文件

apache-activemq-5.16.0\conf\activemq.xml**

```xml
 <!--
            Configure message persistence for the broker. The default persistence
            mechanism is the KahaDB store (identified by the kahaDB tag).
            For more information, see:

            http://activemq.apache.org/persistence.html
        -->
<!--directory:保存数据的目录;journalMaxFileLength:保存消息的文件大小 --> 
        <persistenceAdapter>
            <kahaDB directory="${activemq.data}/kahadb" journalMaxFileLength="16mb"/>
        </persistenceAdapter>
```

#### 特性

1、日志形式存储消息；

2、消息索引以 B-Tree 结构存储，可以快速更新；

3、 完全支持 JMS 事务；

4、支持多种恢复机制kahadb 可以限制每个数据文件的大小。不代表总计数据容量。

### AMQ 方式

 只适用于 5.3 版本之前。 AMQ 也是一个文件型数据库，消息信息最终是存储在文件中。内存中也会有缓存数据。 

性能高于 JDBC，写入消息时，会将消息写入日志文件，由于是顺序追加写，性能很高。

为了提升性能，创建消息主键索引，并且提供缓存机制，进一步提升性能。

每个日志文件的 大小都是有限制的（默认 32m，可自行配置） 。

当超过这个大小，系统会重新建立一个文件。

当所有的消息都消费完成，系统会删除这 个文件或者归档。

主要的缺点是 AMQ Message 会为每一个 Destination 创建一个索引，如果使用了大量的 Queue，索引文件的大小会占用很多磁盘空间。

而且由于索引巨大，一旦 Broker（ActiveMQ 应用实例）崩溃，重建索引的速度会非常 慢。

虽然 AMQ 性能略高于 Kaha DB 方式，但是由于其重建索引时间过长，而且索引文件 占用磁盘空间过大，所以已经不推荐使用。

### JDBC存储

#### 概念

 http://activemq.apache.org/persistence.html 

使用JDBC持久化方式，数据库默认会创建3个表，每个表的作用如下：

1. activemq_msgs：queue和topic的消息都存在这个表中 
2. activemq_acks：存储持久订阅的信息和最后一个持久订阅接收的消息ID 
3. activemq_lock：跟kahadb的lock文件类似，确保数据库在某一时刻只有一个broker在访问 

ActiveMQ 将数据持久化到数据库中，不指定具体的数据库。 可以使用任意的数据库中，本环节中使用 MySQL 数据库。 

下述文件为 activemq.xml 配置文件部分内容：

首先定义一个 mysql-ds 的 MySQL 数据源，然后在 persistenceAdapter 节点中配置 jdbcPersistenceAdapter 并且引用刚才定义的数据源。

dataSource 指定持久化数据库的 bean，createTablesOnStartup 是否在启动的时候创建数 据表，默认值是 true，这样每次启动都会去创建数据表了，一般是第一次启动的时候设置为 true，之后改成 false。

#### 配置

**Beans中添加** 

```xml
 <!-- MySql DataSource Sample Setup --> 
  <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close"> 
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/> 
    <property name="url" value="jdbc:mysql://localhost/activemq?relaxAutoCommit=true"/> 
    <property name="username" value="root"/> 
    <property name="password" value="root"/> 
    <property name="poolPreparedStatements" value="true"/> 
  </bean> 
```

 **修改persistenceAdapter** 改变activeMQ默认的持久化方式

```xml
  <persistenceAdapter>
    <!-- <kahaDB directory="${activemq.data}/kahadb"/> -->
	<jdbcPersistenceAdapter dataSource="#mysql-ds" createTablesOnStartup="true" /> 
  </persistenceAdapter>
```

#### 依赖jar包  

由于activeMQ消息服务器，没有自带mysql数据库的驱动程序。我们需要手动将mysql驱动添加到消息服务器。 

 commons-dbcp 、 commons-pool 、 mysql-connector-java 

拷贝到apache-activemq-5.16.0\lib

![1609599688578](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609599688578.png)

注意jd mysql-connector-java 的版本必须是5.x，我第一次弄的6.0报错了，修改activemq.xml里面jdbc连接也没用！！！

然后在mysql里建个库：activemq，

#### 表：

再启动activemq：生成了三张表

![1609599853733](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609599853733.png)

1. **activemq_acks**：

   用于存储订阅关系。如果是持久化Topic，订阅者和服务器的订阅关系在这个表保存。 主要的数据库字段如下： 

```sql
CREATE TABLE `activemq_acks` (
  `CONTAINER` varchar(250) NOT NULL, -- 消息的destination 
  `SUB_DEST` varchar(250) DEFAULT NULL,-- 如果是使用static集群，这个字段会有集群其他系统的信息 
  `CLIENT_ID` varchar(250) NOT NULL,--每个订阅者都必须有一个唯一的客户端id用以区分 
  `SUB_NAME` varchar(250) NOT NULL,-- 订阅者名称 
  `SELECTOR` varchar(250) DEFAULT NULL,--选择器，可以选择只消费满足条件的消息。条件可以用自定义属性实现，可支持多属性and和or操作 
  `LAST_ACKED_ID` bigint(20) DEFAULT NULL,--记录消费过的消息的id。
  `PRIORITY` bigint(20) NOT NULL DEFAULT '5',
  `XID` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`CONTAINER`,`CLIENT_ID`,`SUB_NAME`,`PRIORITY`),
  KEY `ACTIVEMQ_ACKS_XIDX` (`XID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

2：**activemq_lock**：

在集群环境中才有用，只有一个Broker可以获得消息，称为Master Broker，其他的只能作为备份等待Master Broker不可用，才可能成为下一个Master Broker。这个表用于记录哪个Broker是当前的Master Broker。

3：**activemq_msgs**：用于存储消息，Queue和Topic都存储在这个表中。 主要的数据库字段如下：

```sql
CREATE TABLE `activemq_msgs` (
  `ID` bigint(20) NOT NULL,--自增的数据库主键 
  `CONTAINER` varchar(250) NOT NULL,--消息的destination 
  `MSGID_PROD` varchar(250) DEFAULT NULL,--消息发送者客户端的主键 
  `MSGID_SEQ` bigint(20) DEFAULT NULL,--是发送消息的顺序，msgid_prod+msg_seq可以组成jms的messageid 
  `EXPIRATION` bigint(20) DEFAULT NULL,--消息的过期时间，存储的是从1970-01-01到现在的毫秒数 
  `MSG` longblob,--消息本体的java序列化对象的二进制数据 
  `PRIORITY` bigint(20) DEFAULT NULL,--优先级，从0-9，数值越大优先级越高 
  `XID` varchar(250) DEFAULT NULL,--用于存储订阅关系。如果是持久化topic，订阅者和服务器的订阅关系在这个表保存。
  PRIMARY KEY (`ID`),
  -- 省略其他key的定义
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

再发送消息就会跑到mysql

![1609674195668](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609674195668.png)



### LevelDB存储

 LevelDB持久化性能高于KahaDB，虽然目前默认的持久化方式仍然是KahaDB。并且，在ActiveMQ 5.9版本提供 了基于LevelDB和Zookeeper的数据复制方式，用于Master-slave方式的首选数据复制方案。 但是在ActiveMQ官网对LevelDB的表述：LevelDB官方建议使用以及不再支持，推荐使用的是KahaDB 

### Memory 消息存储

 顾名思义，基于内存的消息存储，就是消息存储在内存中。persistent=”false”,表示不设置持 久化存储，直接存储到内存中 在broker标签处设置。 

### JDBC Message store with ActiveMQ Journal

这种方式克服了JDBC Store的不足，JDBC存储每次消息过来，都需要去写库和读库。 ActiveMQ Journal，使用延迟存储数据到数据库，当消息来到时先缓存到文件中，延迟后才写到数据库中。

当消费者的消费速度能够及时跟上生产者消息的生产速度时，journal文件能够大大减少需要写入到DB中的消息。 举个例子，生产者生产了1000条消息，这1000条消息会保存到journal文件，如果消费者的消费速度很快的情况 下，在journal文件还没有同步到DB之前，消费者已经消费了90%的以上的消息，那么这个时候只需要同步剩余的 10%的消息到DB。 如果消费者的消费速度很慢，这个时候journal文件可以使消息以批量方式写到DB。