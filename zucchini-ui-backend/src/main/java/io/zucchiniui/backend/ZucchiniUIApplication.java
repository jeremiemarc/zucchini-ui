package io.zucchiniui.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    EndpointMBeanExportAutoConfiguration.class,
    JmxAutoConfiguration.class
})
public class ZucchiniUIApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZucchiniUIApplication.class, args);
    }

}
