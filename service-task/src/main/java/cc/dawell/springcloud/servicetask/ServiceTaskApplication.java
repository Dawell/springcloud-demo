package cc.dawell.springcloud.servicetask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@EnableTask
@SpringBootApplication
public class ServiceTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> System.out.println("Hello World!");
    }

    @Bean
    public CommandLineRunner commandLineRunner2() {
        return strings -> System.out.println("Hello 2!");
    }

}
