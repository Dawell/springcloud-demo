该项目配合博客dawell.cc提到的微服务组件的演示项目

与这个demo项目的配套启动配置在[Dawell/springcloud-demo-config](https://github.com/Dawell/springcloud-demo-config)项目中，启动需要2个项目配合

项目是可以直接用main or maven启动的，除了个别项目启动需要修改的配置
1. service-task：修改mysql链接配置（配置在config项目中）
1. service-admin：修改mail配置（配置在config项目中）
1. 依赖与本地zk与kafka，详细搭建可以查看博客

项目中的子项目启动顺序为：eureka-service、service-config、其他服务

其中演示包含了一下SpringCloud组件的功能
1. 注册中心Eureka
1. 配置中心Config
1. 链路追踪Slenth、Zipkin
1. 消息总线Bus
1. API网关Zuul
1. 异步消息Stream、kafka
1. 同步调用Feign、Ribbon
1. 断路降级Hystrix、Turbine
1. 监控管理Admin
1. 临时任务Task

具体配置详解参考博客：[点我阅读](http://dawell.cc/2018/08/01/20180801SpringCloud/)

联系作者 邮箱：dawell@aliyun.com