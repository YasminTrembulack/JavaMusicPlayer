package com.yasminm.scenes;

import java.net.URL;

// import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class WelcomeSceneController {
    
    public static Scene CreateScene() throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("welcome-scene.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene scene = new Scene(root);
        return scene;
    }

 
    @FXML
    protected Button btLogin;

    @FXML
    protected Button btCrateAccount;


    public void goToLogin() {

        try {
            Stage stage = new Stage();
            Scene scene = LoginSceneController.CreateScene();
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela Login. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    public void goToCreateAccount() {

        try {
            Stage stage = new Stage();
            Scene scene = UserRegistrationSceneController.CreateScene();
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela Create Account. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

}
