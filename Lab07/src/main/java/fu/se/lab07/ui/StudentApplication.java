package fu.se.lab07.ui;

import fu.se.lab07.dao.StudentDAO;
import fu.se.lab07.repository.IStudentRepository;
import fu.se.lab07.repository.StudentRepository;
import fu.se.lab07.service.IStudentService;
import fu.se.lab07.service.StudentService;
import fu.se.lab07.ui.controller.LoginController;
import fu.se.lab07.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

public class StudentApplication extends Application {

    private IStudentService studentService;

    @Override
    public void init() {
        // Composition Root: initialize persistence & services
        SessionFactory sf = HibernateUtil.getSessionFactory();
        StudentDAO dao = new StudentDAO(sf);
        IStudentRepository repo = new StudentRepository(dao);
        this.studentService = new StudentService(repo);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fu/se/lab07/ui/LoginGUI.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setStudentService(studentService);

        stage.setScene(new Scene(root, 420, 320));
        stage.setTitle("Library Department - Login");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
