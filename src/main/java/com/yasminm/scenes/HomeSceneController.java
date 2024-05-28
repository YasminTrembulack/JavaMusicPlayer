package com.yasminm.scenes;

import com.yasminm.model.UserData;
import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.util.HibernateUtil;

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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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

            Image img = new Image(getClass().getResourceAsStream(music.get(0).getImagePath()));
            controller.displayImage(img);

            controller.setLbMusicTitle(music.get(0).getTitle());
            controller.setLbAlbumAndArtist(music.get(0).getTitle() + "-" + music.get(0).getAlbum());
        }

        return scene;
    }

    @FXML
    private ImageView ivMusicImage;
    public void displayImage(Image img) {
        ivMusicImage.setImage(img);
    }

    @FXML
    private ScrollPane sp;

    @FXML
    private AnchorPane ap;

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

    public AnchorPane getAp() {
        return ap;
    }

    public void setAp(AnchorPane ap) {
        this.ap = ap;
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
