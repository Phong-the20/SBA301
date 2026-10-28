package fu.se.lab07.app;

import fu.se.lab07.dao.StudentDAO;
import fu.se.lab07.entity.Student;
import fu.se.lab07.repository.IStudentRepository;
import fu.se.lab07.repository.StudentRepository;
import fu.se.lab07.service.IStudentService;
import fu.se.lab07.service.StudentService;
import fu.se.lab07.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class ConsoleSmokeTest {
    public static void main(String[] args) {
        System.out.println("=== Lab 07: Console Smoke Test (Hibernate Core & Layered Architecture) ===");
        SessionFactory sf = HibernateUtil.getSessionFactory();
        StudentDAO dao = new StudentDAO(sf);
        IStudentRepository repo = new StudentRepository(dao);
        IStudentService service = new StudentService(repo);

        String email = "lab07@fpt.edu.vn";
        Student student = repo.findByEmail(email).orElseGet(() -> {
            Student s = new Student(email, "123456", "Lab", "Seven", 8.0);
            return service.create(s);
        });
        System.out.println("Created/Existing student ID: " + student.getId());

        System.out.println("Testing Login with valid credentials:");
        Student loggedIn = service.login(email, "123456");
        System.out.println("Login Success: " + loggedIn.getFirstName() + " " + loggedIn.getLastName());

        System.out.println("Listing all students from Database:");
        service.getAll().forEach(System.out::println);

        System.out.println("=== Console Smoke Test PASS! ===");
        HibernateUtil.shutdown();
    }
}
