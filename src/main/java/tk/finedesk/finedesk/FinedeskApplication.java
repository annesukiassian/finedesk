package tk.finedesk.finedesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@EnableJpaRepositories("tk.finedesk.finedesk.repositories")
//@EntityScan("tk.finedesk.finedesk.entities")
@SpringBootApplication
public class FinedeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinedeskApplication.class, args);
    }
}
