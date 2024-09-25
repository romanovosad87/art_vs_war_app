package com.example.artvswar;

import com.example.artvswar.service.EmailExternalService;
import com.example.artvswar.service.impl.EmailExternalServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArtVsWarApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ArtVsWarApplication.class, args);
        System.out.println(context.getBean(EmailExternalService.class));
        Arrays.stream(context.getBeanDefinitionNames())
                .map(name -> context.getBean(name).getClass())
                .forEach(System.out::println);
    }
}
