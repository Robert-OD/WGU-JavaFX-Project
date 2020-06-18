package Controllers;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddProductController implements Initializable {
    Inventory inv;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField price;
    @FXML private TextField count;
    @FXML private TextField min;
    @FXML private TextField max;
    @FXML private TextField search;
    @FXML private TableView<Part> partSearchTable;
    @FXML private TableView<Part> assocPartsTable;
    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    public AddProductController(Inventory inv) {
        this.inv = inv;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        randomProductID();
        fillSearchTable();
    }
    private void randomProductID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);


        if (inv.productListSize() == 0) {
            id.setText(num.toString());

        }
        if (inv.productListSize() == 1000) {
            ErrorMessages.errorProduct(3, null);
        } else {
            match = randomNum(num);

            if (match == false) {
                id.setText(num.toString());
            } else {
                randomProductID();
            }
        }

        id.setText(num.toString());
    }
    private boolean randomNum(Integer num) {
        Part match = inv.lookUpPart (num);
        return match != null;
    }
    private void fillSearchTable() {
        partsInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = priceFormatting();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainScreen.fxml"));
            Controllers.MainScreenController controller = new Controllers.MainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                ErrorMessages.errorProduct(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                ErrorMessages.errorProduct(5, field);
                error = true;
            }
        } catch (NumberFormatException e) {
            error = true;
            ErrorMessages.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }
    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            ErrorMessages.errorProduct(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            ErrorMessages.errorProduct(3, field);
            return true;
        }
        return false;

    }
    private <T> TableColumn<T, Double> priceFormatting() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }
    private void saveProd() {
        Product product = new Product(Integer.parseInt(id.getText().trim()),
                name.getText().trim(),
                Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()),
                Integer.parseInt(max.getText().trim()));

        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.productAdd(product);

    }
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");

    }
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!search.getText().trim().isEmpty()) {
            partsInventory.clear();
            partSearchTable.setItems(inv.lookUpPart(search.getText().trim()));
            partSearchTable.refresh();
        }
    }
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getPartID();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getPartID() == id) {
                    ErrorMessages.errorProduct(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }

            TableColumn<Part, Double> costCol = priceFormatting();
            assocPartsTable.getColumns().addAll(costCol);

            assocPartsTable.setItems(assocPartList);
        }
    }
    @FXML
    private void deletePart(MouseEvent event) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = ErrorMessages.confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            ErrorMessages.infoWindow(1, removePart.getName());
        } else {
            ErrorMessages.infoWindow(2, "");
        }

    }
    @FXML
    private void cancelAddProduct(MouseEvent event) {
        boolean cancel = ErrorMessages.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }
    @FXML
    private void saveAddProduct(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            ErrorMessages.errorProduct(4, name);
            return;
        }
        for (TextField fieldCount1 : fieldCount) {
            boolean valueError = checkValue(fieldCount1);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount1);
            if (typeError) {
                end = true;
                break;
            }
        }
        if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            ErrorMessages.errorProduct(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            ErrorMessages.errorProduct(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            ErrorMessages.errorProduct(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            ErrorMessages.errorProduct(6, price);
            return;
        }
        if (assocPartList.isEmpty()) {
            ErrorMessages.errorProduct(7, null);
            return;
        }

        saveProd();
        mainScreen(event);

    }
    @FXML
    void clearField(MouseEvent event) {
        search.setText("");
        if (!partsInventory.isEmpty()) {
            partSearchTable.setItems(partsInventory);
        }

    }
}