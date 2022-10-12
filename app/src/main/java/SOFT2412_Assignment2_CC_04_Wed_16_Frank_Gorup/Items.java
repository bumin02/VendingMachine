package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

public class Items{
    String name;
    String code;
    String category;
    long price;
    int quantity;


    public Items(String name, String code, String category, long price, int quantity){
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