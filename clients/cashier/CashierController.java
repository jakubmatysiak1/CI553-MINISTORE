package clients.cashier;


/**
 * The Cashier Controller
 */

public class CashierController
{
  private CashierModel model = null;
  private CashierView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CashierController( CashierModel model, CashierView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  
  // updated methods check and buy which include the quantity to pass through
  // developed by jakub
  public void doCheck( String pn, String quantity_str)
  {
	  try {
		  int quantity = Integer.parseInt(quantity_str.trim()); // convert to int
		  model.doCheck(pn, quantity); // checks for product num with requested quantity int
	  } catch (NumberFormatException e) {
		  view.updateErrorMessage(model, "Invalid quantity: " + quantity_str); // catch error if invalid (tested)
	  }
  }
  
  // method to clear the cashier using button
  // developed by jakub
  public void doClear() {
	    model.doClear(); // clears model object
	}

   /**
   * Buy interaction from view
   */
  public void doBuy(String quantity_str)
  {
	  try {
		  int quantity = Integer.parseInt(quantity_str.trim());
		  model.doBuy(quantity);
	  } catch (NumberFormatException e) {
		  view.updateErrorMessage(model, "Invalid quantity: " + quantity_str);
	  }
  }
  
   /**
   * Bought interaction from view
   */
  public void doBought()
  {
    model.doBought();
  }
}
