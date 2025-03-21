package pillihuaman.com.pe.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;


import java.util.Collections;




@EnableAsync
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableScheduling
@Import(CustomRestExceptionHandlerGeneric.class)
@SpringBootApplication(scanBasePackages = {
        "pillihuaman.com.pe.lib","pillihuaman.com.pe.basebd",
        "pillihuaman.com.pe.security",   "pillihuaman.com.pe.security.config",
        "pillihuaman.com.pe.support"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8091"));
       // app.run(args);
        ApplicationContext context = app.run(args);
        // Print all URL mappings
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                context.getBean(RequestMappingHandlerMapping.class);
        System.out.println("Registered URL Mappings:");
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
            System.out.println("Path: " + key + " -> Method: " + value.getMethod().getName());
        });

        System.out.println("Application started successfully!");
    }


}

