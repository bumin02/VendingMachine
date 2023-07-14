package vendingMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class ItemTest {

  Item item;

  @BeforeEach
  public void setup() {

    this.item = new Item(1, "name", "code", "category", 1.0, 1);

  }

  @Test
  public void testGetName() {

    assertEquals("name", this.item.getName());

  }

  @Test
  public void testGetCode() {

    assertEquals("code", this.item.getCode());

  }

  @Test
  public void testGetCategory() {

    assertEquals("category", this.item.getCategory());

  }

  @Test
  public void testGetPrice() {

    assertEquals(1.0, this.item.getPrice());

  }

  @Test
  public void testGetQuantity() {

    assertEquals(1, this.item.getQuantity());

  }

  @Test
  public void testGetId() {

    assertEquals(1, this.item.getId());

  }

}
