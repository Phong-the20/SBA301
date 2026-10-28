package fu.se.lab07.ui.controller;

import fu.se.lab07.entity.Student;
import fu.se.lab07.service.IStudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtMarks;

    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TableColumn<Student, Long> colId;

    @FXML
    private TableColumn<Student, String> colEmail;

    @FXML
    private TableColumn<Student, String> colFirstName;

    @FXML
    private TableColumn<Student, String> colLastName;

    @FXML
    private TableColumn<Student, Double> colMarks;

    private final ObservableList<Student> data = FXCollections.observableArrayList();
    private IStudentService studentService;
    private Long selectedStudentId;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));

        tblStudents.setItems(data);
        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> populateForm(selected));
    }

    public void setStudentService(IStudentService service) {
        this.studentService = service;
        refreshTable();
    }

    private void refreshTable() {
        if (studentService != null) {
            data.setAll(studentService.getAll());
        }
    }

    private void populateForm(Student s) {
        if (s == null) return;
        selectedStudentId = s.getId();
        txtEmail.setText(s.getEmail());
        txtPassword.setText(s.getPassword());
        txtFirstName.setText(s.getFirstName());
        txtLastName.setText(s.getLastName());
        txtMarks.setText(Double.toString(s.getMarks()));
    }

    private Student readForm() {
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText() : "";
        String first = txtFirstName.getText() != null ? txtFirstName.getText().trim() : "";
        String last = txtLastName.getText() != null ? txtLastName.getText().trim() : "";
        String marksStr = txtMarks.getText() != null ? txtMarks.getText().trim() : "";

        if (email.isBlank() || password.isBlank() || first.isBlank() || last.isBlank() || marksStr.isBlank()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        double marks;
        try {
            marks = Double.parseDouble(marksStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Marks must be a numeric value!");
        }

        if (marks < 0.0 || marks > 10.0) {
            throw new IllegalArgumentException("Marks must be between 0 and 10!");
        }

        return new Student(email, password, first, last, marks);
    }

    @FXML
    private void handleCreate() {
        try {
            Student s = readForm();
            studentService.create(s);
            refreshTable();
            clearForm();
            showInfo("Student added successfully!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedStudentId == null) {
            showError("Please select a student from the table first!");
            return;
        }
        try {
            Student s = readForm();
            s.setId(selectedStudentId);
            studentService.update(s);
            refreshTable();
            clearForm();
            showInfo("Student updated successfully!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedStudentId == null) {
            showError("Please select a student from the table first!");
            return;
        }
        if (!confirm("Are you sure you want to delete this student (ID: " + selectedStudentId + ")?")) {
            return;
        }
        try {
            studentService.delete(selectedStudentId);
            refreshTable();
            clearForm();
            showInfo("Student deleted successfully!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        selectedStudentId = null;
        txtEmail.clear();
        txtPassword.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtMarks.clear();
        tblStudents.getSelectionModel().clearSelection();
        txtEmail.requestFocus();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private boolean confirm(String message) {
        return new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO)
                .showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }
}
