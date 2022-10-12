package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.util.ArrayList;
import java.util.Locale;

public class vendingMachine {
    ArrayList<Items> items;
    ArrayList<User> users;
    User currentUser;

    public vendingMachine() {
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    // return false if account exists or invalid input
    public boolean createAccount(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // check if user exists
        for (User i : users) {
            if (i.getAccount().equals(username)) {
                return false;
            }
        }
        User user = new User(username, password);
        this.users.add(user);
        this.currentUser = user;
        return true;
    }

    // return false if invalid or user and password combination does not exist
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // check if user exists
        for (User i : users) {
            if (i.getAccount().equals(username) && i.getPassword().equals(password)) {
                this.currentUser = i;
                return true;
            }
        }
        return false;
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
