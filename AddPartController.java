package Controllers;
import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    Inventory inv;
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outSourcedRadio;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField count;
    @FXML private TextField price;
    @FXML private TextField max;
    @FXML private TextField company;
    @FXML private Label companyLabel;
    @FXML private TextField min;

    public AddPartController(Inventory inv) {
        this.inv = inv;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        randPartID();
        resetFields();
    }
    private void resetFields() {
        name.setText("Part Name");
        count.setText("Inv Count");
        price.setText("Part Price");
        min.setText("Min");
        max.setText("Max");
        company.setText("Machine ID");
        companyLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
    }
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController(inv);

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
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                ErrorMessages.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                ErrorMessages.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            ErrorMessages.errorPart(3, field);
            System.out.println(e);

        }
        return error;
    }
    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            ErrorMessages.errorPart(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            ErrorMessages.errorPart(3, field);
            return true;
        }
        return false;
    }
    private void randPartID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);

        if (inv.partListSize() == 0) {
            id.setText(num.toString());

        }
        if (inv.partListSize() == 1000) {
            ErrorMessages.errorPart(3, null);
        } else {
            match = taken(num);

            if (match == false) {
                id.setText(num.toString());
            } else {
                randPartID();
            }
        }
    }
    private boolean taken(Integer num) {
        Part match = inv.lookUpPart(num);
        return match != null;
    }
    private void addInHouse() {
        inv.partAdd(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }
    private void addOutSourced() {
        inv.partAdd(new OutSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }
    @FXML
    private void selectInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");
        company.setText("Machine ID");
    }
    @FXML
    private void selectOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");
        company.setText("Company Name");
    }
    @FXML
    private void idDisabled(MouseEvent event) {
    }
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }
    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = ErrorMessages.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }
    @FXML
    private void saveAddPart(MouseEvent event) {

        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
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
            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                ErrorMessages.errorPart(4, name);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                ErrorMessages.errorPart(8, min);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                ErrorMessages.errorPart(6, count);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                ErrorMessages.errorPart(7, count);
                return;
            }
            if (end) {
                return;
            } else if (company.getText().trim().isEmpty() || company.getText().trim().toLowerCase().equals("company name")) {
                ErrorMessages.errorPart(3, company);
                return;

            } else if (inHouseRadio.isSelected() && !company.getText().trim().matches("[0-9]*")) {
                ErrorMessages.errorPart(9, company);
                return;
            } else if (inHouseRadio.isSelected()) {
                addInHouse();

            } else if (outSourcedRadio.isSelected()) {
                addOutSourced();

            }

        } else {
            ErrorMessages.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }
}