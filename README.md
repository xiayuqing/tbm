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