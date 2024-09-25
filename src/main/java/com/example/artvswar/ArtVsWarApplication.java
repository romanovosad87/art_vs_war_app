package com.example.artvswar;

import java.util.Arrays;
import com.example.artvswar.service.EmailExternalService;
import com.example.artvswar.service.impl.EmailExternalServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArtVsWarApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ArtVsWarApplication.class, args);
//        Arrays.stream(context.getBe).forEach(System.out::println);
//        Arrays.stream(context.getBeanDefinitionNames())
//                .map(name -> context.getBean(name).getClass())
//                .forEach(System.out::println);
//        System.out.println(context.getBean(EmailExternalServiceImpl.class));
        System.out.println(context.getBean(EmailExternalService.class));
    }
}
