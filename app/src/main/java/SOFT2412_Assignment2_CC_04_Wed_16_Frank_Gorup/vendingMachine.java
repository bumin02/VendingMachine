package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.util.ArrayList;
import java.util.Locale;

public class vendingMachine {
    ArrayList<Items> items;

    public vendingMachine() {
        this.items = new ArrayList<>();
    }

    // returns false if no options were found
    public boolean listOptions(String category) {
        boolean exist = false;
        boolean all = false;

        if (category.toLowerCase(Locale.ROOT).equals("all")) {
            exist = true;
            all = true;
        }

        for (Items i: this.items) {
            if (all) {
                System.out.println(String.format("+ %s (%s) QTY: %d Price: $. 2%f", i.getName(), i.getCode(), i.getQuantity(), i.getPrice()));
            } else if (i.getCategory().equals(category.toLowerCase(Locale.ROOT))) {
                exist = true;
                System.out.println(String.format("+ %s (%s) QTY: %d Price: $. 2%f", i.getName(), i.getCode(), i.getQuantity(), i.getPrice()));
            }
        }
        return exist;
    }




}
