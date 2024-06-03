package com.yasminm.scenes;

import com.yasminm.model.UserData;
import com.yasminm.util.Authentication;

import java.net.URL;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class LoginSceneController {
    
    public static Scene CreateScene() throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("login-scene.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene scene = new Scene(root);
        return scene;
    }

    @FXML
    protected TextField tfUsername;

    @FXML
    protected PasswordField pfPassword;

    @FXML
    protected Button btSubmit;

    @FXML
    protected Button btBackScene;

    @FXML
    protected CheckBox cbPassword;

    @FXML
    protected TextField tfPassword;

    public void tryLogin(ActionEvent e) {
        UserData user = Authentication.authenticateUser(
            getUsernameInput(), 
            getPasswordInput())
            .getUser();

        if(user != null) {
            Stage crrStage = (Stage) btSubmit
                .getScene().getWindow();
            crrStage.close();

            try {
                Stage stage = new Stage();
                Scene scene = HomeSceneController.CreateScene(user);
                stage.setScene(scene);
                stage.show();
            } 
            catch (Exception ex) {
                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Erro ao processar a tela de HOME. Consulte o apoio de TI",
                        ButtonType.OK);
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Usuário não registrado!",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }

    public void togglevisiblePassword(ActionEvent event) {
        if (cbPassword.isSelected()) {
            tfPassword.setText(pfPassword.getText());
            tfPassword.setVisible(true);
            pfPassword.setVisible(false);
            return;
        }
        pfPassword.setText(tfPassword.getText());
        pfPassword.setVisible(true);
        tfPassword.setVisible(false);
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

    // ---- GETTERS ---- 
    public String getUsernameInput() {
        return tfUsername.getText();
    }

    public String getPasswordInput() {
        return pfPassword.getText();
    }
}
