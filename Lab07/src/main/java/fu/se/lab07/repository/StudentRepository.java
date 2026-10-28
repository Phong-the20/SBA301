package fu.se.lab07.repository;

import fu.se.lab07.dao.StudentDAO;
import fu.se.lab07.entity.Student;

import java.util.List;
import java.util.Optional;

public class StudentRepository implements IStudentRepository {

    private final StudentDAO dao;

    public StudentRepository(StudentDAO dao) {
        this.dao = dao;
    }

    @Override
    public Student save(Student student) {
        return dao.save(student);
    }

    @Override
    public List<Student> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public Student update(Student student) {
        return dao.update(student);
    }

    @Override
    public boolean delete(Long id) {
        return dao.delete(id);
    }
}
