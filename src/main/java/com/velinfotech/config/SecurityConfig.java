package com.velinfotech.config;

import com.velinfotech.security.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpStatus;

/**
 * Splits the API into what the public website needs and what only the admin console
 * should reach.
 *
 * The rule of thumb: anonymous visitors may read published content and submit their
 * own details, but may never read anyone else's. Every list of submissions —
 * applications, internships, feedback, course enquiries — contains names, emails and
 * phone numbers, so all of them require a token.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // Tokens carry the identity; there is no server-side session to keep.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Answer 401 rather than redirecting to a login page that does not exist.
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .authorizeHttpRequests(auth -> auth
                        // When a controller throws, Spring forwards to /error and that
                        // forward re-enters this chain. Without this it matches nothing,
                        // gets denied, and a plain 400 comes back to the caller as a 401
                        // or 403 — which is exactly what made earlier failures here look
                        // like permission problems instead of the bugs they were.
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()

                        // CORS preflight carries no credentials by design.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Infrastructure and docs
                        .requestMatchers("/", "/api/health").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Signing in
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").permitAll()

                        /* ---------- Published content the website reads ---------- */

                        .requestMatchers(HttpMethod.GET, "/api/blogposts", "/api/blogposts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/job-posts", "/api/job-posts/*").permitAll()
                        // Single wildcard on purpose: /api/consulting/job-posts/{id} is public,
                        // but /api/consulting/job-posts/{id}/applications must not be.
                        .requestMatchers(HttpMethod.GET, "/api/consulting/job-posts", "/api/consulting/job-posts/*").permitAll()

                        /* ---------- Forms a visitor submits about themselves ---------- */

                        .requestMatchers(HttpMethod.POST, "/api/jobs/*/apply", "/api/jobs/*/applications").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/consulting/job-posts/*/apply").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/registrations").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/feedbacks").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/internships").permitAll()

                        /* ---------- Everything else is admin ---------- */
                        // Covers every write, and every list of personal data:
                        // /api/jobs/applications, /api/consulting/applications,
                        // /api/internships, /api/feedbacks, /api/registrations.
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().denyAll()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException(username);
        };
    }
}
