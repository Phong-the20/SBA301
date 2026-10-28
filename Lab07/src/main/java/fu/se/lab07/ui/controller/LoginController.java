package fu.se.lab07.ui.controller;

import fu.se.lab07.entity.Student;
import fu.se.lab07.service.IStudentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    private IStudentService studentService;

    public void setStudentService(IStudentService service) {
        this.studentService = service;
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText() : "";

        if (email.isBlank() || password.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Email and password cannot be empty!", ButtonType.OK).showAndWait();
            return;
        }

        try {
            Student loggedIn = studentService.login(email, password);
            openStudentManagement(loggedIn);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        if (txtEmail.getScene() != null && txtEmail.getScene().getWindow() != null) {
            ((Stage) txtEmail.getScene().getWindow()).close();
        }
    }

    private void openStudentManagement(Student loggedIn) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fu/se/lab07/ui/student-view.fxml"));
        Parent root = loader.load();

        StudentController controller = loader.getController();
        controller.setStudentService(studentService);

        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.setScene(new Scene(root, 820, 650));
        stage.setTitle("Manage Student - Logged in as: " + loggedIn.getFirstName() + " " + loggedIn.getLastName());
        stage.centerOnScreen();
        stage.show();
    }
}
