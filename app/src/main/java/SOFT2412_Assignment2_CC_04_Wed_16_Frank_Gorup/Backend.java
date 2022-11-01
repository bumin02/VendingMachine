package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.time.LocalDate;
import java.util.Scanner;

class Backend extends vendingMachine {
    Database db;

    public Backend(Database db) {
        super(db);
    }

    public static int[] sumCash(String[] cashInput, int fiveCents, int tenCents, int twentyCents, int fiftyCents,
                         int oneDollar,
                         int twoDollars, int fiveDollars, int tenDollars, int twentyDollars, int fiftyDollars, int hundredDollars) {

        for (String s : cashInput) {

            String cash[] = s.split("\\*");

            String amount = cash[0];
            int num = Integer.parseInt(cash[1]);

            if (amount.equals("5c")) {
                fiveCents += num;
            } else if (amount.equals("10c")) {
                tenCents += num;
            } else if (amount.equals("20c")) {
                twentyCents += num;
            } else if (amount.equals("50c")) {
                fiftyCents += num;
            } else if (amount.equals("1")) {
                oneDollar += num;
            } else if (amount.equals("2")) {
                twoDollars += num;
            } else if (amount.equals("5")) {
                fiveDollars += num;
            } else if (amount.equals("10")) {
                tenDollars += num;
            } else if (amount.equals("20")) {
                twentyDollars += num;
            } else if (amount.equals("50")) {
                fiftyDollars += num;
            } else if (amount.equals("100")) {
                hundredDollars += num;
            } else {
                return null;
            }

        }

        int[] cash = { fiveCents, tenCents, twentyCents, fiftyCents, oneDollar, twoDollars, fiveDollars, tenDollars,
                twentyDollars, fiftyDollars, hundredDollars };
        return cash;

    }


}



