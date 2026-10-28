package fu.se.lab07.service;

import fu.se.lab07.entity.Student;
import fu.se.lab07.repository.IStudentRepository;

import java.util.List;
import java.util.Optional;

public class StudentService implements IStudentService {

    private final IStudentRepository repository;

    public StudentService(IStudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student create(Student student) {
        validate(student);
        repository.findByEmail(student.getEmail()).ifPresent(existing -> {
            throw new IllegalStateException("Email already registered: " + student.getEmail());
        });
        return repository.save(student);
    }

    @Override
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Student getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }

    @Override
    public Optional<Student> getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        return repository.findByEmail(email.trim());
    }

    @Override
    public Student update(Student student) {
        validate(student);
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student ID cannot be null for update");
        }
        // Verify student exists
        getById(student.getId());

        // Check if email belongs to another student
        repository.findByEmail(student.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(student.getId())) {
                throw new IllegalStateException("Email already registered by another student: " + student.getEmail());
            }
        });

        return repository.update(student);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        getById(id);
        if (!repository.delete(id)) {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
    }

    @Override
    public Student login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }
        Student student = repository.findByEmail(email.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return student;
    }

    private void validate(Student s) {
        if (s == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (s.getEmail() == null || s.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!s.getEmail().contains("@") || !s.getEmail().contains(".")) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        if (s.getPassword() == null || s.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (s.getFirstName() == null || s.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (s.getLastName() == null || s.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (s.getMarks() < 0.0 || s.getMarks() > 10.0) {
            throw new IllegalArgumentException("Marks must be between 0 and 10");
        }
    }
}
