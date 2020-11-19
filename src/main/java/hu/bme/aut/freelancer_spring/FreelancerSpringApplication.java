package hu.bme.aut.freelancer_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FreelancerSpringApplication{

    public static void main(String[] args) {
        SpringApplication.run(FreelancerSpringApplication.class, args);
    }

}