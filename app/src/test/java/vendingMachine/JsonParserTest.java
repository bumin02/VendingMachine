package vendingMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.Test;

public class JsonParserTest {

  @Test
  public void testParseCreditCardFile() {

    ArrayList<Card> cards = JsonParser.parseCreditCardFile();

    assertEquals(50, cards.size());

    Card realCard = new Card("Manuel", "53477");
    Card fakeCard = new Card("Manuel", "53478");

    assertTrue(cards.contains(realCard));
    assertFalse(cards.contains(fakeCard));

  }

}
