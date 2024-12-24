package GUI;
import Entity.Product;
import Service.ProductService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ProductsPage extends Application {

    private final ProductService productService = new ProductService();

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Fetch products using the service layer
        List<Product> products = productService.getProducts();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            Label nameLabel = new Label(product.getName());
            Label priceLabel = new Label("$" + product.getPrice());
            Button addToCartButton = new Button("Add to Cart");

            // Handle "Add to Cart" button click
            addToCartButton.setOnAction(e -> {
                System.out.println(product.getName() + " added to cart.");
            });

            // Add to GridPane
            gridPane.add(nameLabel, 0, i);
            gridPane.add(priceLabel, 1, i);
            gridPane.add(addToCartButton, 2, i);
        }

        VBox root = new VBox(10, gridPane);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Products Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
