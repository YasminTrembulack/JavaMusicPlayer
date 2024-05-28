package com.yasminm.scenes;

import com.yasminm.model.UserData;
import com.yasminm.model.MusicData;
import com.yasminm.model.UserCollection;
import com.yasminm.util.HibernateUtil;

import javafx.event.EventHandler;
import java.net.URL;
import java.util.List;
import java.util.Observable;

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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;

public class HomeSceneController {
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = HomeSceneController.class.getResource("home-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        HomeSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from UserCollection u where u.userid = :userid");
        query.setParameter("userid", user.getId());
        List<UserCollection> collection = query.list();

        for (int i = 0; i < collection.size(); i++) {
            query = session.createQuery("from MusicData m where m.id = :musicid");
            query.setParameter("musicid", collection.get(i).getMusicid());
            List<MusicData> music = query.list();

            Pane p = new Pane();
            p.setPrefSize(615, 84);

            VBox vb = new VBox();
            vb.setPadding(new Insets(20, 20, 20, 20));
            p.getChildren().add(vb);

            Label lbTitle = new Label(music.get(0).getTitle());
            lbTitle.setStyle("-fx-font-weight: bold");
            vb.getChildren().add(lbTitle);

            Label lbArtist = new Label(music.get(0).getArtist());
            vb.getChildren().add(lbArtist);

            controller.vbAllMusic.getChildren().add(p);
        }

        transaction.commit();

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

                pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ObservableList<Node> inside_vb = pane.getChildren();
                        VBox vb = (VBox) inside_vb.get(0);
                        
                        ObservableList<Node> labels = vb.getChildren();
                        Label musicTitle = (Label) labels.get(0);
                        System.out.println("CLICKED:" + musicTitle.getText());

                        controller.setLbTitle(musicTitle.getText());
                    }
                });
            }
        });

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
    private VBox vbAllMusic;

    @FXML
    private Label lbTitle;

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

    public VBox getAp() {
        return vbAllMusic;
    }

    public void setAp(VBox vbAllMusic) {
        this.vbAllMusic = vbAllMusic;
    }

    public Label getLbTitle() {
        return lbTitle;
    }

    public void setLbTitle(String MusicTitle) {
        this.lbTitle.setText(MusicTitle);
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
