## Curl ![](https://img.shields.io/badge/JDK-1.8+-green.svg)
Web 项目中，将前端请求以Curl的形式在日志中打印出来。方便后端调试。

## 使用
添加依赖
```xml
<repository>
    <id>liMao</id>
    <url>https://maven.shinianwangluo.com/repository/public/</url>
</repository>
```

```xml
<dependency>
    <groupId>top.banner.lib</groupId>
    <artifactId>curl</artifactId>
    <version>1.1</version>
</dependency>
```

## 配置方式
```java
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CurlFilter> curlFilterRegistration() {
        FilterRegistrationBean<CurlFilter> registration = new FilterRegistrationBean<>();
        //添加过滤器
        registration.setFilter(new CurlFilter());
        //设置过滤路径，/*所有路径
        registration.addUrlPatterns("/*");
        registration.setName("curlFilter");
        //设置优先级
        registration.setOrder(Integer.MAX_VALUE);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
```

## 效果
```shell
2021-11-24 15:12:02.291  INFO 5552 --- [http-nio-9910-exec-6] top.banner.lib.curl.CurlFilter           : curl -X GET -H 'host:www.banner.top' -H 'x-real-ip:116.22.142.128' -H 'x-forwarded-for:116.22.142.128' -H 'x-forwarded-host:www.banner.top' -H 'x-forwarded-proto:http' -H 'connection:close' -H 'token:79dc67e4d57d441192a6d424bcbc5fed' -H 'accept-encoding:gzip' -H 'user-agent:okhttp/3.13.1' -d '' 'https://www.banner.top/api/exchange/list'
2021-11-24 15:15:41.621  INFO 5552 --- [http-nio-9910-exec-7] top.banner.lib.curl.CurlFilter           : curl -X GET -H 'host:www.banner.top' -H 'x-real-ip:116.22.142.128' -H 'x-forwarded-for:116.22.142.128' -H 'x-forwarded-host:www.banner.top' -H 'x-forwarded-proto:http' -H 'connection:close' -H 'token:79dc67e4d57d441192a6d424bcbc5fed' -H 'accept-encoding:gzip' -H 'user-agent:okhttp/3.13.1' -d '' 'https://www.banner.top/api/paymentMethod/all'

```