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

public class testVendingMachine {
    vendingMachine vm;


    @BeforeEach
    public void setUp() {
        vm = new vendingMachine();
    }

    @Test
    @DisplayName("Validate login")
    public void validateLogin() {
        // invalid input
        assertFalse(vm.login(null, null));
        assertFalse(vm.login("hi", null));
        assertFalse(vm.login(null, "hi"));

        // valid user not exist login
        assertFalse(vm.login("abc", "123ksaf"));

        // wrong password and username combination
        assertTrue(vm.createAccount("abc", "1asd"));
        assertFalse(vm.login("abc", "asdf"));
        assertFalse(vm.login("asedf", "1asd"));

        // correct login
        assertTrue(vm.login("abc", "1asd"));
        assertEquals("abc", vm.getCurrentUser().getAccount());
    }

    @Test
    @DisplayName("Validate create account")
    public void validateCreateAccount() {
        assertFalse(vm.createAccount(null, null));
        assertFalse(vm.createAccount("hi", null));
        assertFalse(vm.createAccount(null, "hi"));

        // valid account creation
        assertTrue(vm.createAccount("abc", "123ksaf"));

        // check user has log in
        assertEquals("abc", vm.getCurrentUser().getAccount());

        // account exist -> not valid
        assertFalse(vm.createAccount("abc", "123ksaf"));
    }
}
