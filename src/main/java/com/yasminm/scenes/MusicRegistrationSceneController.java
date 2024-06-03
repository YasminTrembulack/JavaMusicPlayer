package com.yasminm.scenes;

import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.model.UserData;
import com.yasminm.util.HibernateUtil;

import java.net.URL;
import java.util.List;
import org.hibernate.Query;
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

    private UserData user;

    public void tryAddMusic(){
        UserCollection newCollection = new UserCollection();

        // ..creates object with user input data..
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

        // ..saves object in db..
        session.save(newMusic);

        // ..creates object that associates music with user..
        newCollection.setMusicid(newMusic.getId());
        newCollection.setUserid(user.getId());

        // ..saves object in db..
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

    // ---- GETTERS ---- 
    public String getTfAlbum() {
        return tfAlbum.getText();
    }

    public String getTfArtist() {
        return tfArtist.getText();
    }

    public String getTfDirPath() {
        return tfDirPath.getText();
    }

    public String getTfImgPath() {
        return tfImgPath.getText();
    }

    public String getTfTitle() {
        return tfTitle.getText();
    }
}
