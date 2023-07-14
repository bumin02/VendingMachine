package vendingMachine;

import java.time.LocalDate;

public class Order {

  int id;
  int userId;
  LocalDate date;
  int itemId;
  int quantity;
  double amountPaid;
  double change;
  String paymentMethod;

  public Order(int id, int userId, LocalDate date, int itemId, int quantity, double amountPaid, double change,
      String paymentMethod) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.itemId = itemId;
    this.quantity = quantity;
    this.amountPaid = amountPaid;
    this.change = change;
    this.paymentMethod = paymentMethod;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public LocalDate getDate() {
    return date;
  }

  public int getItemId() {
    return itemId;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getAmountPaid() {
    return amountPaid;
  }

  public double getChange() {
    return change;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  @Override
  public String toString() {
    return "Order [itemId=" + itemId + ", quantity=" + quantity
        + "]";
  }

}
