package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

  Order order;

  @BeforeEach
  public void setup() {

    this.order = new Order(1, 1, LocalDate.now(), 1, 1, 1.0, 1.0, "paymentMethod");

  }

  @Test
  public void testGetId() {

    assertEquals(1, this.order.getId());

  }

  @Test
  public void testGetUserId() {

    assertEquals(1, this.order.getUserId());

  }

  @Test
  public void testGetDate() {

    assertEquals(LocalDate.now(), this.order.getDate());

  }

  @Test
  public void testGetItemId() {

    assertEquals(1, this.order.getItemId());

  }

  @Test
  public void testGetQuantity() {

    assertEquals(1, this.order.getQuantity());

  }

  @Test
  public void testGetAmountPaid() {

    assertEquals(1.0, this.order.getAmountPaid());

  }

  @Test
  public void testGetChange() {

    assertEquals(1.0, this.order.getChange());

  }

  @Test
  public void testGetPaymentMethod() {

    assertEquals("paymentMethod", this.order.getPaymentMethod());

  }

  @Test
  public void testToString() {

    assertEquals(
        "Order [itemId=1, quantity=1]",
        this.order.toString());

  }

}
