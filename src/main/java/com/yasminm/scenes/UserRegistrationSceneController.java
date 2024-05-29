package com.yasminm.scenes;

import com.yasminm.model.UserData;
import com.yasminm.util.HibernateUtil;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class UserRegistrationSceneController {
    public static Scene CreateScene() throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("user-registration-scene.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene scene = new Scene(root);
        return scene;
    }

    @FXML
    protected Button btBackScene;

    @FXML
    protected TextField tfUsername;

    @FXML
    protected TextField tfFullName;

    @FXML
    protected TextField tfEmail;

    @FXML
    protected PasswordField pfPassword;

    @FXML
    protected PasswordField pfConfirmPassword;


    @FXML
    protected Button btSubmit;

    public String getUsernameInput() {
        return tfUsername.getText();
    }

    public String getNameInput() {
        return tfFullName.getText();
    }

    public String getEmailInput() {
        return tfEmail.getText();
    }

    public String getPasswordInput() {
        return pfPassword.getText();
    }

    public String getConfirmPasswordInput() {
        return pfConfirmPassword.getText();
    }

    public void registerUser(ActionEvent e) {
        String password = getPasswordInput();
        String confirmPassword = getConfirmPasswordInput();

        if(!password.equals(confirmPassword)) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Senhas não coincidem!",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        UserData newUser = new UserData();
        newUser.setUsername(getUsernameInput());
        newUser.setName(getNameInput());
        newUser.setEmail(getEmailInput());
        newUser.setPassword(confirmPassword);

        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(newUser);
        transaction.commit();

        
        try {
            Stage crrStage = (Stage) btSubmit
                    .getScene().getWindow();
                crrStage.close();
                
            Stage stage = new Stage();
            Scene scene = HomeSceneController.CreateScene(newUser);
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    public void tryBackScene() {
        try {
            Stage crrStage = (Stage) btBackScene
                .getScene().getWindow();
            crrStage.close();

            Stage stage = new Stage();
            Scene scene = WelcomeSceneController.CreateScene();
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela Welcome. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }
}
