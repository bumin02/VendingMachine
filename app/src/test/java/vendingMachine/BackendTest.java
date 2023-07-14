package vendingMachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BackendTest {

  Backend backend;

  @BeforeEach
  public void setup() {
    backend = new Backend();
  }

  @Test
  public void testSumCash() {
    String[] cashInput = { "5c*1", "10c*1", "20c*1", "50c*1", "1*1", "2*1", "5*1", "10*1", "20*1", "50*1", "100*1" };
    int[] cash = Backend.sumCash(cashInput, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    assertEquals(1, cash[0]);
    assertEquals(1, cash[1]);
    assertEquals(1, cash[2]);
    assertEquals(1, cash[3]);
    assertEquals(1, cash[4]);
    assertEquals(1, cash[5]);
    assertEquals(1, cash[6]);
    assertEquals(1, cash[7]);
    assertEquals(1, cash[8]);
    assertEquals(1, cash[9]);
    assertEquals(1, cash[10]);
  }

  @Test
  public void testSumCashError() {

    String[] cashInput = { "5c*1", "10c*1", "20c*1", "50c*1", "1*1", "2*1", "5*1", "10*1", "20*1", "50*1", "100*1",
        "200*1" };
    int[] cash = Backend.sumCash(cashInput, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    assertEquals(null, cash);

  }

}
