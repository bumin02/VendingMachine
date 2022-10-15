package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

public class Card {
    String number;
    String name;

    public Card(String name, String number) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

}
