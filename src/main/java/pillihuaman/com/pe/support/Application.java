package pillihuaman.com.pe.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan; // <-- AÑADIR IMPORT
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;

import java.util.Collections;

@EnableAsync
@EnableScheduling
@Import(CustomRestExceptionHandlerGeneric.class)
@SpringBootApplication(scanBasePackages = {"pillihuaman.com.pe.support"})
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ConfigurationPropertiesScan("pillihuaman.com.pe.support") // <-- AÑADIR ESTA ANOTACIÓN
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        // Ya no es necesario establecer el puerto aquí si está en application.properties
        // app.setDefaultProperties(Collections.singletonMap("server.port", "8091"));

        ApplicationContext context = app.run(args);

        // El código para imprimir los mappings está bien para depuración.
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                context.getBean(RequestMappingHandlerMapping.class);
        System.out.println("--- Registered URL Mappings ---");
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
            System.out.println("Path: " + key);
        });
        System.out.println("---------------------------------");
        System.out.println("Application 'support' started successfully!");
    }
}