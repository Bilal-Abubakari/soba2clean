package com.example.soba2clean.configuration;

import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.enums.Role;
import com.example.soba2clean.service.authentication.AuthenticationService;
import com.example.soba2clean.service.authentication.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class AdminSeederConfig implements ApplicationRunner {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final Logger logger = Logger.getLogger(AdminSeederConfig.class.getName());
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.firstName}")
    private String adminFirstName;
    @Value("${admin.lastName}")
    private String adminLastName;
    @Value("${admin.phoneNumber}")
    private String adminPhoneNumber;
    @Value("${admin.address}")
    private String adminAddress;

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if (seeder.contains("admin")) {
               if (!this.userService.userExistsByEmail(adminEmail)) {
                    seedAdmin();
                } else {
                    logger.info("Admin already exists, skipping seeding");
                }
                logger.info("Success run admin seeder");
            }
        } else {
            logger.info("Admin seeder skipped");
        }
    }

    private void seedAdmin() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail(adminEmail);
        registerDto.setPassword(adminPassword);
        registerDto.setConfirmPassword(adminPassword);
        registerDto.setFirstName(adminFirstName);
        registerDto.setLastName(adminLastName);
        registerDto.setPhoneNumber(adminPhoneNumber);
        registerDto.setAddress(adminAddress);
        authenticationService.register(registerDto, Role.ADMIN);
        logger.info("Admin created successfully");
    }
}
