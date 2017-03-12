#  学习 rpc 和 netty 的远程服务调用框架
### 暂时提供的功能
 - 底层是使用Netty框架进行通信，提供protobuf序列化方式
 - spring 结合实现远程调用，通过自定义标签实现无入侵代码
 - 新增zookeeper 实现服务发现
 - 提供provider 服务端线程个数和连接数配置
 - 客户端调用超时和重试
 - 多个服务端的负载均衡，随机从多个服务提供者获取一个
 - 客户端启动的时缓存服务端的信息,实现服务订阅
 - 客户端监听服务端节点,服务端宕机或者停止服务后，客户端能感知。
 - 心跳检测，定时检测prvider 服务是否还存在

 ### todo
 - 注解方式，启动服务。

### 例子

#### 服务端
         <bean id="storeService"  class="com.zhaopeng.demo.provider.StoreServiceImpl" scope="prototype"></bean>
         <eagle:registry address="127.0.0.1:2181"></eagle:registry>
         <eagle:application port="8080" protocol="eagle" accepts="20"></eagle:application>
          <eagle:service interfaceName="com.zhaopeng.demo.api.StoreService" ref="storeService"></eagle:service>

#### 客户端

        <eagle:application port="8080" protocol="eagle" accepts="20"></eagle:application>
        <eagle:registry address="127.0.0.1:2181"></eagle:registry>
        <eagle:reference id="storeService" interface="com.zhaopeng.demo.api.StoreService"></eagle:reference>




