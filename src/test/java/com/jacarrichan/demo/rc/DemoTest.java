package com.jacarrichan.demo.rc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
/**  指定当前生效的配置文件( active profile)，如果是 appplication-dev.yml 则 dev   **/
@ActiveProfiles("dev")
/** 指定  @SpringBootApplication  启动类 和 端口  **/
@SpringBootTest(classes = DemoTest.ConfigContext.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class DemoTest {

    public String applicationName;

    @Value("${spring.application.name}")
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Test
    public void test1() {
        System.out.println(applicationName);

    }

    @Configuration()
    @EnableAutoConfiguration()
    @ComponentScan(value = {"com.jacarrichan.demo"})
    static class ConfigContext {
    }
}