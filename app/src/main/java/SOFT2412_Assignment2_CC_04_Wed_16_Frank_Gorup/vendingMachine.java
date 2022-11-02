package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.io.Console;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors

public class vendingMachine {
    ArrayList<Item> items;
    User currentUser;
    Database db;
    static Timer timer;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

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
        deleteFiles("./src/vendingMachine.db");

        // fake user
        this.db.insertIntoUsersTable("owner", "owner", "owner");
        this.db.insertIntoUsersTable("test", "test", "buyer");
        this.db.insertIntoUsersTable("seller", "seller", "seller");
        this.db.insertIntoUsersTable("cashier", "cashier", "cashier");

        // dummy items with quantities and prices defined
        this.db.insertIntoItemsTable("mineral Water", "mw", "drinks", 3, 20);
        this.db.insertIntoItemsTable("sprite", "sr", "drinks", 5, 10);
        this.db.insertIntoItemsTable("coca cola", "cc", "drinks", 5, 10);
        this.db.insertIntoItemsTable("pepsi", "pe", "drinks", 4, 10);
        this.db.insertIntoItemsTable("juice", "ju", "drinks", 3.5, 10);

        this.db.insertIntoItemsTable("mars", "ma", "chocolates", 5, 12);
        this.db.insertIntoItemsTable("m&m", "mm", "chocolates", 6, 12);
        this.db.insertIntoItemsTable("bounty", "bo", "chocolates", 5, 12);
        this.db.insertIntoItemsTable("snickers", "sn", "chocolates", 7, 12);

        this.db.insertIntoItemsTable("smiths", "sm", "chips", 5, 10);
        this.db.insertIntoItemsTable("pringles", "pr", "chips", 6, 10);
        this.db.insertIntoItemsTable("kettle", "ke", "chips", 5, 10);
        this.db.insertIntoItemsTable("thins", "th", "chips", 4, 10);

        this.db.insertIntoItemsTable("mentos", "me", "candies", 3, 15);
        this.db.insertIntoItemsTable("sour patch", "sp", "candies", 5, 12);
        this.db.insertIntoItemsTable("skittles", "sk", "candies", 6, 12);

