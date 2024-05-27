package com.yasminm.scenes;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserRegistrationSceneController {
    public static Scene CreateScene() throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("user-registration-scene.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene scene = new Scene(root);
        return scene;
    }

    @FXML
    protected TextField tfUsername;

    @FXML
    protected TextField tfName;

    @FXML
    protected TextField tfEmail;

    @FXML
    protected PasswordField pfPassword;

    @FXML
    protected Button btSubmit;
}
