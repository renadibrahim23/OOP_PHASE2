package GUI;
import Entity.Admin;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Service.AdminService;


public class AdminDashBoard extends Application{

    private AdminService adminService = new AdminService();
    adminService.createNewAdmin("admin123",)
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();


        VBox navigationMenu = createNavigationMenu();
        root.setLeft(navigationMenu);


        Label welcomeLabel = new Label("Welcome to the Admin Dashboard");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
        root.setCenter(welcomeLabel);



        Admin admin = adminService.getAdminByUsername("admin123"); // Fetch from service


        VBox adminInfoSection = createAdminInfoSection(admin);
        root.setRight(adminInfoSection);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private VBox createNavigationMenu(){
        VBox menu = new VBox(10);
        menu.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10px;");

        Button dashboardButton = new Button("Dashboard");
        Button usersButton = new Button("Manage Users");
        Button productsButton = new Button("Manage Products");
        Button reportsButton = new Button("Reports");
        Button logoutButton = new Button("Logout");

        dashboardButton.setStyle("-fx-background-color:#3498db; -fx-text-fill:white;");
        usersButton.setStyle("-fx-background-color:#3498db; -fx-text-fill:white;");
        reportsButton.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");
        productsButton.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");
        logoutButton.setStyle("-fx-background-color:#e74c3c;-fx-text-fill:white;");


        menu.getChildren().addAll(dashboardButton,usersButton,reportsButton,productsButton,logoutButton);
        return menu;


    }

    private VBox createAdminInfoSection(Admin admin){
        VBox  adminInfo=new VBox(10);
        adminInfo.setStyle("-fx-background-color: #f9f9f9; -fx-padding:15px; -fx-border-color: #cccccc;");
        adminInfo.setPrefWidth(200);

        //ImageView adminPicture= new ImageView(new Image("file:"+ adminService.getPicturePath()));

        Label usernameLabel= new Label("username: "+adminService.getAdminUsername(admin));
        Label passwordLabel= new Label("Password: "+adminService.getAdminPassword(admin));
        Label roleLabel= new Label("Role: "+adminService.getAdminRole(admin));
        Label hoursLabel= new Label("Working Hours: "+adminService.getAdminWorkingHours(admin));

        usernameLabel.setStyle("-fx-font-size: 14px;");
        passwordLabel.setStyle("-fx-font-size: 14px;");
        roleLabel.setStyle("-fx-font-size: 14px;");
        hoursLabel.setStyle("-fx-font-size: 14px;");

        adminInfo.getChildren().addAll(usernameLabel,passwordLabel,roleLabel,hoursLabel);
        return adminInfo;
    }

    public static void main(String[] args) {launch(args);}
}
