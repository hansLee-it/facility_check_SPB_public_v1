package hans.firefighting.check.facility_check.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
;

@Configuration
public class SwaggerConfiguration {

	/**
	 * <pre>
	 * 1. 메소드명 : openAPI
	 * 2. 작성일   : 2024. 3. 31.
	 * 3. 작성자   : itHans
	 * 4. 설명     : swagger (/swagger-ui.html)
	 * </pre>
	 * 
	 * @return
	 */

	  @Bean
	  public OpenAPI openAPI() {
	    return new OpenAPI()
	    .info(new Info()
	    .title("Fire Facility Check Service API")
	    .description("Provide check list for fire fighting facility check worker")
	    .version("1.0.0"));
	  }
	
}