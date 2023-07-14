package vendingMachine;

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
