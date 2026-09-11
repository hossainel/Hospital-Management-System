package com.hospital.hms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementSystemApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("Hospital Management System (HMS) Spring Boot application online!");
        System.out.println("Local clinical web portal: http://localhost:8080");
        System.out.println("Legacy Flat File system auto-seeding routine complete.");
        System.out.println("=================================================================");
    }
}
