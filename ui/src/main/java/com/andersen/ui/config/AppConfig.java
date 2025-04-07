package com.andersen.ui.config;

import com.andersen.domain.repository.booking.BookingRepository;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import com.andersen.service.auth.AuthService;
import com.andersen.service.auth.AuthServiceImpl;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.security.PasswordEncoder;
import com.andersen.service.security.SessionManager;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.andersen.ui.controller",
        "com.andersen.service",
        "com.andersen.domain.repository" ,
        "com.andersen.domain.logger"
})
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public AuthService authService(UserRepository userRepository,
                                   SessionManager sessionManager,
                                   PasswordEncoder passwordEncoder) {
        return new AuthServiceImpl(userRepository, sessionManager, passwordEncoder);
    }

    @Bean
    public BookingService bookingService(BookingRepository bookingRepository,
                                         WorkspaceRepository workspaceRepository) {
        return new BookingServiceImpl(bookingRepository, workspaceRepository);
    }

    @Bean
    public WorkspaceService workspaceService(WorkspaceRepository workspaceRepository) {
        return new WorkspaceServiceImpl(workspaceRepository);
    }

    @Bean
    public SessionManager sessionManager() {
        return new SessionManager();
    }
}