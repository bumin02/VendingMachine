package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.util.ArrayList;

public class User {
    String account;
    String password;
    ArrayList<History> histories;
    Card card;

    public User(String account, String password) {
        this.account = account;
        this.password = password;
        this.histories = new ArrayList<>();
    }

    public void setCard(String name, String number) {
        this.card = new Card(name, number);
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;

    }
}
