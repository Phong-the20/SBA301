package fu.edu.sba301.slot38;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Slot38Application {

    public static void main(String[] args) {
        SpringApplication.run(Slot38Application.class, args);
        System.out.println("==================================================================");
        System.out.println("   SBA301 - Slot 38: DevOps, CI/CD, DevTools, JDWP Remote Debug");
        System.out.println("   LiveReload port: 35729 | Actuator: http://localhost:8080/actuator");
        System.out.println("==================================================================");
    }
}
