package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {

  User user;
  Database db;;
  Card card;

  @BeforeEach
  public void setup() {
    this.user = new User(123, "Account", "Password", "buyer", -1);
    this.db = mock(Database.class);
    this.card = mock(Card.class);
  }

  @Test
  public void testGetAccount() {
    assertEquals("Account", this.user.getAccount());
  }

  @Test
  public void testGetPassword() {
    assertEquals("Password", this.user.getPassword());
  }

  @Test
  public void testGetId() {
    assertEquals(123, this.user.getId());
  }

  @Test
  public void testGetCard() {
    assertEquals(-1, this.user.getCard());
  }

  @Test
  public void testSetCard() {
    this.user.setCard(321);
    assertEquals(321, this.user.getCard());
  }

  @Test
  public void testAssociateCard1() {

    this.user.setCard(321);
    when(this.card.getName()).thenReturn("name");
    when(this.card.getNumber()).thenReturn("number");

    when(this.db.checkCardValidity(anyString(), anyString())).thenReturn(true);
    when(this.db
        .associateCardWithUser(anyInt(), anyInt()))
        .thenReturn(0);

    int res = this.user.associateCardWithUser(this.card, this.db);
    assertEquals(res, -1);

  }

  @Test
  public void testAssociateCard2() {

    this.user.setCard(321);
    when(this.card.getName()).thenReturn("name");
    when(this.card.getNumber()).thenReturn("number");
    when(this.card.getId()).thenReturn(123);

    when(this.db.checkCardValidity(anyString(), anyString())).thenReturn(true);
    when(this.db
        .associateCardWithUser(anyInt(), anyInt()))
        .thenReturn(1);

    int res = this.user.associateCardWithUser(this.card, this.db);
    assertEquals(res, 1);
    assertEquals(this.user.getCard(), 123);

  }

  @Test
  public void testAssociateCard3() {

    when(this.card.getName()).thenReturn("name");
    when(this.card.getNumber()).thenReturn("number");

    when(this.db.checkCardValidity(anyString(), anyString())).thenReturn(false);

    int res = this.user.associateCardWithUser(this.card, this.db);
    assertEquals(res, -2);

  }

  @Test
  public void testAssociateCard4() {

    this.user.setCard(123);
    when(this.card.getName()).thenReturn("name");
    when(this.card.getNumber()).thenReturn("number");

    when(this.db.checkCardValidity(anyString(), anyString())).thenReturn(false);

    int res = this.user.associateCardWithUser(this.card, this.db);
    assertEquals(res, 2);

  }

  @Test
  public void testHasSellerPermissions() {

    this.user = new User(123, "Account", "Password", "seller", -1);
    assertEquals(true, this.user.hasSellerPermissions());

  }

  @Test
  public void testHasCashierPermissions() {

    this.user = new User(123, "Account", "Password", "cashier", -1);
    assertEquals(true, this.user.hasCashierPermissions());

  }

  @Test
  public void testHasOwnerPermissions() {

    this.user = new User(123, "Account", "Password", "owner", -1);
    assertEquals(true, this.user.hasOwnerPermissions());
    assertEquals(true, this.user.hasCashierPermissions());
    assertEquals(true, this.user.hasSellerPermissions());

  }

  @Test
  public void testGetRole() {

    this.user = new User(123, "Account", "Password", "owner", -1);
    assertEquals("owner", this.user.getRole());

  }

}
