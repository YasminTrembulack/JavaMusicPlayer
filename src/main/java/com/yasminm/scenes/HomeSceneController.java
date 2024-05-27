package com.yasminm.scenes;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HomeSceneController {
    public static Scene CreateScene() throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("home-scene.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene scene = new Scene(root);
        return scene;
    }
}
