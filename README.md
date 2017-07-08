# tbm
##Log collector

待办: 
   ~~operation 配置内容改成数组,修改OperationManager存储方式,OpsFactory中初始化所有 Op 类,初始化的时候注入其对应的所有的Operation~~

   ~~SqlExecutor增加异步~~

   增加ConnectionManager



##config sample
tbm config
// 因为没有绑定端口,一台物理机只能部署一个system.id
// client  side config
system.id=123
host=127.0.0.1
port=9411
jvm.stat.enable=false
jvm.stat.period=30 // TimeUnit.SECONDS
jvm.stat.executor.size=5

//  netty config
io.ratio=70  Io任务比重,default 70
frame.length.max=8192
so.send.buf=32768
so.receive.buf=32768   32 * 1024
// 写空闲时间,秒.
idle.write.time=30
// 读空闲时间,秒.
idle.read.time=40

// server side
datasource.driver=com.jdbc.mysql.Driver
datasource.username=root
datasource.password=123
datasource.url=
datasource.initial.size=10
datasource.min.idle=5
datasource.max.active=30
datasource.filter=stat
datasource.max.wait=60000
datasource.eviction.time=60000
datasource.prepared.statement= true
datasource.ps.conn.size=30 // setMaxPoolPreparedStatementPerConnectionSize

collector.executor.size=30
collector.executor.shutdown.wait=60000   线程池延迟关闭最长等待时间,ms.-1则立即停止

// ShardingUnits:HOUR,DAY,MONTH,YEAR,HASH
sharding.memory.pool=DAY
sharding.memory.summary=DAY
sharding.thread=DAY
sharing.classload=DAY
sharding.business=DAY
