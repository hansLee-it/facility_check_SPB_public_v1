package hans.firefighting.check.facility_check.db;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DefaultConMapper {
    String value() default "";
}
