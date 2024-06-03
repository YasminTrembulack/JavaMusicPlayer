package com.yasminm.scenes;

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
