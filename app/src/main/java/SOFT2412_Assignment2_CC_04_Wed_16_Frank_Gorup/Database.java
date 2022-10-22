package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This is a calss for functions concerning the database.
 */
public class Database {

  private static final String dbName = "vendingMachine.db";
  private static final String dbURL = "jdbc:sqlite:" + dbName;

  /**
   * This method creates the DB.
   */
  public void createDB() {

    File dbFile = new File(dbName);

    if (dbFile.exists()) {
      return;
    }

    try {

      DriverManager.getConnection(dbURL);

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      System.exit(-1);

    }

  }

  /**
   * This method sets up the DB by creating the relevant tables.
   */
  public void setupDB() {

    createDB();

    String createCardTable = "CREATE TABLE IF NOT EXISTS cards ("
        + "id integer PRIMARY KEY,"
        + "name text NOT NULL,"
        + "number text NOT NULL"
        + ");";

    String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
        + "id integer PRIMARY KEY,"
        + "account text NOT NULL,"
        + "password text NOT NULL,"
        + "role text NOT NULL,"
        + "cardId integer NOT NULL,"
        + "FOREIGN KEY (cardId) REFERENCES cards (id)"
        + ");";

    String createItemsTable = "CREATE TABLE IF NOT EXISTS items ("
        + "id integer PRIMARY KEY,"
        + "name text NOT NULL,"
        + "code text NOT NULL,"
        + "category text NOT NULL,"
        + "price real NOT NULL,"
        + "quantity integer NOT NULL"
        + ");";

    String createCancelledOrdersTable = "CREATE TABLE IF NOT EXISTS cancelledOrders ("
        + "id integer PRIMARY KEY,"
        + "userId integer NOT NULL,"
        + "date DATETIME NOT NULL,"
        + "reason text NOT NULL"
        + ");";

    String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
        + "id integer PRIMARY KEY,"
        + "userId integer NOT NULL,"
        + "date DATETIME NOT NULL,"
        + "itemId integer NOT NULL,"
        + "quantity integer NOT NULL,"
        + "amountPaid real NOT NULL,"
        + "returnedChange real NOT NULL,"
        + "paymentMethod text NOT NULL,"
        + "FOREIGN KEY (itemId) REFERENCES items (id)"
        + ");";

    String createChangeTable = "CREATE TABLE IF NOT EXISTS change ("
        + "id integer PRIMARY KEY,"
        + "denomination text NOT NULL,"
        + "quantity integer NOT NULL"
        + ");";

    try (Connection conn = DriverManager.getConnection(dbURL);
        Statement statement = conn.createStatement()) {

      statement.execute(createUsersTable);
      statement.execute(createCardTable);
      statement.execute(createItemsTable);
      statement.execute(createCancelledOrdersTable);
      statement.execute(createOrdersTable);
      statement.execute(createChangeTable);

      if (getAllCards().isEmpty()) {
        addApprovedCards();
      }

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      System.exit(-1);

    }

  }

