package va.com.szkal.maymer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "va.com.szkal.maymer")
@EnableScheduling
public class SpringBootTomcatApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootTomcatApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTomcatApplication.class);
    }
}
