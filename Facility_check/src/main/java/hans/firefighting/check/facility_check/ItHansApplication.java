package hans.firefighting.check.facility_check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class ItHansApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(ItHansApplication.class, args);
	}

}
