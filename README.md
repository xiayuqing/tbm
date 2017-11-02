# tbm
##Log collector

### Usage

maven dependency
```xml
<dependency>
   <groupId>org.tbm</groupId>
   <artifactId>tbm-client</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>
```

import tbm client and start
```xml
    <bean id="tbmClient" class="org.tbm.client.ClientAgentStartup" init-method="start">
        <property name="location" value="classpath:tbm-config.cfg"/>
        <property name="ignoreResourceNotFound" value="true"/>
    </bean>
```



add log4j appender
```properties
og4j.rootLogger=INFO,rolling,errlog,stdout,tbm
...
#tbm
log4j.appender.tbm=org.tbm.client.log4tbm.TbmLog4jAppender
...
```

add client config file:tbm-config.cfg
```properties
### 一台物理机同一system.id只能部署一份,否则会造成日志收集器无法区分日志来源
system.id=123
host=127.0.0.1
port=9411
# jvm数据监控是否启用
jvm.stat.enable=true
# 统计频率,单位秒
jvm.stat.period=30
# 线程池大小
jvm.stat.executor.size=5

### netty config
io.ratio=70
frame.length.max=32768
so.send.buf=32768
so.receive.buf=32768
# 写空闲时间,秒.
idle.write.time=30
# 读空闲时间,秒.
idle.read.time=40
```


