package com.andersen.config;

import com.andersen.repository_JPA.booking.BookingRepository;
import com.andersen.repository_JPA.user.UserRepository;
import com.andersen.repository_JPA.workspace.WorkspaceRepository;
import com.andersen.service.auth.AuthService;
import com.andersen.service.auth.AuthServiceImpl;
import com.andersen.service.Security.PasswordEncoder;
import com.andersen.service.Security.SessionManager;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Spring application, providing bean definitions
 * for various services used in the application.
 * This class uses Spring's @Configuration annotation to indicate that it contains
 * bean definitions that should be processed by the Spring container.
 */
@Configuration
public class AppConfig {

    /**
     * Defines a bean for the AuthService implementation.
     *
     * @param userRepository  The UserRepository to be injected into the AuthService
     * @param sessionManager  The SessionManager to be injected into the AuthService
     * @param passwordEncoder The PasswordEncoder to be injected into the AuthService
     * @return an instance of AuthService implementation
     */
    @Bean
    public AuthService authService(UserRepository userRepository,
                                   SessionManager sessionManager,
                                   PasswordEncoder passwordEncoder) {
        return new AuthServiceImpl(userRepository, sessionManager, passwordEncoder);
    }

    /**
     * Defines a bean for the BookingService implementation.
     *
     * @param bookingRepository   The BookingRepository to be injected into the BookingService
     * @param workspaceRepository The WorkspaceRepository to be injected into the BookingService
     * @return an instance of BookingService implementation
     */
    @Bean
    public BookingService bookingService(BookingRepository bookingRepository,
                                         WorkspaceRepository workspaceRepository) {
        return new BookingServiceImpl(bookingRepository, workspaceRepository);
    }

    /**
     * Defines a bean for the WorkspaceService implementation.
     *
     * @param workspaceRepository The WorkspaceRepository to be injected into the WorkspaceService
     * @return an instance of WorkspaceService implementation
     */
    @Bean
    public WorkspaceService workspaceService(WorkspaceRepository workspaceRepository) {
        return new WorkspaceServiceImpl(workspaceRepository);
    }

    /**
     * Defines a bean for the SessionManager.
     *
     * @return an instance of SessionManager
     */
    @Bean
    public SessionManager sessionManager() {
        return new SessionManager(); // Instantiate and return a new SessionManager
    }
}