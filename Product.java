package Model;
import java.util.ArrayList;
public class Product {
    private ArrayList<Part> associatedParts = new ArrayList<>();
    private int productID;
    private String name;
    private double price = 0.0;
    private int inStock = 0;
    private int min;
    private int max;

    public Product(int productID, String name, double price, int inStock, int min, int max) {
        setProductID(productID);
        setName(name);
        setPrice(price);
        setInStock(inStock);
        setMin(min);
        setMax(max);

    }

    public int getProductID() {
        return this.productID;
    }
    public void setProductID(int id) {
        this.productID = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return this.inStock;
    }
    public void setInStock(int quantity) {
        this.inStock = quantity;
    }

    public int getMin() {
        return this.min;
    }
    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return this.max;
    }
    public void setMax(int max) {
        this.max = max;
    }

    public void addAssociatedPart(Part partToAdd) {
        associatedParts.add(partToAdd);
    }
    public boolean removeAssociatedPart(int partToRemove) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getPartID() == partToRemove) {
                associatedParts.remove(i);
                return true;
            }
        }

        return false;
    }
    public Part lookupAssociatedPart(int partToSearch) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getPartID() == partToSearch) {
                return associatedParts.get(i);
            }
        }
        return null;
    }

    public int getPartsListSize() {
        return associatedParts.size();
    }
}