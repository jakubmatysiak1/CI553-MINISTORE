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
        // Use default constructors
        model = new CashierModel();
        view = new CashierView();
        controller = new CashierController(model, view);
    }

    @Test
    public void testDoCheckInvalidQuantity() {
        String productNumber = "P123";
        String quantity = "invalid";

        controller.doCheck(productNumber, quantity);

        assertTrue(view.getLastMessage().contains("Invalid quantity: invalid"));
    }

    @Test
    public void testDoBuyValidQuantity() {
        String quantity = "5";

        controller.doBuy(quantity);

        assertEquals(5, model.getPurchasedQuantity());
    }

    @Test
    public void testDoClear() {
        controller.doClear();

        assertTrue(model.isCleared());
    }

    @Test
    public void testDoBought() {
        controller.doBought();

        assertTrue(model.isBought());
    }
}
