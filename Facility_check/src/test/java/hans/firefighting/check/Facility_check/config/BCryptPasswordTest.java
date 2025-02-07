package hans.firefighting.check.Facility_check.config;

import org.junit.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordTest {
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    public void stringBcrypt(){

        String password = "{PASSWORD_to_ENCODE}";

        System.out.println("Password:" + password);
        System.out.println("EncodedPassword:" + passwordEncoder().encode(password));
    }
}
