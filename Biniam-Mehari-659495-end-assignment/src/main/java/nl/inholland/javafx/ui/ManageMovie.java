package nl.inholland.javafx.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.Movie;
import nl.inholland.javafx.model.User;

import java.time.LocalDateTime;

public class ManageMovie extends BasicForm{
    private Stage window;
    private Database database ;
    private User user;
   private TextField txtMovieTitle;
  private  TextField txtPrice;
  private   TextField txtDuration;


    public ManageMovie( Database database, User user) {

        this.database = database;
        this.user = user;

        // set-up the window
        window = new Stage();
        window.setWidth(550);
        window.setHeight(600);
        window.setTitle("Fantastic Cinema-- -- manage movies--username: "+user.getUserName());


// making menu and adding it to menubar
        Menu adminMenu = new Menu("Admin");
        Menu helpMenu = new Menu("Help");
        Menu logoutMenu = new Menu("Logout");

        MenuItem logoutMenuItem = new MenuItem("Logout");
        logoutMenu.getItems().add(logoutMenuItem);

        MenuItem helpMenuItem = new MenuItem("About");
        helpMenu.getItems().add(helpMenuItem);

        MenuItem purchaseTicketMenuItem = new MenuItem("Purchase ticket");
        MenuItem manageShowingsMenuItem = new MenuItem("Manage showing");
        adminMenu.getItems().addAll(purchaseTicketMenuItem,manageShowingsMenuItem);

        //this will  print the write menubar
        MenuBar menubar = new MenuBar(adminMenu,helpMenu,logoutMenu);


        Label lblManageMovies = new Label("Manage movies");
        lblManageMovies.setFont(new Font(25));

        // here i made 2 tableview for showing the data of room and room 2
        TableView<Movie> tableViewRoom1 = new TableView();
        tableViewRoom1.setMinWidth(300);
        tableViewRoom1.setItems(database.getMovies());
        showRoomData( tableViewRoom1);

        // making the first gridpane to add the tableview
        GridPane gridPane = new GridPane();

        // gridPane.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF",0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setConstraints(lblManageMovies,0,1);
        GridPane.setConstraints(tableViewRoom1,0,2);

        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.getChildren().addAll(lblManageMovies,tableViewRoom1);

        //making lebel and textfield to collect information for a movie to add or edit
        Label lblMovieTitle = new Label("Title: ");
        GridPane.setConstraints(lblMovieTitle,0 ,0);
        Label lblPrice = new Label("Price: ");
        GridPane.setConstraints(lblPrice,0 ,1);
        Label lblDuration= new Label("Duration: ");
        GridPane.setConstraints(lblDuration,0 ,2);

         txtMovieTitle = new TextField();
        txtMovieTitle.setPromptText("Movie title");
        GridPane.setConstraints(txtMovieTitle,1 ,0);
        txtPrice = new TextField();
        txtPrice.setPromptText("Movie price");
        GridPane.setConstraints(txtPrice,1 ,1);
        txtDuration = new TextField();
        txtDuration.setPromptText("Movie duration");
        GridPane.setConstraints(txtDuration,1 ,2);

        Button btnAddMovie = new Button("Add movie");
        GridPane.setConstraints(btnAddMovie,2 ,2);
        Button btnClear = new Button("Clear");
        GridPane.setConstraints(btnClear,3 ,2);

        GridPane gridPane2 = new GridPane();
        //gridPane2.setVisible(false);
        gridPane2.minHeight(400);
        gridPane2.minWidth(350);
        // gridPane2.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF",0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane2.setPadding(new Insets(20, 20, 20, 20));
        gridPane2.setHgap(20);
        gridPane2.setVgap(20);
        gridPane2.getChildren().addAll(lblMovieTitle,lblPrice,txtMovieTitle,txtPrice,txtDuration,lblDuration,btnClear,btnAddMovie);

        manageShowingsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // ManageShowing manageShowing = new ManageShowing(database);
                new ManageShowing(user,database);
                window.close();

            }
        });

        purchaseTicketMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    new PurchaseTicket(user,database);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.close();

            }
        });

        logoutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    new Login().start(database);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.close();

            }
        });

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                clean();
            }
        });

        btnAddMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert a = new Alert(Alert.AlertType.ERROR);

                try {

                    if(!txtMovieTitle.getText().equals("")|| !txtPrice.getText().equals("")|| !txtDuration.getText().equals("")){
                        if (checkIfMovieExist(txtMovieTitle.getText(),Integer.parseInt(txtDuration.getText()))){
                            a.setContentText("Movie already added with\n the same title and duration");
                            a.show();
                        }
                        else {
                            database.addMovie(new Movie(txtMovieTitle.getText().toString(),Double.parseDouble(txtPrice.getText()),Integer.parseInt(txtDuration.getText())));
                            tableViewRoom1.refresh();
                            clean();
                        }

                    }
                    else {
                        a.setContentText("First Fill the required fields");
                        a.show();
                    }
                }catch (Exception exp){
                    a.setContentText("price or duration needs to be numbers");
                    a.show();
                }


            }
        });


        // in order to add all the gridpane i make a borderpane
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(menubar);
        borderpane.setCenter(gridPane);
        borderpane.setBottom(gridPane2);


// create the scene and show the window and adds the borderpane
        Scene scene = new Scene(borderpane);

// css file is added hier
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();
    }
    // this will method will print the data of each room
    // i made this methode to minimize duplicate code
    private void showRoomData(TableView<Movie> room){

        TableColumn<Movie, String> movieTitle = new TableColumn<>("Title");
        TableColumn<Movie, Double> price = new TableColumn<>("Price");
        TableColumn<Movie, LocalDateTime> duration = new TableColumn<>("Duration");


        movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        room.getColumns().addAll(movieTitle,duration,price);
    }

    private void clean(){
        txtMovieTitle.setText("");
        txtDuration.setText("");
        txtPrice.setText("");
    }
    private boolean checkIfMovieExist(String movieTitle, int duration){
        for (Movie m:database.getMovies()){
            if (movieTitle.equals(m.getTitle())&& m.getDuration()==duration){
                return true;
            }
        }
        return false;
    }
}
