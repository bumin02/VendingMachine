package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import org.junit.jupiter.api.BeforeEach;

public class VendingMachineTest {

    vendingMachine vm;
    Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
        vm = new vendingMachine(db);
    }

}
