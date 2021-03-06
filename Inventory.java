package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ArrayList<Product> allProducts;
    private ArrayList<Part> allParts;

    public Inventory() {
        allProducts = new ArrayList<>();
        allParts = new ArrayList<>();
    }
    public void productUpdate(Product product) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == product.getProductID()) {
                allProducts.set(i, product);
                break;
            }
        }
        return;
    }
    public void partAdd(Part partToAdd) {
        if (partToAdd != null) {
            allParts.add(partToAdd);
        }
    }
    public boolean partDelete(Part partToDelete) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToDelete.getPartID()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }
    public Part lookUpPart(int partToLookUp) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getPartID() == partToLookUp) {
                    return allParts.get(i);
                }
            }

        }
        return null;
    }
    public void productAdd(Product productToAdd) {
        if (productToAdd != null) {
            this.allProducts.add(productToAdd);
        }
    }
    public boolean productRemove(int productToRemove) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == productToRemove) {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }
    public Product lookUpProduct(int productToSearch) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductID() == productToSearch) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }
    public ObservableList<Part> lookUpProduct(String productNameToLookUp) {
        return null;
    }
    public ObservableList<Part> lookUpPart(String partNameToLookUp) {
        if (!allParts.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partNameToLookUp)) {
                    searchPartsList.add(p);
                }
            }
            return searchPartsList;
        }
        return null;
    }
    public void updatePart(Part partToUpdate) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToUpdate.partID) {
                allParts.set(i, partToUpdate);
                break;
            }
        }
        return;
    }
    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }
    public ArrayList<Part> getAllParts() {
        return allParts;
    }
    public int partListSize() {
        return allParts.size();
    }
    public int productListSize() {
        return allProducts.size();
    }
}