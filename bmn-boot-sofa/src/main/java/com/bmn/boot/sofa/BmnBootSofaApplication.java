package com.bmn.boot.sofa;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.bmn.boot.sofa.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource({ "classpath*:rpc-sofa-boot-starter-samples.xml" })
@SpringBootApplication
public class BmnBootSofaApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BmnBootSofaApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

        HelloService helloSyncServiceReference = (HelloService) applicationContext
            .getBean("helloServiceReference");

        System.out.println(helloSyncServiceReference.saySync("sync"));

//        ProviderConfig config;
//        config.export();
    }

}
