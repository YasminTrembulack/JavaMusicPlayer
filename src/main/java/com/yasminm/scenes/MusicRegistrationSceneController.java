package com.yasminm.scenes;

import java.net.URL;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.model.UserData;
import com.yasminm.util.HibernateUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MusicRegistrationSceneController {
    
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = MusicRegistrationSceneController.class.getResource("music-registration-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        MusicRegistrationSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        controller.user = user;

        return scene;
    }
 
    public UserData user;

    @FXML
    protected Button btAddMusic;

    @FXML
    protected Button btBackScene;

    @FXML
    protected TextField tfAlbum;

    @FXML
    protected TextField tfArtist;

    @FXML
    protected TextField tfDirPath;

    @FXML
    protected TextField tfImgPath;

    @FXML
    protected TextField tfTitle;


    public String getTfAlbum() {
        return tfAlbum.getText();
    }

    public void setTfAlbum(TextField tfAlbum) {
        this.tfAlbum = tfAlbum;
    }

    public String getTfArtist() {
        return tfArtist.getText();
    }

    public void setTfArtist(TextField tfArtist) {
        this.tfArtist = tfArtist;
    }

    public String getTfDirPath() {
        return tfDirPath.getText();
    }

    public void setTfDirPath(TextField tfDirPath) {
        this.tfDirPath = tfDirPath;
    }

    public String getTfImgPath() {
        return tfImgPath.getText();
    }

    public void setTfImgPath(TextField tfImgPath) {
        this.tfImgPath = tfImgPath;
    }

    public String getTfTitle() {
        return tfTitle.getText();
    }

    public void setTfTitle(TextField tfTitle) {
        this.tfTitle = tfTitle;
    }

    public void tryAddMusic(){

        UserCollection newCollection = new UserCollection();

        MusicData newMusic = new MusicData();
        newMusic.setTitle(getTfTitle());
        newMusic.setAlbum(getTfAlbum());
        newMusic.setArtist(getTfArtist());
        newMusic.setDirectoryPath(getTfDirPath());
        newMusic.setImagePath(getTfImgPath());
        

        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(newMusic);

        transaction.commit();

        session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        transaction = session.beginTransaction();

        Query query = session.createQuery("from MusicData m where m.title = :music_title and m.artist = :music_artist and m.album = :music_album");
        query.setParameter("music_title", getTfTitle());
        query.setParameter("music_artist", getTfArtist());
        query.setParameter("music_album", getTfAlbum());

        List<MusicData> music = query.list();

        newCollection.setMusicid(music.get(0).getId());
        newCollection.setUserid(user.getId());

        session.save(newCollection);
        
        transaction.commit();

        Alert alert = new Alert(
                    AlertType.INFORMATION,
                    "Musica adicionada com sucesso!",
                    ButtonType.OK);
            alert.showAndWait();
    }

    public void tryBackScene() {
        try {
            Stage crrStage = (Stage) btBackScene
                .getScene().getWindow();
            crrStage.close();

            Stage stage = new Stage();
            Scene scene = HomeSceneController.CreateScene(user);
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela Home. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }
}
