package com.laporeon.expensetracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Expense Tracker API")
                                .description("""
                                          A REST API for personal finance management.
                                          
                                          ## Features
                                          - Expense tracking with categories
                                          - User profile management
                                          - Soft delete support
                                          - Pagination and sorting
                                          
                                          ## Authentication
                                          This API uses **JWT (JSON Web Token)** for authentication.
                                  
                                          ### How to use:
                                          1. Register at `/api/v1/auth/register` or login with an existing user at `/api/v1/auth/login`
                                          2. Copy the returned JWT token (it starts with `eyJ...`)
                                          3. Click on the **Authorize** button (🔒) at the top of this page
                                          4. Paste the **FULL** token into the field
                                          5. Click **Authorize** and then **Close**
                                          6. All protected endpoints will now show a closed lock 🔒
                                          """)
                                .version("1.0.0")
                                .license(
                                        new License()
                                                .name("MIT")
                                                .url("https://opensource.org/licenses/MIT")
                                )
                )
                .components(new Components()
                                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                                            .description("""
                        **JWT Token Authentication**
                        
                        1. POST to `/api/v1/auth/login`
                        2. Copy `token` from response
                        3. Paste here (without `Bearer ` prefix)
                        
                        ```
                        eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                        ```
                        """)));
    }
}
