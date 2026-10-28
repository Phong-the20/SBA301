package fu.se.lab07;

import fu.se.lab07.dao.StudentDAO;
import fu.se.lab07.entity.Student;
import fu.se.lab07.repository.IStudentRepository;
import fu.se.lab07.repository.StudentRepository;
import fu.se.lab07.service.IStudentService;
import fu.se.lab07.service.StudentService;
import fu.se.lab07.ui.controller.LoginController;
import fu.se.lab07.ui.controller.StudentController;
import fu.se.lab07.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Lab07IntegrationTests {

    private static SessionFactory sessionFactory;
    private static StudentDAO studentDAO;
    private static IStudentRepository repository;
    private static IStudentService service;

    @BeforeAll
    static void setUpAll() {
        sessionFactory = HibernateUtil.buildTestSessionFactory();
        studentDAO = new StudentDAO(sessionFactory);
        repository = new StudentRepository(studentDAO);
        service = new StudentService(repository);
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Test
    void T01_sessionFactory_bootstrap() {
        assertNotNull(sessionFactory, "SessionFactory must not be null");
        assertFalse(sessionFactory.isClosed(), "SessionFactory must be open");
    }

    @Test
    void T02_create_student_success() {
        Student s = new Student("test_create@fu.edu.vn", "Pass123!", "Nguyen", "An", 8.5);
        Student created = service.create(s);

        assertNotNull(created.getId(), "Generated ID must not be null");
        assertEquals("test_create@fu.edu.vn", created.getEmail());
        assertEquals("Nguyen", created.getFirstName());
        assertEquals("An", created.getLastName());
        assertEquals(8.5, created.getMarks());
    }

    @Test
    void T03_create_student_duplicateEmail_throwsIllegalState() {
        Student s1 = new Student("dup@fu.edu.vn", "Pass123!", "Tran", "Binh", 7.0);
        service.create(s1);

        Student s2 = new Student("dup@fu.edu.vn", "OtherPass!", "Le", "Cuong", 9.0);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.create(s2));
        assertTrue(ex.getMessage().contains("Email already registered"));
    }

    @Test
    void T04_create_student_invalidMarks_throwsIllegalArgument() {
        Student sLow = new Student("low@fu.edu.vn", "Pass123!", "Pham", "Dung", -1.0);
        IllegalArgumentException exLow = assertThrows(IllegalArgumentException.class, () -> service.create(sLow));
        assertTrue(exLow.getMessage().contains("between 0 and 10"));

        Student sHigh = new Student("high@fu.edu.vn", "Pass123!", "Pham", "Dung", 10.5);
        IllegalArgumentException exHigh = assertThrows(IllegalArgumentException.class, () -> service.create(sHigh));
        assertTrue(exHigh.getMessage().contains("between 0 and 10"));
    }

    @Test
    void T05_findById_existing_returnsStudent() {
        Student s = service.create(new Student("find_me@fu.edu.vn", "Pass123!", "Hoang", "Em", 9.0));
        Student found = service.getById(s.getId());

        assertNotNull(found);
        assertEquals(s.getId(), found.getId());
        assertEquals("find_me@fu.edu.vn", found.getEmail());
    }

    @Test
    void T06_findById_missing_returnsEmptyOrThrows() {
        Long nonExistentId = 999999L;
        Optional<Student> repoResult = repository.findById(nonExistentId);
        assertTrue(repoResult.isEmpty(), "Repository findById should return Optional.empty for missing ID");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getById(nonExistentId));
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void T07_findAll_returnsAllStudents() {
        List<Student> list = service.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty(), "Student list should contain records");
    }

    @Test
    void T08_update_student_success() {
        Student s = service.create(new Student("update_me@fu.edu.vn", "Pass123!", "Vu", "Giang", 6.5));
        s.setMarks(9.5);
        s.setFirstName("Vuong");

        Student updated = service.update(s);
        assertEquals(9.5, updated.getMarks());
        assertEquals("Vuong", updated.getFirstName());

        Student reloaded = service.getById(s.getId());
        assertEquals(9.5, reloaded.getMarks());
        assertEquals("Vuong", reloaded.getFirstName());
    }

    @Test
    void T09_update_student_missingId_throwsIllegalArgument() {
        Student detachedWithoutId = new Student("noid@fu.edu.vn", "Pass123!", "No", "Id", 7.0);
        assertThrows(IllegalArgumentException.class, () -> service.update(detachedWithoutId));

        detachedWithoutId.setId(888888L);
        assertThrows(IllegalArgumentException.class, () -> service.update(detachedWithoutId));
    }

    @Test
    void T10_delete_student_success() {
        Student s = service.create(new Student("delete_me@fu.edu.vn", "Pass123!", "Dang", "Hai", 7.5));
        Long id = s.getId();

        service.delete(id);
        assertTrue(repository.findById(id).isEmpty(), "Deleted student must no longer exist in repository");
    }

    @Test
    void T11_delete_student_missingId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(777777L));
    }

    @Test
    void T12_login_validCredentials_returnsStudent() {
        service.create(new Student("auth_user@fu.edu.vn", "MySecret123", "Ngo", "Khang", 8.0));
        Student loggedIn = service.login("auth_user@fu.edu.vn", "MySecret123");

        assertNotNull(loggedIn);
        assertEquals("auth_user@fu.edu.vn", loggedIn.getEmail());
        assertEquals("Ngo", loggedIn.getFirstName());
    }

    @Test
    void T13_login_invalidPassword_throwsIllegalArgument() {
        service.create(new Student("auth_wrong@fu.edu.vn", "CorrectPass", "Bui", "Long", 8.0));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("auth_wrong@fu.edu.vn", "WrongPassword"));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void T14_login_nonExistingEmail_throwsIllegalArgument() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("non_existing@fu.edu.vn", "SomePass"));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void T15_controller_architecture_doesNotImportHibernateOrDAO() {
        // Architectural Audit: Controllers must only depend on Service
        for (Field field : StudentController.class.getDeclaredFields()) {
            assertNotEquals("org.hibernate.Session", field.getType().getName(), "StudentController must not have Hibernate Session field");
            assertNotEquals("fu.se.lab07.dao.StudentDAO", field.getType().getName(), "StudentController must not directly reference StudentDAO");
        }
        for (Field field : LoginController.class.getDeclaredFields()) {
            assertNotEquals("org.hibernate.Session", field.getType().getName(), "LoginController must not have Hibernate Session field");
            assertNotEquals("fu.se.lab07.dao.StudentDAO", field.getType().getName(), "LoginController must not directly reference StudentDAO");
        }
    }

    @Test
    void T16_service_layer_handlesAllBusinessLogic() {
        // Validate blank fields in service
        Student blankEmail = new Student("", "123456", "A", "B", 8.0);
        assertThrows(IllegalArgumentException.class, () -> service.create(blankEmail));

        Student blankFirst = new Student("valid@fu.edu.vn", "123456", "", "B", 8.0);
        assertThrows(IllegalArgumentException.class, () -> service.create(blankFirst));

        Student invalidEmail = new Student("invalid-format", "123456", "A", "B", 8.0);
        assertThrows(IllegalArgumentException.class, () -> service.create(invalidEmail));
    }
}
