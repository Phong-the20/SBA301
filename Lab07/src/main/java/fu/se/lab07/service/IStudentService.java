package fu.se.lab07.service;

import fu.se.lab07.entity.Student;
import java.util.List;
import java.util.Optional;

public interface IStudentService {
    Student create(Student student);
    List<Student> getAll();
    Student getById(Long id);
    Optional<Student> getByEmail(String email);
    Student update(Student student);
    void delete(Long id);
    Student login(String email, String password);
}
