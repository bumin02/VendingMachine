package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
        + "id integer PRIMARY KEY,"
        + "username text NOT NULL,"
        + "password text NOT NULL,"
        + "balance real NOT NULL,"
        + "admin integer NOT NULL"
        + ");";

    String createCardTable = "CREATE TABLE IF NOT EXISTS cards ("
        + "id integer PRIMARY KEY,"
        + "name text NOT NULL,"
        + "number text NOT NULL"
        + ");";

    String createItemsTable = "CREATE TABLE IF NOT EXISTS items ("
        + "id integer PRIMARY KEY,"
        + "name text NOT NULL,"
        + "code text NOT NULL,"
        + "category text NOT NULL,"
        + "price real NOT NULL,"
        + "quantity integer NOT NULL"
        + ");";

    try (Connection conn = DriverManager.getConnection(dbURL);
        Statement statement = conn.createStatement()) {

      statement.execute(createUsersTable);
      statement.execute(createCardTable);
      statement.execute(createItemsTable);

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

  public int insertIntoUsersTable(String account, String password) {

    String insertIntoUsersTable = "INSERT INTO USERS (account, password) VALUES (?, ?);";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(insertIntoUsersTable)) {

      statement.setString(1, account);
      statement.setString(2, password);

      statement.execute();
      return 1;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

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

        User user = new User(id, account, password);

        users.add(user);

      }

      return users;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return null;

    }

  }

  public int checkUserExists(String account, String password) {

    String checkUserExists = "SELECT * FROM USERS WHERE account = ? AND password = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(checkUserExists)) {

      statement.setString(1, account);
      statement.setString(2, password);

      ResultSet rs = statement.executeQuery();

      if (rs.next()) {

        return rs.getInt("id");

      } else {

        return 0;

      }

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return -1;

    }

  }

  public String findUserPassword(String account) {

    String findUserPassword = "SELECT password FROM USERS WHERE account = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(findUserPassword)) {

      statement.setString(1, account);

      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        return rs.getString("password");
      }

      return null;

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

        Card card = new Card(number, name);

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

  public int updateItemQuantity(String name, String code, String category, int quantity) {

    String updateItemQuantity = "UPDATE ITEMS SET quantity = ? WHERE name = ? AND code = ? AND category = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(updateItemQuantity)) {

      statement.setInt(1, quantity);
      statement.setString(2, name);
      statement.setString(3, code);
      statement.setString(4, category);

      statement.execute();
      return 1;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

  public int updateItemPrice(String name, String code, String category, long price) {

    String updateItemPrice = "UPDATE ITEMS SET price = ? WHERE name = ? AND code = ? AND category = ?;";

    try (Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement statement = conn.prepareStatement(updateItemPrice)) {

      statement.setLong(1, price);
      statement.setString(2, name);
      statement.setString(3, code);
      statement.setString(4, category);

      statement.execute();
      return 1;

    } catch (SQLException e) {

      System.out.println(e.getMessage());
      return 0;

    }

  }

}
