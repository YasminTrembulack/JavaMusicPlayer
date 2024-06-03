package com.yasminm.scenes;


import com.yasminm.model.UserData;
import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.util.HibernateUtil;

import javafx.event.EventHandler;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.border.LineBorder;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;

public class HomeDeleteSceneController {
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = HomeDeleteSceneController.class.getResource("home-delete-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        HomeDeleteSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        controller.setCurrentUser(user);
        controller.buildMusicDisplay(controller, user);
        controller.setPaneListeners(controller);

        return scene;
    }

    public void buildMusicDisplay(HomeDeleteSceneController controller, UserData user) {

        List<UserCollection> collection = getUserCollectionFromDB(user);

        for (int i = 0; i < collection.size(); i++) {

            MusicData music = getMusicFromDB(collection.get(i).getMusicid());

            Pane p = new Pane();
            p.setPrefSize(615, 84);

            VBox vb = new VBox();
            vb.setPadding(new Insets(20, 20, 20, 20));

            Label lbTitle = new Label(music.getTitle());
            lbTitle.setStyle("-fx-font-weight: bold");

            Label lbArtist = new Label(music.getArtist() + " - " + music.getAlbum());

            vb.getChildren().add(lbTitle);
            vb.getChildren().add(lbArtist);

            p.getChildren().add(vb);

            controller.vbAllMusic.getChildren().add(p);
        }
    }

    public void setPaneListeners(HomeDeleteSceneController controller) {
        controller.vbAllMusic.getChildren().forEach(node -> {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getTarget() instanceof Pane) {
                            pane.setStyle("-fx-background-color: #fdfdfd");
                        }
                    }
                });
                pane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pane.setStyle("-fx-background-color: #f0f0f0");
                    }
                });
            }
        });
    }

    public MusicData getMusicFromDB(Long musicid) {
        // .. create session to consult database ..
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // .. gets user collection data from database ..
        Query query = session.createQuery("from MusicData m where m.id = :musicid");
        query.setParameter("musicid", musicid);
        List<MusicData> music = query.list();

        if(music.size() <= 0) {
            System.out.println("Error collecting data from database ;/");
            return null;
        }

        MusicData m = (MusicData) music.get(0);

        transaction.commit();

        return m;
    }

    public List<UserCollection> getUserCollectionFromDB(UserData user) {
        // .. create session to consult database ..
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // .. gets user collection data from database ..
        Query query = session.createQuery("from UserCollection u where u.userid = :userid");
        query.setParameter("userid", user.getId());

        List<UserCollection> l = query.list();

        if(l.size() <= 0) {
            System.out.println("Error collecting data from database ;/");
            return null;
        }

        transaction.commit();

        return l;
    }


    @FXML
    private VBox vbAllMusic;


    private UserData currentUser;

    public VBox getVbAllMusic() {
        return vbAllMusic;
    }

    public UserData getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserData currentUser) {
        this.currentUser = currentUser;
    }

    
}