        this.db.insertIntoChangeTable("5c", 10);
        this.db.insertIntoChangeTable("10c", 10);
        this.db.insertIntoChangeTable("20c", 10);
        this.db.insertIntoChangeTable("50c", 10);
        this.db.insertIntoChangeTable("1", 10);
        this.db.insertIntoChangeTable("2", 10);
        this.db.insertIntoChangeTable("5", 10);
        this.db.insertIntoChangeTable("10", 10);
        this.db.insertIntoChangeTable("20", 10);
        this.db.insertIntoChangeTable("50", 10);
        this.db.insertIntoChangeTable("100", 10);

    }

    public void runVendingMachine() {
        Scanner sc = new Scanner(System.in);
        File f = new File("reports");
        f.mkdir();

        // delete files
        deleteFiles("reports/availableItems.txt");
        deleteFiles("reports/summary.txt");
        deleteFiles("reports/usersList.txt");

        System.out.println(ANSI_PURPLE + "\nStarting vending machine..." + ANSI_RESET);
        ArrayList<Order> lastFiveOrders = this.db.getFiveMostRecentOrders(-1);

        lastFiveOrders = this.db.getFiveMostRecentOrders(this.currentUser == null ? -1 : this.currentUser.getId());

        if (lastFiveOrders.size() == 0) {
            System.out.println("\nNo recent orders have been made by anonymous users.\n");
        } else {
            System.out.println("\nThe most recent orders made by anonymous users:");
            System.out.println(
                    "--------------------------------------------------------------------------------------------------------------------");
            for (Order order : lastFiveOrders) {
                System.out.println(order);
            }
            System.out.println(
                    "---------------------------------------------------------------------------------------------------------------------\n");
        }

        System.out.println("What would you like to do anonymous user? (type help for instructions, exit to quit)");
        System.out.print("> ");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(ANSI_RED + "You have been logged out due to inactivity." + ANSI_RESET);
                System.exit(0);
            }
        }, 120000);

        while (sc.hasNext()) {

            timer.cancel();
            String input = sc.nextLine();

            if (input.toLowerCase().startsWith("list")) {
                boolean res = listOptions(input);

                if (!res) {
                    System.out.println(ANSI_RED + "The category you specified does not exist." + ANSI_RESET);
                }

            }

            else if (input.toLowerCase().startsWith("login")) {
                loginHelper(sc);
            }

            else if (input.toLowerCase().startsWith("register")) {
                registerHelper(sc);
            }

            else if (input.toLowerCase().startsWith("buyer")) {
                makePurchase(input, sc);
            }

            else if (input.toLowerCase().startsWith("cashier")) {
                if (currentUser == null || !currentUser.getRole().equals("cashier")) {
                    System.out.println(ANSI_RED + "Sorry you do not have cashier permission." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }

                String[] inputList = input.toLowerCase().split(" ");
                if (inputList.length < 2) {
                    System.out.println(ANSI_RED + "Missing inputs. Please try again." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }
                if (inputList[1].equals("modify")) {
                    if (inputList.length < 4) {
                        System.out.println(ANSI_RED + "Missing inputs. Please try again." + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    }
                    int quant;
                    try {
                        quant = Integer.parseInt(inputList[3]);
                    } catch (Exception e) {
                        System.out.println(ANSI_RED + "Invalid Input" + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    }

                    cashReplenishment(inputList[2], quant);
                }

                else if (inputList[1].equals("list")) {
                    cashierLs();
                }

                else if (inputList[1].equals("summary")) {
                    cashierSummary();
                }

                else {
                    System.out.println(ANSI_RED + "Invalid Input" + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }

            }

            else if (input.toLowerCase().startsWith("owner")) {
                ArrayList<User> users = db.getAllUsers();

                if (currentUser == null || !currentUser.hasOwnerPermissions()) {
                    System.out.println(ANSI_RED + "Sorry you do not have owner permission." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }
                String[] inputList = input.toLowerCase().split(" ");
                if (inputList.length < 2) {
                    System.out.println(ANSI_RED + "Missing inputs. Please try again." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }
                if (inputList[1].equals("list")) {
                    ownerLsUsersTerminal();
                    onwerLsCancelledOrders();
                } else if (inputList[1].equals("remove")) {
                    if (inputList.length < 4) {
                        System.out.println(ANSI_RED + "Missing inputs. Please try again." + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    } else if (!inputList[3].toLowerCase(Locale.ROOT).equals("owner")
                            && !inputList[3].toLowerCase(Locale.ROOT).equals("seller")
                            && !inputList[3].toLowerCase(Locale.ROOT).equals("cashier")) {
                        System.out.println(ANSI_RED + "Cannot remove the user with the role specified." + ANSI_RESET);
                    }

                    boolean suc = false;
                    for (User i : users) {
                        if (i.getAccount().equals(inputList[2]) && !inputList[2].equals(currentUser.getAccount())
                                && inputList[3].equals(i.getRole())) {
                            db.removeFromUsersTable(inputList[2]);
                            System.out.println("Successfully removed the user");
                            suc = true;
                            break;
                        }
                    }
                    if (!suc) {
                        System.out.println(
                                ANSI_RED + "Unsuccessful as an error has occurred. Please try again with valid input"
                                        + ANSI_RESET);
                    }
                } else if (inputList[1].equals("add")) {
                    if (inputList.length < 5) {
                        System.out.println(ANSI_RED + "Missing inputs. Please try again." + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    }

                    // Check whether account exist
                    for (User i : users) {
                        if (i.getAccount().equals(inputList[2])) {
                            System.out.println(ANSI_RED + "Account already exists." + ANSI_RESET);
                            System.out
                                    .println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                            System.out.print("> ");
                            continue;
                        }
                    }

                    if (!inputList[4].toLowerCase(Locale.ROOT).equals("owner")
                            && !inputList[4].toLowerCase(Locale.ROOT).equals("seller")
                            && !inputList[4].toLowerCase(Locale.ROOT).equals("cashier")) {
                        System.out.println(ANSI_RED + "Cannot add the user with the role specified." + ANSI_RESET);
                    }

                    db.insertIntoUsersTable(inputList[2], inputList[3], inputList[4]);
                    System.out.println("Success");
                } else {
                    System.out.println(ANSI_RED + "Invalid command." + ANSI_RESET);
                }
            }

            else if (input.toLowerCase().startsWith("seller")) {
                if (currentUser == null || !currentUser.hasSellerPermissions()) {
                    System.out.println(ANSI_RED + "ERROR: Sorry you do not have seller permission." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }
                String[] inputList = input.toLowerCase().split(" ");
                if (inputList.length < 2) {
                    System.out.println(ANSI_RED + "ERROR: Missing inputs. Please try again." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }

                if (inputList[1].equals("list")) {
                    sellerLs();
                } else if (inputList[1].equals("summary")) {
                    sellerSummary();
                }

                else if (inputList[1].equals("modify")) {
                    if (inputList.length < 5) {
                        System.out.println(ANSI_RED + "ERROR: Missing inputs. Please try again." + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    }

                    String toModify = inputList[2];
                    String item = inputList[3];
                    String[] newArray = Arrays.copyOfRange(inputList, 4, inputList.length);
                    // String changeInto = inputList[4];
                    if (toModify.equals("qty")) {
                        db.sellerModifyQuantity(toModify, item, newArray[0]);
                    } else if (toModify.equals("name")) {
                        db.sellerModifyName(toModify, item, newArray);
                        ;
                    } else if (toModify.equals("category")) {
                        db.sellderModifyCategory(toModify, item, newArray);
                        ;
                    } else if (toModify.equals("code")) {
                        if (newArray.length > 1) {
                            System.out.println(ANSI_RED + "ERROR: Invalid input, code must be one word" + ANSI_RESET);
                        } else {
                            db.sellderModifyCode(toModify, item, newArray[0]);
                            ;
                        }
                    } else if (toModify.equals("price")) {
                        if (newArray.length > 1) {

                            System.out
                                    .println(ANSI_RED + "ERROR: Invalid input, please only input 1 price" + ANSI_RESET);
                        } else {
                            db.sellderModifyPrice(toModify, item, newArray[0]);
                        }
                    } else {
                        System.out.println(ANSI_RED + "ERROR: Invalid inputs. Please try again." + ANSI_RESET);
                        System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                        System.out.print("> ");
                        continue;
                    }
                }

                else {
                    System.out.println(ANSI_RED + "ERROR: Invalid inputs. Please try again." + ANSI_RESET);
                    System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                    System.out.print("> ");
                    continue;
                }
            }

            else if (input.toLowerCase().startsWith("help")) {
                helpOptions(input);
            }

            else if (input.toLowerCase().startsWith("logout")) {
                logoutUser();
            }

            else if (input.toLowerCase().equals("exit")) {
                System.out.println(ANSI_PURPLE + "\nShutting down vending machine..." + ANSI_RESET);
                break;
            }

            else {
                System.out.println(ANSI_RED + "Invalid Input. Please try again." + ANSI_RESET);
                System.out.println("\nWhat would you like to do? (type help for instructions, exit to quit)");
                System.out.print("> ");
                continue;
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("You have been logged out due to inactivity.");
                    System.exit(0);
                }
            }, 120000);

            String name = this.currentUser == null ? "anonymous user" : this.currentUser.getAccount();

            System.out.println("\nWhat would you like to do " + name + "? (type help for instructions, exit to quit)");
            System.out.print("> ");
        }

        sc.close();

    }

    public void ownerLsUsersTerminal() {
        System.out.println("----------users list.txt----------");
        ArrayList<User> users = db.getAllUsers();

        for (User i : users) {
            String message = String.format("%s | Role : %s", i.getAccount(), i.getRole());
            System.out.println(message);
        }
        System.out.println("-------------------------------------\n");
    }

    public void ownerLsUsersTxt() {
        try {
            FileWriter myWriter = new FileWriter("reports/usersList.txt");
            ArrayList<User> users = db.getAllUsers();

            for (User i : users) {
                String message = String.format("%s | Role : %s\r\n", i.getAccount(), i.getRole());
                myWriter.write(message);
            }

            myWriter.close();

        } catch (IOException e) {
            System.out.println(ANSI_RED + "An error occurred." + ANSI_RESET);
            e.printStackTrace();
        }

    }

    public void onwerLsCancelledOrders() {
        System.out.println("---------------cancelled orders list.txt---------------");
        ArrayList<Order> orders = db.getAllCancelled();

        if (orders != null) {
            for (Order i : orders) {
                String message = String.format("Order ID: %s | Order User: %s | Date: %s | Reason: %s", i.getId(),
                        i.getUserId(), i.getDate(), i.getPaymentMethod());
                System.out.println(message);
            }
        }
        System.out.println("------------------------------------------------------\n");
    }

    public void sellerLs() {
        System.out.println("----------seller_report.txt----------");
        ArrayList<Item> items = db.getAllItems();

        for (Item i : items) {
            String message = String.format("%s (%s) QTY: %s Price: %s", i.getName(), i.getCode(), i.getQuantity(),
                    i.getPrice());
            System.out.println(message);
        }

        System.out.println("-------------------------------------");
    }

    public void sellerSummary() {
        System.out.println("----------seller_summary.txt----------");
        ArrayList<Order> orders = db.getOrders();
        ArrayList<Item> items = db.getAllItems();

        for (Item i : items) {
            int quant = 0;
            for (Order o : orders) {
                if (o.getItemId() == i.getId()) {
                    quant += o.getQuantity();
                }
            }

            String message = String.format("%s (%s) Total sold: %d", i.getName(), i.getCode(), quant);
            System.out.println(message);
        }

        System.out.println("--------------------------------------");
    }

    public void getSellerReport() {
        try {
            FileWriter myWriter = new FileWriter("reports/availableItems.txt");
            ArrayList<Item> items = db.getAllItems();

            for (Item i : items) {
                String message = String.format("%s (%s): category: %s, price: %s, quantity: %s\r\n", i.getName(),
                        i.getCode(), i.getCategory(), i.getPrice(), i.getQuantity());
                myWriter.write(message);
            }

            myWriter.close();

            FileWriter summary = new FileWriter("reports/summary.txt");
            ArrayList<Order> orders = db.getOrders();

            if (orders == null) {
                for (Item i : items) {
                    String message = String.format("%s (%s) | quantity sold 0\r\n", i.getName(), i.getCode());
                    summary.write(message);
                }
                summary.close();
                return;

            } else {
                for (Item i : items) {
                    int quant = 0;
                    for (Order o : orders) {
                        if (o.getItemId() == i.getId()) {
                            quant += o.getQuantity();
                        }
                    }
                    String message = String.format("%s (%s) | quantity sold %s\n", i.getName(), i.getCode(), quant);
                    summary.write(message);
                }
            }

            summary.close();

        } catch (IOException e) {
            System.out.println(ANSI_RED + "An error occurred." + ANSI_RESET);
            e.printStackTrace();
        }
    }

    public void cashierLs() {
        System.out.println("----------cashier_report.txt----------");
        ArrayList<Item> items = db.getAllItems();

        String[] denominations = { "100", "50", "20", "10", "5", "2", "1", "50c", "20c", "10c", "5c" };

        for (String i : denominations) {
            int denominationQuantity = db.getAmountOfChangeForDenomination(i);
            if (i.charAt(i.length() - 1) == 'c') {
                String message = String.format("Demonination: " + i + " | Quantity: " + denominationQuantity);
                System.out.println(message);
            } else { // if denomination if not a coin, add a $ in front of it
                String message = String.format("Demonination: $" + i + " | Quantity: " + denominationQuantity);
                System.out.println(message);
            }
        }
        System.out.println("-------------------------------------");
    }

    public void cashierSummary() {

        ArrayList<Order> orders = this.db.getOrders();
        String purchasedItemCode = "-";

        if (orders == null) {
            System.out.println("---------- no orders have been placed ----------");
        }

        else {
            System.out.println("-------------------cashier_summary---------------------");
            for (Order order : orders) {
                for (Item i : items) {
                    if (i.getId() == order.getItemId()) {
                        purchasedItemCode = i.getCode();
                    }
                }
                String message = String.format("%s (%s) | Paid: %s, Returned: %s, Method: %s\r", order.getDate(),
                        purchasedItemCode, order.getAmountPaid(), order.getChange(), order.getPaymentMethod());
                System.out.println(message);
            }
            System.out.println("--------------------------------------------------------");
        }
    }

    public void getCashierReport() {
        try {
            FileWriter writeChange = new FileWriter("reports/cashierAvailableChange.txt");
            ArrayList<Item> items = db.getAllItems();

            String[] denominations = { "100", "50", "20", "10", "5", "2", "1", "50c", "20c", "10c", "5c" };

            for (String i : denominations) {
                int denominationQuantity = db.getAmountOfChangeForDenomination(i);
                if (i.charAt(i.length() - 1) == 'c') {
                    String message = String
                            .format("Demonination: " + i + " | Quantity: " + denominationQuantity + "\n");
                    writeChange.write(message);
                } else { // if denomination if not a coin, add a $ in front of it
                    String message = String
                            .format("Demonination: $" + i + " | Quantity: " + denominationQuantity + "\n");
                    writeChange.write(message);
                }
            }

            writeChange.close();

            FileWriter summary = new FileWriter("reports/cashierSummary.txt");
            ArrayList<Order> orders = this.db.getOrders();

            String purchasedItemCode = "-";

            if (orders == null) {
                summary.write("---------- no orders have been placed ----------");
            }

            else {
                for (Order order : orders) {
                    for (Item i : items) {
                        if (i.getId() == order.getItemId()) {
                            purchasedItemCode = i.getCode();
                        }
                    }
                    String message = String.format("%s (%s) | Paid: %s, Returned: %s, Method: %s\r\n", order.getDate(),
                            purchasedItemCode, order.getAmountPaid(), order.getChange(), order.getPaymentMethod());
                    summary.write(message);
                }
            }

            summary.close();

        } catch (IOException e) {
            System.out.println(ANSI_RED + "An error occurred." + ANSI_RESET);
            e.printStackTrace();
        }
    }

    public int cashReplenishment(String denomination, int quantity) {
        if (quantity < 0) {
            System.out.println(ANSI_RED + "quantity cannot be negative." + ANSI_RESET);
            return 0;
        }

        if (db.getAmountOfChangeForDenomination(denomination) == -1) {
            System.out.println(ANSI_RED + "cash denomination does not exist." + ANSI_RESET);
            return 0;
        }

        if (db.updateChangeForDenomination(denomination, quantity) == 0) {
            return 0;
        } else {
            System.out.printf("Success! There are now %dx%s in the vending machine%n", quantity, denomination);
            return 1;
        }
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
            System.out.println(ANSI_RED + "Error creating user." + ANSI_RESET);
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
        User newUser = null;
        newUser = this.db.getUserByAccountAndPassword(account, password);

        if (newUser == null) {

            System.out.println(ANSI_RED + "Invalid username or password!" + ANSI_RESET);

        } else {

            this.currentUser = newUser;
            System.out.println("Successfully logged in!");

            if (this.currentUser.hasSellerPermissions()) {
                getSellerReport();
                System.out.println("check availableItems.txt and summary.txt for seller reports");
            } else if (this.currentUser.hasCashierPermissions()) {
                getCashierReport();
                System.out.println("check cashierAvailableChange.txt and cashierSummary.txt for cashier reports");
            } else if (this.currentUser.hasOwnerPermissions()) {
                ownerLsUsersTxt();
                // onwerLsCancelledOrders();
                System.out.println("check usersList.txt for owner reports");
            }

            else {
                // delete files
                deleteFiles("reports/availableItems.txt");
                deleteFiles("reports/summary.txt");
                deleteFiles("reports/usersList.txt");
            }
        }
    }

    public void deleteFiles(String name) {
        Path path = FileSystems.getDefault().getPath(name);
        try {
            Files.delete(path);
        } catch (Exception x) {
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

    public void helpOptions(String input) {

        String helpInput[] = input.split(" ");
        String help;

        if (helpInput.length == 1) {
            help = "all";
        } else {
            help = helpInput[1];
        }

        if (help.toLowerCase().equals("all")) {
            System.out.println(
                    "\n-------------------------------------------------HELP COMMANDS-------------------------------------------------");
            System.out.println(
                    "\n1. HELP LIST");
            System.out.println(
                    "\n2. HELP LOGIN");
            System.out.println(
                    "\n3. HELP REGISTER");
            System.out.println(
                    "\n4. HELP BUYER");
            System.out.println(
                    "\n5. HELP SELLER");
            System.out.println(
                    "\n6. HELP CASHIER");
            System.out.println(
                    "\n7. HELP OWNER");

            System.out.println(
                    "\n--------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("list")) {
            System.out.println(
                    "\n--------------------------------------------------HELP LIST---------------------------------------------------");
            System.out.println(
                    "\nlist [String: categories] : Categories include chocolates, drinks, candies, chips, or all.\n    Example:\n    > list chocolates\n            Mars (MA) QTY: 20 Price: $2\n            M&M (MM) QTY: 10 Price: $5\n            Bounty (BO) QTY: 5 Price: $1\n            Snickers (SN) QTY: 15 Price: 5\n");
            System.out.println(
                    "\n---------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("login")) {
            System.out.println(
                    "\n---------------------------------------------------HELP LOGIN---------------------------------------------------");
            System.out.println(
                    "\nlogin : Command to login for an existing user.\n    Example:\n    > login\n            Enter username: myUsername\n            Enter password: myPassword\n            Successfully logged in!\n");
            System.out.println(
                    "\n----------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("register")) {
            System.out.println(
                    "\n--------------------------------------------------HELP REGISTER--------------------------------------------------");
            System.out.println(
                    "\nregister : Command for a new user to create an account.\n    Example:\n    > Enter username: myUsername\n            Enter password: myPassword\n            Confirm password: myPassword\n            User successfully created!\n");
            System.out.println(
                    "\n-----------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("buyer")) {
            System.out.println(
                    "\n----------------------------------------------------HELP BUYER----------------------------------------------------");
            System.out.println(
                    "\nbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]: Command allows buyer to purchase an item.\n    Example of a user buying 3 bottles of mineral water using 3x$5 notes and 3x50c coins :\n    > buyer cash 3 mw 50c*3,5*3\n            change 16.50 - 3.75 = $12.75\n            change breakdown ($5x2) ($2x1) (50cx1) (20cx1) (5cx1)\n    Example of a user buying 4 cans of Sprite using a credit card:\n    > buyer card 4 spr\n            card valid, transaction successful!\n");
            System.out.println(
                    "\n------------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("seller")) {
            System.out.println(
                    "\n---------------------------------------------------HELP SELLER---------------------------------------------------");
            System.out.println(
                    "\nseller modify [qty|price|name|code|category] [String: ItemCode] [QTY | price | name | code | category] : Seller is able to modify items.\n    Example of a seller changing the item quantity of coke:\n    > seller modify qty cc 10\n            Success!\n");
            System.out.println(
                    "\nseller list : Provides the seller with txt file for the list of current items and prints into the terminal.\n    Example:\n    > seller list\n            ----------seller_report.txt----------\n            Mineral Water (MW) QTY: 20 Price: $2\n            Mars (MA) QTY: 12 Price: $5\n            Pringles (PR) QTY: 10 Price: $6\n            Mentos (MN) QTY: 15 Price: $3\n            -------------------------------------\n");
            System.out.println(
                    "\nseller summary : Provides seller with the summary item history in txt file and prints into the terminal.\n    Example:\n    > seller summary\n            ----------seller_summary.txt----------\n            Mineral Water (MW) Total sold: 2\n            Mars (MA) Total sold: 7\n            Pringles (PR) Total sold: 4\n            Mentos (MN) Total sold: 5\n            --------------------------------------\n");
            System.out.println(
                    "\n------------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("cashier")) {
            System.out.println(
                    "\n---------------------------------------------------HELP CASHIER---------------------------------------------------");
            System.out.println(
                    "\ncashier modify [String: coin/note denomination] [String: new quantity] : Cashier is able to modify the quantity of a specific coin/note in the vending machine.\n    Example of a cashier changing the quantity of 50c:\n    > cashier modify 50c 10\n            Success! There are now 10x50c in the vending machine\n    Example of a cashier changing the quantity of $20:\n    > cashier modify 20 10\n            Success! There are now 10x$20 in the vending machine\n");
            System.out.println(
                    "\ncashier list : Provides the cashier with a list of available change, including the quantity of each coin and note in the vending machine.\n    Example:\n    > cashier list\n            ----------cashierAvailableChange.txt----------\n            Demonination: $100 | Quantity: 10\n            Demonination: $50 | Quantity: 10\n            Demonination: $20 | Quantity: 9\n            Demonination: $10 | Quantity: 8\n            ----------------------------------------------\n");
            System.out.println(
                    "\ncashier summary: Provides the cashier with a summary of transactions, including transaction date and time, item sold, money paid, change, and payment method.\n    Example:\n    > cashier summary\n            --------------------cashierSummary.txt--------------------\n            2022-11-01 (sp) | Paid: 20.0, Returned: 15.0, Method: cash\n            2022-11-01 (pe) | Paid: 5.0, Returned: 1.0, Method: cash\n            2022-11-01 (ju) | Paid: 10.0, Returned: 3.0, Method: cash\n            2022-10-31 (mm) | Paid: 20.0, Returned: 2.0, Method: cash\n            ----------------------------------------------------------\n");
            System.out.println(
                    "\n------------------------------------------------------------------------------------------------------------------");
        } else if (help.toLowerCase().equals("owner")) {
            System.out.println(
                    "\n---------------------------------------------------HELP OWNER---------------------------------------------------");
            System.out.println(
                    "\nowner remove [String: role] [String: account name] : Seller is able to remove a seller or cashier or owner that is not the owner itself.\n    Example:\n    > owner remove seller seller\n            Success!\n");
            System.out.println(
                    "\nowner list : Provides the seller with txt file for the list of users and prints into the terminal.\n    Example:\n    > owner list\n            ----------users list.txt----------\n            owner | Role : owner\n            test | Role : buyer\n            ----------------------------------\n");
            System.out.println(
                    "\nowner add [String: account name] [String: password] [String: role]: Seller is able to add a seller or cashier or owner.\n    Example:\n    > owner add account password seller\n            Success\n");
            System.out.println(
                    "\n------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(ANSI_RED + "Invalid command. Please try again." + ANSI_RESET);
        }

    }

    public void makePurchase(String input, Scanner sc) {

        String userInput[] = input.split(" ");

        if (userInput.length < 4) {
            System.out.println(
                    ANSI_RED + "Invalid input! Please make sure your input is in the correct format:" + ANSI_RESET);
            System.out.println(
                    "\tbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]");
            return;
        }

        if (userInput[1].equals("cash")) {
            makeCashPurchase(userInput, sc);
        } else if (userInput[1].equals("card")) {
            makeCardPurchase(userInput, sc);
        } else {
            System.out.println(ANSI_RED + "Invalid payment method!" + ANSI_RESET);
            return;
        }

    }

    public void makeCashPurchase(String userInput[], Scanner sc) {

        // buyer cash 3 SP 50c*3,5*3

        int userId = this.currentUser == null ? -1 : this.currentUser.getId();

        if (userInput.length < 5) {
            System.out.println(
                    ANSI_RED + "Invalid input! Please make sure your input is in the correct format:" + ANSI_RESET);
            System.out.println(
                    ANSI_RED + "\tbuyer [String: paymentMethod] [Int: QTY] [String: itemName] [ (if Cash, Comma Delimiter): num*amount]"
                            + ANSI_RESET);
            return;
        }

        String itemCode = userInput[3];
        int itemId = this.db.getItemIdByCode(itemCode);

        if (itemId == -1) {
            System.out.println(ANSI_RED
                    + "The item code provided was invalid. Please list the items to see the item codes." + ANSI_RESET);
            return;
        }

        int quantity = Integer.parseInt(userInput[2]);
        // System.out.println("YOU WANT TO PURCHASE " + quantity + " ITEMS");
        int currentItemQuantity = this.db.getItemQuantityByCode(itemCode);

        if (quantity <= 0) {

            LocalDate date = LocalDate.now();
            String reason = "Invalid quantity provided";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println(ANSI_RED + "The order could not be processed. " + reason + ANSI_RESET);

            logoutUser();

            return;

        }

        if (currentItemQuantity < quantity) {

            LocalDate date = LocalDate.now();
            String reason = "There was not enough quantity of " + itemCode + " to fulfill the order.";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println("The order could not be processed. " + reason);

            logoutUser();

            return;

        }

        double totalCost = this.db.getItemPriceByCode(itemCode) * quantity;

        int fiveCents = 0;
        int tenCents = 0;
        int twentyCents = 0;
        int fiftyCents = 0;
        int oneDollar = 0;
        int twoDollars = 0;
        int fiveDollars = 0;
        int tenDollars = 0;
        int twentyDollars = 0;
        int fiftyDollars = 0;
        int hundredDollars = 0;

        int[] cash = Backend.sumCash(userInput[4].split(","), fiveCents, tenCents, twentyCents, fiftyCents, oneDollar,
                twoDollars,
                fiveDollars, tenDollars,
                twentyDollars, fiftyDollars, hundredDollars);

        if (cash == null) {
            System.out.println(ANSI_RED + "Invalid cash input Please make sure you are only using valid denominations: "
                    + ANSI_RESET);
            System.out.println(ANSI_RED + "\t5c, 10c, 20c, 50c, 1, 2, 5, 10, 20, 50, 100" + ANSI_RESET);
            return;
        }

        fiveCents += cash[0];
        tenCents += cash[1];
        twentyCents += cash[2];
        fiftyCents += cash[3];
        oneDollar += cash[4];
        twoDollars += cash[5];
        fiveDollars += cash[6];
        tenDollars += cash[7];
        twentyDollars += cash[8];
        fiftyDollars += cash[9];
        hundredDollars += cash[10];

        double totalCash = fiveCents * 0.05 + tenCents * 0.1 + twentyCents * 0.2 + fiftyCents * 0.5 + oneDollar * 1
                + twoDollars * 2 + fiveDollars * 5 + tenDollars * 10 + twentyDollars * 20 + fiftyDollars * 50
                + hundredDollars * 100;

        if (totalCash < totalCost) {
            System.out.println("The total cash you have provided was not enough to cover the cost of the order.");
            System.out.print("Please enter more cash to meet the cost of the order: ");

            String moreCash = sc.nextLine();
            String initialCashInput = userInput[4];
            String newCashInput = initialCashInput + "," + moreCash;

            String newInput = userInput[0] + " " + userInput[1] + " " + userInput[2] + " " + userInput[3] + " "
                    + newCashInput;

            makeCashPurchase(newInput.split(" "), sc);
            return;

        }

        // figure out the change
        double change = calculateChange(totalCash, totalCost, userId);

        if (change >= 0) {

            LocalDate date = LocalDate.now();

            int res = this.db.createOrder(userId, date, itemId, quantity, totalCash, change, "cash");

            if (res < 0) {
                System.out.println(ANSI_RED + "Error in creating order!" + ANSI_RESET);
                return;
            }

            // edit quantity of item in db
            if (this.db.updateItemQuantity(itemId, this.db.getItemQuantityByCode(itemCode) - quantity) != itemId) {
                System.out.println(ANSI_RED + "Error in updating item quantity!" + ANSI_RESET);
                return;
            }

            logoutUser();

        }

    }

    public double calculateChange(double totalCash, double totalCost, int userId) {

        double change = totalCash - totalCost;

        int fiveCents = 0;
        int tenCents = 0;
        int twentyCents = 0;
        int fiftyCents = 0;
        int oneDollar = 0;
        int twoDollars = 0;
        int fiveDollars = 0;
        int tenDollars = 0;
        int twentyDollars = 0;
        int fiftyDollars = 0;
        int hundredDollars = 0;

        int totalFiveCents = this.db.getAmountOfChangeForDenomination("5c");
        int totalTenCents = this.db.getAmountOfChangeForDenomination("10c");
        int totalTwentyCents = this.db.getAmountOfChangeForDenomination("20c");
        int totalFiftyCents = this.db.getAmountOfChangeForDenomination("50c");
        int totalOneDollar = this.db.getAmountOfChangeForDenomination("1");
        int totalTwoDollars = this.db.getAmountOfChangeForDenomination("2");
        int totalFiveDollars = this.db.getAmountOfChangeForDenomination("5");
        int totalTenDollars = this.db.getAmountOfChangeForDenomination("10");
        int totalTwentyDollars = this.db.getAmountOfChangeForDenomination("20");
        int totalFiftyDollars = this.db.getAmountOfChangeForDenomination("50");
        int totalHundredDollars = this.db.getAmountOfChangeForDenomination("100");

        boolean skipHundredDollars = false;
        boolean skipFiftyDollars = false;
        boolean skipTwentyDollars = false;
        boolean skipTenDollars = false;
        boolean skipFiveDollars = false;
        boolean skipTwoDollars = false;
        boolean skipOneDollar = false;
        boolean skipFiftyCents = false;
        boolean skipTwentyCents = false;
        boolean skipTenCents = false;

        while (change > 0) {

            // account for floating point precision errors
            change = Double.parseDouble(df.format(change));

            if (change >= 100 && !skipHundredDollars) {

                if (totalHundredDollars > 0) {
                    hundredDollars++;
                    totalHundredDollars--;
                    change -= 100.00;
                } else {
                    skipHundredDollars = true;
                }

            } else if (change >= 50 && !skipFiftyDollars) {

                if (totalFiftyDollars > 0) {
                    fiftyDollars++;
                    totalFiftyDollars--;
                    change -= 50.00;
                } else {
                    skipFiftyDollars = true;
                }

            } else if (change >= 20 && !skipTwentyDollars) {

                if (totalTwentyDollars > 0) {
                    twentyDollars++;
                    totalTwentyDollars--;
                    change -= 20.00;
                } else {
                    skipTwentyDollars = true;
                }

            } else if (change >= 10 && !skipTenDollars) {

                if (totalTenDollars > 0) {
                    tenDollars++;
                    totalTenDollars--;
                    change -= 10.00;
                } else {
                    skipTenDollars = true;
                }

            } else if (change >= 5 && !skipFiveDollars) {

                if (totalFiveDollars > 0) {
                    fiveDollars++;
                    totalFiveDollars--;
                    change -= 5.00;
                } else {
                    skipFiveDollars = true;
                }

            } else if (change >= 2 && !skipTwoDollars) {

                if (totalTwoDollars > 0) {
                    twoDollars++;
                    totalTwoDollars--;
                    change -= 2.00;
                } else {
                    skipTwoDollars = true;
                }

            } else if (change >= 1 && !skipOneDollar) {

                if (totalOneDollar > 0) {
                    oneDollar++;
                    totalOneDollar--;
                    change -= 1.00;
                } else {
                    skipOneDollar = true;
                }

            } else if (change >= 0.5 && !skipFiftyCents) {

                if (totalFiftyCents > 0) {
                    fiftyCents++;
                    totalFiftyCents--;
                    change -= 0.50;
                } else {
                    skipFiftyCents = true;
                }

            } else if (change >= 0.2 && !skipTwentyCents) {

                if (totalTwentyCents > 0) {
                    twentyCents++;
                    totalTwentyCents--;
                    change -= 0.20;
                } else {
                    skipTwentyCents = true;
                }

            } else if (change >= 0.1 && !skipTenCents) {

                if (totalTenCents > 0) {
                    tenCents++;
                    totalTenCents--;
                    change -= 0.10;
                } else {
                    skipTenCents = true;
                }

            } else if (change >= 0.05) {

                if (totalFiveCents > 0) {
                    fiveCents++;
                    totalFiveCents--;
                    change -= 0.05;
                } else {
                    break;
                }

            } else {

                break;

            }

        }

        if (change > 0) {

            LocalDate date = LocalDate.now();
            String reason = "Not enough change to perform transaction.";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println("The order could not be processed. " + reason);

            logoutUser();

            return -1;

        }

        System.out.println("Order successful!");
        System.out.println("change " + totalCash + " - " + totalCost + " = " + "$" + df.format(totalCash - totalCost));

        if ((totalCash - totalCost) == 0) {
            System.out.println("No change required.");
            return 0;
        }

        System.out.print("change breakdown");

        if (hundredDollars > 0) {
            System.out.print(" ($100 x " + hundredDollars + ") ");
            this.db.updateChangeForDenomination("100", this.db.getAmountOfChangeForDenomination(
                    "100") - hundredDollars);
        }
        if (fiftyDollars > 0) {
            System.out.print(" ($50 x " + fiftyDollars + ") ");
            this.db.updateChangeForDenomination("50", this.db.getAmountOfChangeForDenomination(
                    "50") - fiftyDollars);
        }
        if (twentyDollars > 0) {
            System.out.print(" ($20 x " + twentyDollars + ") ");
            this.db.updateChangeForDenomination("20", this.db.getAmountOfChangeForDenomination(
                    "20") - twentyDollars);
        }
        if (tenDollars > 0) {
            System.out.print(" ($10 x " + tenDollars + ") ");
            this.db.updateChangeForDenomination("10", this.db.getAmountOfChangeForDenomination(
                    "10") - tenDollars);
        }
        if (fiveDollars > 0) {
            System.out.print(" ($5 x " + fiveDollars + ") ");
            this.db.updateChangeForDenomination("5", this.db.getAmountOfChangeForDenomination(
                    "5") - fiveDollars);
        }
        if (twoDollars > 0) {
            System.out.print(" ($2 x " + twoDollars + ") ");
            this.db.updateChangeForDenomination("2", this.db.getAmountOfChangeForDenomination(
                    "2") - twoDollars);
        }
        if (oneDollar > 0) {
            System.out.print(" ($1 x " + oneDollar + ") ");
            this.db.updateChangeForDenomination("1", this.db.getAmountOfChangeForDenomination(
                    "1") - oneDollar);
        }
        if (fiftyCents > 0) {
            System.out.print(" (50c x " + fiftyCents + ") ");
            this.db.updateChangeForDenomination("50c", this.db.getAmountOfChangeForDenomination(
                    "50c") - fiftyCents);
        }
        if (twentyCents > 0) {
            System.out.print(" (20c x " + twentyCents + ") ");
            this.db.updateChangeForDenomination("20c", this.db.getAmountOfChangeForDenomination(
                    "20c") - twentyCents);
        }
        if (tenCents > 0) {
            System.out.print(" (10c x " + tenCents + ") ");
            this.db.updateChangeForDenomination("10c", this.db.getAmountOfChangeForDenomination(
                    "10c") - tenCents);
        }
        if (fiveCents > 0) {
            System.out.print(" (5c x " + fiveCents + ") ");
            this.db.updateChangeForDenomination("5c", this.db.getAmountOfChangeForDenomination(
                    "5c") - fiveCents);
        }

        System.out.println();

        return (totalCash - totalCost);

    }

    public void makeCardPurchase(String userInput[], Scanner sc) {

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
                System.out.println(
                        ANSI_RED + "Invalid input! Please make sure your input is in the correct format:" + ANSI_RESET);
                System.out.println(
                        ANSI_RED + "\tbuyer card [Int: QTY] [String: itemName] [String: cardName] [Int: cardNumber]"
                                + ANSI_RESET);
                return;
            }

            String cardName = userInput[4];
            String cardNumber = userInput[5];

            int res;

            if (userId != -1) {

                System.out.print("Do you want to store this card on file? (Y/N): ");

                String storeCard = sc.nextLine();

                if (storeCard.equals("Y") || storeCard.equals("y")) {

                    System.out.println("Storing card on file...");

                    res = setUserCard(cardName, cardNumber);

                } else {

                    System.out.println("Not storing card on file...");

                    boolean flag = this.db.checkCardValidity(cardName, cardNumber);

                    if (flag) {
                        res = 1;
                    } else {
                        res = -2;
                    }

                }

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

                System.out.println(ANSI_RED + "The order could not be processed. " + reason + ANSI_RESET);

                logoutUser();

                return;

            }

        }

        String itemCode = userInput[3];
        int itemId = this.db.getItemIdByCode(itemCode);

        if (itemId == -1) {
            System.out.println(ANSI_RED
                    + "The item code provided was invalid. Please list the items to see the item codes." + ANSI_RESET);
            return;
        }

        int quantity = Integer.parseInt(userInput[2]);
        int currentItemQuantity = this.db.getItemQuantityByCode(itemCode);

        if (quantity <= 0) {

            LocalDate date = LocalDate.now();
            String reason = "Invalid quantity provided";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println(ANSI_RED + "The order could not be processed. " + reason + ANSI_RESET);

            logoutUser();

            return;

        }

        if (currentItemQuantity < quantity) {

            LocalDate date = LocalDate.now();
            String reason = "There was not enough quantity of " + itemCode + " to fulfill the order.";

            this.db.createCancelledOrder(userId, date, reason);

            System.out.println("The order could not be processed. " + reason);

            logoutUser();

            return;

        }

        double totalCost = this.db.getItemPriceByCode(itemCode) * quantity;

        LocalDate date = LocalDate.now();
        int res = this.db.createOrder(userId, date, itemId, quantity, totalCost, 0, "card");

        if (res < 0) {
            System.out.println(ANSI_RED + "Error in creating order!" + ANSI_RESET);
            return;
        }

        // edit quantity of item in db
        if (this.db.updateItemQuantity(itemId, this.db.getItemQuantityByCode(itemCode) - quantity) != itemId) {
            System.out.println(ANSI_RED + "Error in updating item quantity!" + ANSI_RESET);
            return;
        }

        System.out.println("card valid, transaction successful!");
        logoutUser();
        return;

    }

    public int setUserCard(String cardName, String cardNumber) {

        for (Card card : this.db.getAllCards()) {

            if (card.getName().equals(cardName) && card.getNumber().equals(cardNumber)) {

                int res = this.currentUser.associateCardWithUser(card, this.db);

                if (res == -1) {
                    System.out.println(ANSI_RED + "Error associating card with user." + ANSI_RESET);
                    return -1;
                } else if (res == -2) {
                    System.out.println(
                            ANSI_RED + "The card you have provided is invalid. You will not be able to make purchases until you provide a valid card."
                                    + ANSI_RESET);
                    return -2;
                } else if (res == 2) {
                    System.out.println(
                            ANSI_RED + "The card you have provided is invalid. However, you have an existing card stored on your account. This card will be used for this transaction instead."
                                    + ANSI_RESET);
                    return 2;
                } else {
                    System.out.println("Card successfully associated with user.");
                    return 1;
                }

            }

        }

        return -1;

    }

    public void logoutUser() {
        this.currentUser = null;
        System.out.println("Logged out");

        deleteFiles("reports/availableItems.txt");
        deleteFiles("reports/summary.txt");
        deleteFiles("reports/usersList.txt");
        deleteFiles("reports/cashierAvailableChange.txt");
        deleteFiles("reports/cashierSummary.txt");
    }

}
