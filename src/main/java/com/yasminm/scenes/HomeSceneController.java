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

public class HomeSceneController {
    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = HomeSceneController.class.getResource("home-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        HomeSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        controller.setCurrentUser(user);
        controller.buildMusicDisplay(controller, user);
        controller.setPaneListeners(controller);

        return scene;
    }

    public void buildMusicDisplay(HomeSceneController controller, UserData user) {

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

    public void setPaneListeners(HomeSceneController controller) {
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
                        MusicData music = getMusicFromDB(musicTitle.getText());

                        controller.setCurrentMusic(music);
                        MediaPlayer mp = createMediaPlayer(controller);
                        controller.setCurrentMusicPlayer(mp);

                        controller.setLbTitle(music.getTitle());
                        controller.setLbAlbumAndArtist(music.getArtist() + " - " + music.getAlbum());
                    }
                });
            }
        });
    }

    public void btPlayAction(ActionEvent e) {
        MediaPlayer mp = getCurrentMusicPlayer();
   
        if(mp.getStatus() == Status.PLAYING) {
            mp.pause();
            return;
        }
        else if(mp.getStatus() == Status.PAUSED) {
            mp.play();
            return;
        }

        mp.play();
        return;
    }

    public void btNextAction(ActionEvent e) {
        int index = 0;
        List<UserCollection> collection = getUserCollectionFromDB(getCurrentUser());
        ArrayList<MusicData> allMusic = getAllMusics(collection);

        for(int i = 0; i < allMusic.size(); i++) {
            if(allMusic.get(i).equals(getCurrentMusic())) {
                index = i;
            }
        }

        int music = index;
        if(index <= allMusic.size() - 1) {
            music = index + 1;
        } 

        System.out.println(music);
        System.out.println(allMusic.get(music).getTitle());
        setCurrentMusic(allMusic.get(music));
        setLbTitle(currentMusic.getTitle());
        setLbAlbumAndArtist(currentMusic.getArtist() + " - " + currentMusic.getAlbum());
    }

    public void btBackAction(ActionEvent e) {
        int index = 0;
        List<UserCollection> collection = getUserCollectionFromDB(getCurrentUser());
        ArrayList<MusicData> allMusic = getAllMusics(collection);
        for(MusicData m : allMusic) System.out.println(m.getTitle());

        for(int i = 0; i < allMusic.size(); i++) {
            if(allMusic.get(i).equals(getCurrentMusic())) {
                index = i;
            }
        }

        int music = 0;
        if(index - 1 <= allMusic.size() - 1) {
            music = index;
        } 
        else {
            music = index - 1;
        }

        System.out.println(music);
        System.out.println(allMusic.get(music).getTitle());
        setCurrentMusic(allMusic.get(music));
        setLbTitle(currentMusic.getTitle());
        setLbAlbumAndArtist(currentMusic.getArtist() + " - " + currentMusic.getAlbum());
    }

    // public void setButtonsListeners(HomeSceneController controller, List<UserCollection> collection) {

    //     controller.btPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {
    //         @Override
    //         public void handle(MouseEvent event) {
    //             System.out.println("AAAAAAAAAAA CLICKED");
    //             MediaPlayer mp = controller.getCurrentMusicPlayer();
   
    //             if(mp.getStatus() == Status.PLAYING) {
    //                 mp.pause();
    //             }
    //             else if(mp.getStatus() == Status.PAUSED) {
    //                 mp.play();
    //             }
    //         }
    //     });

    //     controller.btNext.setOnMouseClicked(new EventHandler<MouseEvent>() {
    //         @Override
    //         public void handle(MouseEvent event) {
    //             int index = 0;
    //             ArrayList<MusicData> allMusic = controller.getAllMusics(collection);

    //             for(int i = 0; i < allMusic.size(); i++) {
    //                 if(allMusic.get(i).equals(controller.getCurrentMusic())) {
    //                     index = i;
    //                 }
    //             }

    //             controller.setCurrentMusic(allMusic.get(index + 1));
    //         }
    //     });
    // }

    public MediaPlayer createMediaPlayer(HomeSceneController controller) {
        try {
            File file = new File(controller.getCurrentMusic().getdirectoryPath());
            URI uri = file.toURI();
            String mediaUrl = uri.toString();           
            Media media = new Media(mediaUrl); 
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            return mediaPlayer;

        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
            return null;
        }
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

        transaction.commit();

        return l;
    }

    public MusicData getMusicFromDB(String musicTitle) {
        // .. create session to consult database ..
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // .. gets user collection data from database ..
        Query query = session.createQuery("from MusicData m where m.title = :musicTitle");
        query.setParameter("musicTitle", musicTitle);
        List<MusicData> music = query.list();

        MusicData m = (MusicData) music.get(0);

        transaction.commit();

        return m;
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

        MusicData m = (MusicData) music.get(0);

        transaction.commit();

        return m;
    }

    public ArrayList<MusicData> getAllMusics(List<UserCollection> l) {
        ArrayList<MusicData> paths = new ArrayList<>();

        for(UserCollection u : l) {
            MusicData m = getMusicFromDB(u.getMusicid());
            paths.add(m);
        }

        return paths;
    }

    @FXML
    private ImageView ivMusicImage;

    @FXML
    private VBox vbAllMusic;

    @FXML
    private Pane pnCurrentMusic;

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbAlbumAndArtist;

    @FXML
    private ProgressBar pb;

    @FXML
    private Button btPlay;

    @FXML
    private Button btNext;

    @FXML
    private Button btBack;

    @FXML
    private Label lbUsername;

    private MusicData currentMusic;

    private MediaPlayer currentMusicPlayer;

    private UserData currentUser;

    private Integer currentMusicIndex;

    public Integer getCurrentMusicIndex() {
        return currentMusicIndex;
    }

    public void setCurrentMusicIndex(Integer currentMusicIndex) {
        this.currentMusicIndex = currentMusicIndex;
    }

    public UserData getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserData currentUser) {
        this.currentUser = currentUser;
    }

    public MediaPlayer getCurrentMusicPlayer() {
        return currentMusicPlayer;
    }

    public void setCurrentMusicPlayer(MediaPlayer currentMusicPlayer) {
        this.currentMusicPlayer = currentMusicPlayer;
    }

    public Pane getPnCurrentMusic() {
        return pnCurrentMusic;
    }

    public MusicData getCurrentMusic() {
        return currentMusic;
    }

    public ImageView getIvMusicImage() {
        return ivMusicImage;
    }

    public VBox getVbAllMusic() {
        return vbAllMusic;
    }

    public Label getLbTitle() {
        return lbTitle;
    }

    public Label getLbAlbumAndArtist() {
        return lbAlbumAndArtist;
    }

    public void setLbTitle(Label lbTitle) {
        this.lbTitle = lbTitle;
    }

    public void setCurrentMusic(MusicData currentMusic) {
        this.currentMusic = currentMusic;
    }

    public void setIvMusicImage(ImageView ivMusicImage) {
        this.ivMusicImage = ivMusicImage;
    }

    public void setAp(VBox vbAllMusic) {
        this.vbAllMusic = vbAllMusic;
    }

    public void setLbTitle(String MusicTitle) {
        this.lbTitle.setText(MusicTitle);
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

    public Button getBtPlay() {
        return btPlay;
    }

    public void setBtPlay(Button btPlay) {
        this.btPlay = btPlay;
    }

    public Button getBtNext() {
        return btNext;
    }

    public void setBtNext(Button btNext) {
        this.btNext = btNext;
    }

    public Button getBtBack() {
        return btBack;
    }

    public void setBtBack(Button btBack) {
        this.btBack = btBack;
    }

    public Label getLbUsername() {
        return lbUsername;
    }

    public void setLbUsername(String lbUsername) {
        this.lbUsername.setText(lbUsername);
    }

}