  public void addApprovedCards() {

    ArrayList<Card> cards = JsonParser.parseCreditCardFile();

    String insertCard = "INSERT INTO CARDS (name, number) VALUES (?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(insertCard)) {

      for (Card card : cards) {

        statement.setString(1, card.getName());
        statement.setString(2, card.getNumber());

        statement.executeUpdate();

      }

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return;

    }

  }

  public boolean checkCardValidity(String name, String number) {

    String selectCard = "SELECT * FROM cards WHERE name = ? AND number = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectCard)) {

      statement.setString(1, name);
      statement.setString(2, number);

      ResultSet result = statement.executeQuery();

      if (result.next()) {
        return true;
      }

      return false;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return false;

    }

  }

  public int associateCardWithUser(int cardId, int userId) {

    String sql = "UPDATE users SET cardId = ? WHERE id = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(sql)) {

      statement.setInt(1, cardId);
      statement.setInt(2, userId);

      statement.executeUpdate();

      return 1;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return -1;

    }

  }

  public User insertIntoUsersTable(String account, String password, String role) {

    String insertIntoUsersTable = "INSERT INTO USERS (account, password, role, cardId) VALUES (?, ?, ?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoUsersTable)) {

      statement.setString(1, account);
      statement.setString(2, password);
      statement.setString(3, role);

      // set default cardInt to -1 when inserting a new user
      statement.setInt(4, -1);

      statement.execute();

      return getUserByAccountAndPassword(account, password);

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public User getUserByAccountAndPassword(String account, String pwd) {

    String selectUser = "SELECT * FROM users WHERE account = ? AND password = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectUser)) {

      statement.setString(1, account);
      statement.setString(2, pwd);

      ResultSet result = statement.executeQuery();

      if (result.next()) {

        int id = result.getInt("id");
        String password = result.getString("password");
        String role = result.getString("role");
        int cardId = result.getInt("cardId");

        return new User(id, account, password, role, cardId);

      }

      return null;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public ArrayList<User> getAllUsers() {

    String getAllUsers = "SELECT * FROM USERS;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        Statement statement = conn.createStatement()) {

      ResultSet rs = statement.executeQuery(getAllUsers);

      ArrayList<User> users = new ArrayList<>();

      // create a list of users from the result set
      while (rs.next()) {

        int id = rs.getInt("id");
        String account = rs.getString("account");
        String password = rs.getString("password");
        String role = rs.getString("role");
        int cardId = rs.getInt("cardId");

        User user = new User(id, account, password, role, cardId);

        users.add(user);

      }

      return users;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public ArrayList<Card> getAllCards() {

    String getAllCards = "SELECT * FROM CARDS;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        Statement statement = conn.createStatement()) {

      ResultSet rs = statement.executeQuery(getAllCards);

      ArrayList<Card> cards = new ArrayList<>();

      // create a list of cards from the result set
      while (rs.next()) {

        String number = rs.getString("number");
        String name = rs.getString("name");

        Card card = new Card(name, number);

        cards.add(card);

      }

      return cards;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public int insertIntoItemsTable(String name, String code, String category, double price, int quantity) {

    String insertIntoItemsTable = "INSERT INTO ITEMS (name, code, category, price, quantity) VALUES (?, ?, ?, ?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoItemsTable)) {

      statement.setString(1, name);
      statement.setString(2, code);
      statement.setString(3, category);
      statement.setDouble(4, price);
      statement.setDouble(5, quantity);

      statement.execute();

      // return the userId
      ResultSet rs = statement.getGeneratedKeys();

      if (rs.next()) {
        return rs.getInt(1);
      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int getItemIdByCode(String code) {

    String selectItem = "SELECT * FROM items WHERE code = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectItem)) {

      statement.setString(1, code);

      ResultSet result = statement.executeQuery();

      if (result.next()) {

        int id = result.getInt("id");

        return id;

      }

      return -1;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return -1;

    }

  }

  public double getItemPriceByCode(String code) {

    String selectItem = "SELECT * FROM items WHERE code = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectItem)) {

      statement.setString(1, code);

      ResultSet result = statement.executeQuery();

      if (result.next()) {

        double price = result.getDouble("price");

        return price;

      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int getItemQuantityByCode(String code) {

    String selectItem = "SELECT * FROM items WHERE code = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectItem)) {

      statement.setString(1, code);

      ResultSet result = statement.executeQuery();

      if (result.next()) {

        int quantity = result.getInt("quantity");

        return quantity;

      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public ArrayList<Item> getAllItems() {

    String getAllItems = "SELECT * FROM ITEMS;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        Statement statement = conn.createStatement()) {

      ResultSet rs = statement.executeQuery(getAllItems);

      ArrayList<Item> items = new ArrayList<>();

      // create a list of items from the result set
      while (rs.next()) {

        String name = rs.getString("name");
        String code = rs.getString("code");
        String category = rs.getString("category");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");

        Item item = new Item(name, code, category, price, quantity);

        items.add(item);

      }

      return items;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public int updateItemQuantity(int itemId, int newQuantity) {

    String updateItemQuantity = "UPDATE items SET quantity = ? WHERE id = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(updateItemQuantity)) {

      statement.setInt(1, newQuantity);
      statement.setInt(2, itemId);

      statement.execute();

      return itemId;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int createCancelledOrder(int userId, LocalDate date, String reason) {

    String insertIntoCancelledOrdersTable = "INSERT INTO cancelledOrders (userId, date, reason) VALUES (?, ?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoCancelledOrdersTable)) {

      statement.setInt(1, userId);
      statement.setString(2, date.toString());
      statement.setString(3, reason);

      statement.execute();

      // return the userId
      ResultSet rs = statement.getGeneratedKeys();

      if (rs.next()) {
        return rs.getInt(1);
      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int createOrder(int userId, LocalDate date, int itemId, int quantity, double amountPaid, double returnedChange,
      String paymentMethod) {

    String insertIntoOrdersTable = "INSERT INTO orders (userId, date, itemId, quantity, amountPaid, returnedChange, paymentMethod) VALUES (?, ?, ?, ?, ?, ?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoOrdersTable)) {

      statement.setInt(1, userId);
      statement.setString(2, date.toString());
      statement.setInt(3, itemId);
      statement.setInt(4, quantity);
      statement.setDouble(5, amountPaid);
      statement.setDouble(6, returnedChange);
      statement.setString(7, paymentMethod);

      statement.execute();

      // return the userId
      ResultSet rs = statement.getGeneratedKeys();

      if (rs.next()) {
        return rs.getInt(1);
      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public ArrayList<Order> getFiveMostRecentOrders(int userId) {

    String getFiveMostRecentOrders = "SELECT * FROM orders WHERE userId = ? ORDER BY date DESC LIMIT 5;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(getFiveMostRecentOrders)) {

      statement.setInt(1, userId);

      ResultSet rs = statement.executeQuery();

      ArrayList<Order> orders = new ArrayList<>();

      // create a list of orders from the result set
      while (rs.next()) {

        int id = rs.getInt("id");
        LocalDate date = LocalDate.parse(rs.getString("date"));
        int itemId = rs.getInt("itemId");
        int quantity = rs.getInt("quantity");
        double amountPaid = rs.getDouble("amountPaid");
        double returnedChange = rs.getDouble("returnedChange");
        String paymentMethod = rs.getString("paymentMethod");

        Order order = new Order(id, userId, date, itemId, quantity, amountPaid, returnedChange, paymentMethod);

        orders.add(order);

      }

      return orders;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public int insertIntoChangeTable(String denomination, int quantity) {

    String insertIntoChangeTable = "INSERT INTO change (denomination, quantity) VALUES (?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoChangeTable)) {

      statement.setString(1, denomination);
      statement.setInt(2, quantity);

      statement.execute();

      // return the userId
      ResultSet rs = statement.getGeneratedKeys();

      if (rs.next()) {
        return rs.getInt(1);
      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int getAmountOfChangeForDenomination(String denomination) {

    String selectItem = "SELECT * FROM change WHERE denomination = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);

        PreparedStatement statement = conn.prepareStatement(selectItem)) {

      statement.setString(1, denomination);

      ResultSet result = statement.executeQuery();

      if (result.next()) {

        int quantity = result.getInt("quantity");

        return quantity;

      }

      return 0;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int updateChangeForDenomination(String denomination, int newQuantity) {

    String updateItemQuantity = "UPDATE change SET quantity = ? WHERE denomination = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(updateItemQuantity)) {

      statement.setInt(1, newQuantity);
      statement.setString(2, denomination);

      statement.execute();

      return newQuantity;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

}
