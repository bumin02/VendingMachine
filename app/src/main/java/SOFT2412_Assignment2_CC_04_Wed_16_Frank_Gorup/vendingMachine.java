package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.Locale;

public class vendingMachine {
    ArrayList<Items> items;
    ArrayList<User> users;
    User currentUser;

    public vendingMachine() {
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
          
    }

    public void runVendingMachine() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nStarting vending machine...");
        System.out.print("What would you like to do? (press help for instructions)\n");

        String input = sc.next();

        if (input.toLowerCase().equals("help")) {
            System.out.println("\n--------------------------------------------------------HELP--------------------------------------------------------");
            System.out.println("\nlist [String: categories] : Categories include chocolate, drinks, candies, chips, or all.\n    Example:\n    > list chocolate\n            Mars (MA) QTY: 20 Price: $2\n            M&M (MM) QTY: 10 Price: $5\n            Bounty (BO) QTY: 5 Price: $1\n            Snickers (SN) QTY: 15 Price: 5\n");
            System.out.println("\nlogin : Command to login for an existing user.\n    Example:\n    > login\n            Enter username: myUsername\n            Enter password: myPassword\n            Successfully logged in!\n");
            System.out.println("\nregister : Command for a new user to create an account.\n    Example:\n    > Enter username: myUsername\n            Enter password: myPassword\n            Confirm password: myPassword\n            User successfully created!\n");
            System.out.println("\nbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]: Command allows buyer to purchase an item.\n    Example of a user buying 3 bottles of mineral water using 3x$5 notes and 3x50c coins :\n    > buyer cash 3 mw 50c*3,5*3\n            change 16.50 - 3.75 = $12.75\n            change breakdown ($5x2) ($2x1) (50cx1) (20cx1) (5cx1)\n    Example of a user buying 4 cans of Sprite using a credit card:\n    > buyer card 4 spr\n            card valid, transaction successful!\n");
            System.out.println("\nseller modify [String: itemName] [String: ItemCode] [Int: QTY] [Int: ItemPrice] : Seller is able to modify items.\n    Example of a seller chaning the item quantity of coke:\n    > seller modify qty coke 10\n            Success!\n");
            System.out.println("\nseller list [String: filetype]: Provides the seller with either a csv or txt file for the list of current items.\n    Example:\n    > seller list txt\n            ----------seller_report.txt----------\n            Mineral Water (MW) QTY: 20 Price: $2\n            Mars (MA) QTY: 12 Price: $5\n            Pringles (PR) QTY: 10 Price: $6\n            Mentos (MN) QTY: 15 Price: $3\n            -------------------------------------\n");
            System.out.println("\nseller summary [String: filetype]: Provides seller with the summary item history in csv or txt file.\n    Example:\n    > seller summary csv\n            ----------seller_summary.csv----------\n            Mineral Water (MW) Total sold: 2\n            Mars (MA) Total sold: 7\n            Pringles (PR) Total sold: 4\n            Mentos (MN) Total sold: 5\n            --------------------------------------\n");
            System.out.println("\n--------------------------------------------------------------------------------------------------------------------");        
            runVendingMachine();
        }

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

    public User getCurrentUser(){
        return this.currentUser;
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
