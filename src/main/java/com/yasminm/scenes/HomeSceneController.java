package com.yasminm.scenes;

import com.yasminm.model.UserData;
import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.util.HibernateUtil;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javafx.event.ActionEvent;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class HomeSceneController {
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = HomeSceneController.class.getResource("home-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        HomeSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        controller.user = user;

        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        controller.setLbUsername(user.getUsername());

        Query query = session.createQuery("from UserCollection u where u.userid = :userid");
        query.setParameter("userid", user.getId());
        List<UserCollection> collection = query.list();

        for(int i = 0; i < collection.size(); i++) {
            query = session.createQuery("from MusicData m where m.id = :musicid");
            query.setParameter("musicid", collection.get(i).getMusicid());
            List<MusicData> music = query.list();
            
            Pane p = new Pane();
            Label l = new Label(music.get(0).getTitle());
            p.getChildren().add(l);
            controller.vbAllMusic.getChildren().add(p);
        }

        transaction.commit();


        //IMAGEM ##########
        String imagePath = "C:/Users/disrct/Pictures/Chrysanthemum.jpg";
        File file = new File(imagePath);
        String fileURL = file.toURI().toURL().toString();
        Image image = new Image(fileURL);
        controller.displayImage(image);



        return scene;
    }

    public void goToAddMusic(){
        try {
            Stage crrStage = (Stage) btAddMusic
                .getScene().getWindow();
            crrStage.close();

            Stage stage = new Stage();
            Scene scene = MusicRegistrationSceneController.CreateScene(user);
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Erro ao processar a tela AddMusic. Consulte o apoio de TI",
                    ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    public void tryExit() {

        try {
            Stage crrStage = (Stage) btExit
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

    @FXML
    private Button btExit;

    @FXML
    private ImageView ivMusicImage;
    public void displayImage(Image img) {
        ivMusicImage.setImage(img);
    }

    @FXML
    private ScrollPane sp;

    @FXML
    private VBox vbAllMusic;

    @FXML
    private Label lbMusicTitle;

    @FXML
    private Label lbAlbumAndArtist;

    @FXML
    private ProgressBar pb;

    @FXML
    private Button btPlayButton;

    @FXML
    private Button btNext;

    @FXML
    private Button btPrevious;

    @FXML
    private Label lbUsername;

    @FXML
    private Button btAddMusic;

    public UserData user;

    public ImageView getIvMusicImage() {
        return ivMusicImage;
    }

    public void setIvMusicImage(ImageView ivMusicImage) {
        this.ivMusicImage = ivMusicImage;
    }

    public ScrollPane getSp() {
        return sp;
    }

    public void setSp(ScrollPane sp) {
        this.sp = sp;
    }

    public VBox getAp() {
        return vbAllMusic;
    }

    public void setAp(VBox vbAllMusic) {
        this.vbAllMusic = vbAllMusic;
    }

    public Label getLbMusicTitle() {
        return lbMusicTitle;
    }

    public void setLbMusicTitle(String lbMusicTitle) {
        this.lbMusicTitle.setText(lbMusicTitle);
    }

    public Label getLbAlbumAndArtist() {
        return lbAlbumAndArtist;
    }

    public void setLbAlbumAndArtist(String lbAlbumAndArtist) {
        this.lbAlbumAndArtist.setText(lbAlbumAndArtist);
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public Button getBtPlayButton() {
        return btPlayButton;
    }

    public void setBtPlayButton(Button btPlayButton) {
        this.btPlayButton = btPlayButton;
    }

    public Button getBtNext() {
        return btNext;
    }

    public void setBtNext(Button btNext) {
        this.btNext = btNext;
    }

    public Button getBtExit() {
        return btExit;
    }

    public void setBtExit(Button btExit) {
        this.btExit = btExit;
    }

    public Button getBtPrevious() {
        return btPrevious;
    }

    public void setBtPrevious(Button btPrevious) {
        this.btPrevious = btPrevious;
    }

    public Label getLbUsername() {
        return lbUsername;
    }

    public void setLbUsername(String lbUsername) {
        this.lbUsername.setText(lbUsername);
    }

    
}
