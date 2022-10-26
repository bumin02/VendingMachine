package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VendingMachineTest {

    vendingMachine vm;
    Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
        vm = new vendingMachine(db);
    }

}
