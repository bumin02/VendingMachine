package vendingMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardTest {

  Card card;

  @BeforeEach
  public void setup() {

    this.card = new Card("name", "number");

  }

  @Test
  public void testGetName() {

    assertEquals("name", this.card.getName());

  }

  @Test
  public void testGetNumber() {

    assertEquals("number", this.card.getNumber());

  }

  @Test
  public void testGetId() {

    assertEquals(-1, this.card.getId());

  }

  @Test
  public void testOtherConstructor() {

    Card card = new Card(1, "name", "number");

    assertEquals(1, card.getId());
    assertEquals("name", card.getName());
    assertEquals("number", card.getNumber());

  }

}
