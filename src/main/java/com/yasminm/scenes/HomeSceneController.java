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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

//  //IMAGEM ##########
//  String imagePath = "C:/Users/disrct/Pictures/Chrysanthemum.jpg";
//  File file = new File(imagePath);
//  String fileURL = file.toURI().toURL().toString();
//  Image image = new Image(fileURL);
//  controller.displayImage(image);


public class HomeSceneController {

    public static Scene CreateScene(UserData user) throws Exception {
        URL sceneUrl = HomeSceneController.class.getResource("home-scene.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();

        // ..creates controller that uses data from user object..
        HomeSceneController controller = loader.getController();
        Scene scene = new Scene(root);

        controller.setLbUsername(user.getUsername());
        controller.setCurrentUser(user);
        controller.buildMusicDisplay(controller, user);
        controller.setPaneListeners(controller);

        return scene;
    }

    public void buildMusicDisplay(HomeSceneController controller, UserData user) {
        List<UserCollection> collection = getUserCollectionFromDB(user);

        if(collection == null) return;

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

                // ..panel hover..
                pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getTarget() instanceof Pane) {
                            pane.setStyle("-fx-background-color: #fdfdfd");
                        }
                    }
                });

                // ..panel hover..
                pane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        pane.setStyle("-fx-background-color: #f0f0f0");
                    }
                });

                // ..sets current music when music clicked..
                pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // ..gets vbox inside pane(music), where its displayed title, album and artist name..
                        ObservableList<Node> inside_vb = pane.getChildren();
                        VBox vb = (VBox) inside_vb.get(0);

                        // ..gets music object from music title label..
                        ObservableList<Node> labels = vb.getChildren();
                        Label musicTitle = (Label) labels.get(0);
                        MusicData music = getMusicFromDB(musicTitle.getText());

                        // ..display music on right side of screen..
                        selectMusic(music);
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

        // ..finds music index..
        for(int i = 0; i < allMusic.size(); i++) {
            MusicData music = allMusic.get(i);
            if(music.getTitle().equals(getCurrentMusic().getTitle())) {
                index = i;
            }
        }

        // ..if its the last music, keep it on the last
        int music = index;
        if(index < allMusic.size() - 1) {
            music = index + 1;
        } 

        // ..sets current music as next in array..
        selectMusic(allMusic.get(music));
    }

    public void btBackAction(ActionEvent e) {
        int index = 0;
        List<UserCollection> collection = getUserCollectionFromDB(getCurrentUser());
        ArrayList<MusicData> allMusic = getAllMusics(collection);

        // ..finds music index..
        for(int i = 0; i < allMusic.size(); i++) {
            MusicData music = allMusic.get(i);
            if(music.getTitle().equals(getCurrentMusic().getTitle())) {
                index = i;
            }
        }

        // ..if its the first music, keep it on the first
        int music = index;
        if(index > 0) {
            music = index - 1;
        } 

        // ..sets current music as previous in array..
        selectMusic(allMusic.get(music));
    }

    public void goToAddMusic(ActionEvent e) {
        Stage crrStage = (Stage) btAddMusic
                .getScene().getWindow();
            crrStage.close();

            try {
                Stage stage = new Stage();
                Scene scene = MusicRegistrationSceneController.CreateScene(currentUser);
                stage.setScene(scene);
                stage.show();
            } 
            catch (Exception ex) {
                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Erro ao processar a tela de HOME. Consulte o apoio de TI",
                        ButtonType.OK);
                alert.showAndWait();
                ex.printStackTrace();
            }
    }

    public void tryExit(ActionEvent e) {
        Stage crrStage = (Stage) btAddMusic
                .getScene().getWindow();
            crrStage.close();

            try {
                Stage stage = new Stage();
                Scene scene = WelcomeSceneController.CreateScene();
                stage.setScene(scene);
                stage.show();
            } 
            catch (Exception ex) {
                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Erro ao processar a tela de HOME. Consulte o apoio de TI",
                        ButtonType.OK);
                alert.showAndWait();
                ex.printStackTrace();
            }
    }

    public void goToDelete(ActionEvent e) {
        Stage crrStage = (Stage) btAddMusic
                .getScene().getWindow();
            crrStage.close();

            try {
                Stage stage = new Stage();
                Scene scene = HomeDeleteSceneController.CreateScene();
                stage.setScene(scene);
                stage.show();
            } 
            catch (Exception ex) {
                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Erro ao processar a tela de HOME. Consulte o apoio de TI",
                        ButtonType.OK);
                alert.showAndWait();
                ex.printStackTrace();
            }
    }

    public void selectMusic(MusicData music) {
        setCurrentMusic(music);
        setLbTitle(currentMusic.getTitle());
        setLbAlbumAndArtist(currentMusic.getArtist() + " - " + currentMusic.getAlbum());

        File file = new File(currentMusic.getImagePath());
        Image image = new Image(file.toURI().toString());
        setIvMusicImage(image);

        MediaPlayer mp = createMediaPlayer(this);
        setCurrentMusicPlayer(mp);
    }

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
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

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

    public MusicData getMusicFromDB(String musicTitle) {
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from MusicData m where m.title = :musicTitle");
        query.setParameter("musicTitle", musicTitle);
        List<MusicData> music = query.list();

        if(music.size() <= 0) {
            System.out.println("Error collecting data from database ;/");
            return null;
        }

        MusicData m = (MusicData) music.get(0);

        transaction.commit();

        return m;
    }

    public MusicData getMusicFromDB(Long musicid) {
        Session session = HibernateUtil
                .getSessionFactory()
                .getCurrentSession();
        Transaction transaction = session.beginTransaction();

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

    @FXML
    private Button btAddMusic;

    @FXML
    private Button btExit;

    @FXML
    private Button btDelete;

    private MusicData currentMusic;

    private MediaPlayer currentMusicPlayer;

    private UserData currentUser;

    // ---- GETTERS ---- 
    public UserData getCurrentUser() {
        return currentUser;
    }

    public MediaPlayer getCurrentMusicPlayer() {
        return currentMusicPlayer;
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

    // ---- SETTERS ---- 
    public void setCurrentUser(UserData currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentMusicPlayer(MediaPlayer currentMusicPlayer) {
        this.currentMusicPlayer = currentMusicPlayer;
    }

    public void setCurrentMusic(MusicData currentMusic) {
        this.currentMusic = currentMusic;
    }

    public void setIvMusicImage(Image img) {
        this.ivMusicImage.setImage(img);
    }

    public void setLbTitle(String MusicTitle) {
        this.lbTitle.setText(MusicTitle);
    }

    public void setLbAlbumAndArtist(String lbAlbumAndArtist) {
        this.lbAlbumAndArtist.setText(lbAlbumAndArtist);
    }

    public void setLbUsername(String lbUsername) {
        this.lbUsername.setText(lbUsername);
    }
}
