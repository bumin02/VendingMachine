package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

public class User {
    String account;
    String password;
    int id;
    String role;
    int cardId;

    public User(int id, String account, String password, String role, int cardId) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.role = role;
        this.cardId = cardId;
    }

    public void setCard(int cardId) {
        this.cardId = cardId;
    }

    public int getCard() {
        return this.cardId;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;

    }

    public int getId() {
        return this.id;
    }

    public String getRole() {
        return this.role;
    }

    public int associateCardWithUser(Card card, Database db) {

        // check for validity
        boolean valid = db.checkCardValidity(card.getName(), card.getNumber());

        if (valid) {

            int res = db.associateCardWithUser(card.getId(), this.getId());

            if (res != 1) {
                return -1;
            } else {
                this.setCard(card.getId());
                return 1;
            }

        } else {

            if (this.getCard() != -1) {
                System.out.println(
                        "The card you have provided is invalid. However, you have an existing card stored on your account. This card will be used for this transaction instead.");
                return 2;
            } else {
                System.out.println(
                        "The card you have provided is invalid. You will not be able to make purchases until you provide a valid card.");
                return -2;
            }

        }

    }

}
