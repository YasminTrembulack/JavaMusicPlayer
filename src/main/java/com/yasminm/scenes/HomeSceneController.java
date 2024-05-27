package com.yasminm.scenes;

import com.yasminm.model.UserData;

import java.net.URL;
import org.hibernate.HibernateException;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

        // controller.set

        return scene;
    }

    @FXML
    protected ImageView ivMusicImage;

    @FXML
    protected ScrollPane sp;

    @FXML
    protected AnchorPane ap;

    @FXML
    protected Label lbMusicTitle;

    @FXML
    protected Label lbAlbumAndArtist;

    @FXML
    protected ProgressBar pb;

    @FXML
    protected Button btPlayButton;

    @FXML
    protected Button btNext;

    @FXML
    protected Button btPrevious;

    @FXML
    protected Label lbUsername;
}
