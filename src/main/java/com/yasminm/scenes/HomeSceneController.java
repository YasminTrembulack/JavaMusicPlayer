package com.yasminm.scenes;

import com.yasminm.model.UserData;

import java.net.URL;
import org.hibernate.HibernateException;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class HomeSceneController {
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = LoginSceneController.class.getResource("home-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        HomeSceneController controller = loader.getController();
        Scene scene = new Scene(loader.load());
        return scene;


    }

    @FXML
    public ImageView imagePath;

    // @FXML
    // public String imagePath;
}
