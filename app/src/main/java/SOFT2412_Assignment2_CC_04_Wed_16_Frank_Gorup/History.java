package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

public class History {
    String name;
    String code;
    String category;
    double price;
    int quantity;

    public History(String name, String code, String category, double price, int quantity) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getCategory() {
        return this.category;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

}
