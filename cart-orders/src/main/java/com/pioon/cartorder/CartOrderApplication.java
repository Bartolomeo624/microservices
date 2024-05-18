package com.pioon.cartorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartOrderApplication.class, args);
    }

}