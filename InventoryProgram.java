package Main;
import Model.InHouse;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryProgram extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        addTestData(inv);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainScreen.fxml"));
        Controllers.MainScreenController controller = new Controllers.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    void addTestData(Inventory inv) {
        Part a1 = new InHouse(1, "Part 1", 5.00, 5, 5, 100, 101);
        Part a2 = new InHouse(3, "Part 2", 10.00, 10, 5, 100, 103);
        Part b = new InHouse(2, "Part 3", 15.00, 12, 5, 100, 102);
        inv.partAdd(a1);
        inv.partAdd(b);
        inv.partAdd(a2);
        Product prod1 = new Product(1, "Product 1", 5.00, 5, 5, 100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(a2);
        inv.productAdd(prod1);
        Product prod2 = new Product(2, "Product 2", 10.00, 10, 5, 100);
        prod2.addAssociatedPart(a2);
        prod2.addAssociatedPart(b);
        inv.productAdd(prod2);
        Product prod3 = new Product(3, "Product 3", 15.00, 12, 5, 100);
        prod3.addAssociatedPart(b);
        prod3.addAssociatedPart(a1);
        inv.productAdd(prod3);
    }
}