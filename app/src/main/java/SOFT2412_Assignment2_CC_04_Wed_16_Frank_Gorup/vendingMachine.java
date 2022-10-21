package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class vendingMachine {
    ArrayList<Item> items;
    User currentUser;
    Database db;

    public vendingMachine(Database db) {
        this.db = db;

        // by default, anonymous user
        this.currentUser = null;
        this.items = new ArrayList<>();

        // for setup if needed
        // initialSetup();
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    // helper function to add items to the vending machine
    public void initialSetup() {

        // fake user
        this.db.insertIntoUsersTable("test", "test", "buyer");

        // dummy items with quantities and prices defined
        this.db.insertIntoItemsTable("Mineral Water", "MW", "Drinks", 3, 20);
        this.db.insertIntoItemsTable("Sprite", "SP", "Drinks", 5, 10);
        this.db.insertIntoItemsTable("Coca Cola", "CC", "Drinks", 5, 10);
        this.db.insertIntoItemsTable("Pepsi", "PE", "Drinks", 4, 10);
        this.db.insertIntoItemsTable("Juice", "JU", "Drinks", 3.5, 10);

        this.db.insertIntoItemsTable("Mars", "MA", "Chocolates", 5, 12);
        this.db.insertIntoItemsTable("M&M", "MM", "Chocolates", 6, 12);
        this.db.insertIntoItemsTable("Bounty", "BO", "Chocolates", 5, 12);
        this.db.insertIntoItemsTable("Snickers", "SN", "Chocolates", 7, 12);

        this.db.insertIntoItemsTable("Smiths", "SM", "Chips", 5, 10);
        this.db.insertIntoItemsTable("Pringles", "PR", "Chips", 6, 10);
        this.db.insertIntoItemsTable("Kettle", "KE", "Chips", 5, 10);
        this.db.insertIntoItemsTable("Thins", "TH", "Chips", 4, 10);

        this.db.insertIntoItemsTable("Mentos", "ME", "Candies", 3, 15);
        this.db.insertIntoItemsTable("Sour Patch", "SP", "Candies", 5, 12);
        this.db.insertIntoItemsTable("Skittles", "SK", "Candies", 6, 12);

    }

    public void runVendingMachine() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nStarting vending machine...");
        System.out.println("What would you like to do? (press help for instructions)");
        System.out.print(">");

        while (sc.hasNext()) {

            String input = sc.nextLine();

            if (input.toLowerCase().startsWith("list")) {
                boolean res = listOptions(input);

                if (!res) {
                    System.out.println("The category you specified does not exist.");
                    // todo: show the categories that do exist?
                }

            }

            if (input.toLowerCase().startsWith("login")) {
                loginHelper(sc);
            }

            if (input.toLowerCase().startsWith("register")) {
                registerHelper(sc);
            }

            if (input.toLowerCase().startsWith("buyer")) {
                makePurchase(input);
            }

            if (input.toLowerCase().startsWith("seller")) {

            }

            if (input.toLowerCase().equals("help")) {
                System.out.println(
                        "\n--------------------------------------------------------HELP--------------------------------------------------------");
                System.out.println(
                        "\nlist [String: categories] : Categories include chocolate, drinks, candies, chips, or all.\n    Example:\n    > list chocolate\n            Mars (MA) QTY: 20 Price: $2\n            M&M (MM) QTY: 10 Price: $5\n            Bounty (BO) QTY: 5 Price: $1\n            Snickers (SN) QTY: 15 Price: 5\n");
                System.out.println(
                        "\nlogin : Command to login for an existing user.\n    Example:\n    > login\n            Enter username: myUsername\n            Enter password: myPassword\n            Successfully logged in!\n");
                System.out.println(
                        "\nregister : Command for a new user to create an account.\n    Example:\n    > Enter username: myUsername\n            Enter password: myPassword\n            Confirm password: myPassword\n            User successfully created!\n");
                System.out.println(
                        "\nbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]: Command allows buyer to purchase an item.\n    Example of a user buying 3 bottles of mineral water using 3x$5 notes and 3x50c coins :\n    > buyer cash 3 mw 50c*3,5*3\n            change 16.50 - 3.75 = $12.75\n            change breakdown ($5x2) ($2x1) (50cx1) (20cx1) (5cx1)\n    Example of a user buying 4 cans of Sprite using a credit card:\n    > buyer card 4 spr\n            card valid, transaction successful!\n");
                System.out.println(
                        "\nseller modify [String: itemName] [String: ItemCode] [Int: QTY] [Int: ItemPrice] : Seller is able to modify items.\n    Example of a seller chaning the item quantity of coke:\n    > seller modify qty coke 10\n            Success!\n");
                System.out.println(
                        "\nseller list [String: filetype]: Provides the seller with either a csv or txt file for the list of current items.\n    Example:\n    > seller list txt\n            ----------seller_report.txt----------\n            Mineral Water (MW) QTY: 20 Price: $2\n            Mars (MA) QTY: 12 Price: $5\n            Pringles (PR) QTY: 10 Price: $6\n            Mentos (MN) QTY: 15 Price: $3\n            -------------------------------------\n");
                System.out.println(
                        "\nseller summary [String: filetype]: Provides seller with the summary item history in csv or txt file.\n    Example:\n    > seller summary csv\n            ----------seller_summary.csv----------\n            Mineral Water (MW) Total sold: 2\n            Mars (MA) Total sold: 7\n            Pringles (PR) Total sold: 4\n            Mentos (MN) Total sold: 5\n            --------------------------------------\n");
                System.out.println(
                        "\n--------------------------------------------------------------------------------------------------------------------");
            }

            if (input.toLowerCase().equals("exit")) {
                break;
            }

            System.out.println("\nWhat would you like to do? (press help for instructions)");
            System.out.print(">");
        }

        sc.close();

    }

    public void registerHelper(Scanner sc) {

        System.out.print("Enter username: ");
        String account = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (this.db.getUserByAccountAndPassword(account, password) != null) {
            System.out.println("User already exists!");
            return;
        }

        // the result is either 0 (if error) or the new user id
        User newUser = this.db.insertIntoUsersTable(account, password, "buyer");

        if (newUser == null) {
            System.out.println("Error creating user.");
        } else {
            System.out.println("Account successfully created!");
            this.currentUser = newUser;
        }

    }

    public void loginHelper(Scanner sc) {

        System.out.print("Enter username: ");
        String account = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        // the result is either 0 (if error) or the userId
        User newUser = this.db.getUserByAccountAndPassword(account, password);

        if (newUser == null) {

            System.out.println("Invalid username or password!");

        } else {

            this.currentUser = newUser;
            System.out.println("Successfully logged in!");

        }

    }

    public boolean listOptions(String input) {

        String categoryInput[] = input.split(" ");
        String category;

        if (categoryInput.length == 1) {
            category = "all";
        } else {
            category = categoryInput[1];
        }

        boolean exist = false;

        for (Item i : this.db.getAllItems()) {
            if (category.toLowerCase().equals("all")) {
                exist = true;
                System.out.println(String.format("+ %s (%s) QTY: %d Price: $%.2f", i.getName(), i.getCode(),
                        i.getQuantity(), i.getPrice()));
            } else if (i.getCategory().toLowerCase().equals(category.toLowerCase())) {
                exist = true;
                System.out.println(String.format("+ %s (%s) QTY: %d Price: $%.2f", i.getName(), i.getCode(),
                        i.getQuantity(), i.getPrice()));
            }
        }

        return exist;
    }

    public void makePurchase(String input) {

        String userInput[] = input.split(" ");

        if (userInput.length < 4) {
            System.out.println("Invalid input! Please make sure your input is in the correct format:");
            System.out.println(
                    "\tbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]");
            return;
        }

        if (userInput[1].equals("cash")) {
            // return makeCashPurchase(userInput);
            return;
        } else if (userInput[1].equals("card")) {
            makeCardPurchase(userInput);
        } else {
            System.out.println("Invalid payment method!");
            return;
        }

    }

    public void makeCardPurchase(String userInput[]) {

        // buyer card 4 spr
        // buyer card 4 spr name number
        // buyer card 4 SP Sergio 42689

        int userId = this.currentUser == null ? -1 : this.currentUser.getId();

        if (userInput.length == 4 && (userId == -1 || this.currentUser.getCard() == -1)) {
            System.out.println("You do not have a card stored on file. Please specify one by using the format:");
            System.out.println("\tbuyer card [Int: QTY] [String: itemName] [String: cardName] [Int: cardNumber]");
            return;
        }

        if (userInput.length > 4) {

            if (userInput.length != 6) {
                System.out.println("Invalid input! Please make sure your input is in the correct format:");
                System.out.println(
                        "\tbuyer card [Int: QTY] [String: itemName] [String: cardName] [Int: cardNumber]");
                return;
            }

            String cardName = userInput[4];
            String cardNumber = userInput[5];

            int res;

            if (userId != -1) {
                res = setUserCard(cardName, cardNumber);
            } else {

                boolean flag = this.db.checkCardValidity(cardName, cardNumber);

                if (flag) {
                    res = 1;
                } else {
                    res = -2;
                }

            }

            if (res < 0) {

                LocalDate date = LocalDate.now();
                String reason = res == -1 ? "There was an error in associating card to user."
                        : "The card details provided were invalid.";

                this.db.createCancelledOrder(userId, date, reason);

                System.out.println("The order could not be processed. " + reason);

                return;

            }

        }

        String itemCode = userInput[3];
        int itemId = this.db.getItemIdByCode(itemCode);

        if (itemId == -1) {
            System.out.println("The item code provided was invalid. Please list the items to see the item codes.");
            return;
        }

        int quantity = Integer.parseInt(userInput[2]);
        int currentItemQuantity = this.db.getItemQuantityByCode(itemCode);

        if (currentItemQuantity < quantity) {

            LocalDate date = LocalDate.now();
            String reason = "There was not enough quantity of " + itemCode + " to fulfill the order.";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println("The order could not be processed. " + reason);

            return;

        }

        double totalCost = this.db.getItemPriceByCode(itemCode) * quantity;

        LocalDate date = LocalDate.now();
        int res = this.db.createOrder(userId, date, itemId, quantity, totalCost, 0, "card");

        if (res < 0) {
            System.out.println("Error in creating order!");
            return;
        }

        // edit quantity of item in db
        if (this.db.updateItemQuantity(itemId, this.db.getItemQuantityByCode(itemCode) - quantity) != itemId) {
            System.out.println("Error in updating item quantity!");
            return;
        }

        System.out.println("Items dispensed. Thank you for your purchase!");
        return;

    }

    public int setUserCard(String cardName, String cardNumber) {

        for (Card card : this.db.getAllCards()) {

            if (card.getName().equals(cardName) && card.getNumber().equals(cardNumber)) {

                int res = this.currentUser.associateCardWithUser(card, this.db);

                if (res == -1) {
                    System.out.println("Error associating card with user.");
                    return -1;
                } else if (res == -2) {
                    System.out.println(
                            "The card you have provided is invalid. You will not be able to make purchases until you provide a valid card.");
                    return -2;
                } else if (res == 2) {
                    System.out.println(
                            "The card you have provided is invalid. However, you have an existing card stored on your account. This card will be used for this transaction instead.");
                    return 2;
                } else {
                    System.out.println("Card successfully associated with user.");
                    return 1;
                }

            }

        }

        return -1;

    }

}
