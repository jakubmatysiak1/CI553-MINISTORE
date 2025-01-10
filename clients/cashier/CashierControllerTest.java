package clients.cashier;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CashierControllerTest {

    private CashierModel model;
    private CashierView view;
    private CashierController controller;

    @BeforeEach
    public void setUp() {
        // default constructors
        model = new CashierModel();
        view = new CashierView();
        controller = new CashierController(model, view);
    }

    @Test
    public void testDoCheckInvalidQuantity() {
        String productNumber = "ghj2342"; // product num to random
        String quantity = "invalid"; // quantity to invlid

        controller.doCheck(productNumber, quantity); // handles invalid quantity

        assertTrue(view.getLastMessage().contains("Invalid quantity: invalid")); // ensure msg contains expected error
    }

    @Test
    public void testDoBuyValidQuantity() {
        String quantity = "5"; // qt to 5

        controller.doBuy(quantity); // processes purchase

        assertEquals(5, model.getPurchasedQuantity()); // check for purchased qt to 5 
    }

    @Test
    public void testDoClear() {
    	
        controller.doClear(); // clear

        assertTrue(model.isCleared()); // check for clear, true for pass, false for fail
    }

    @Test
    public void testDoBought() {
    	
        controller.doBought(); // simulation of purchase

        assertTrue(model.isBought()); // true if purchase was true, then pass. 
       // anything else is fail
    }
}
