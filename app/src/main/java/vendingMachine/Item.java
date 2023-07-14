package vendingMachine;

public class Item {
    String name;
    String code;
    String category;
    double price;
    int quantity;
    int id;

    public Item(int id, String name, String code, String category, double price, int quantity) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
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

    public int getId() {
        return this.id;
    }

}