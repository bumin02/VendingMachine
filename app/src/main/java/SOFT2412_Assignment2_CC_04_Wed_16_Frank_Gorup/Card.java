package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

public class Card {

    int id;
    String number;
    String name;

    public Card(String name, String number) {
        this.number = number;
        this.name = name;
    }

    public Card(int id, String name, String number) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

}
