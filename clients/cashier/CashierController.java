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
		  int quantity = Integer.parseInt(quantity_str.trim());
		  model.doCheck(pn, quantity);
	  } catch (NumberFormatException e) {
		  view.update(null, "Invalid quantity: " + quantity_str);
	  }
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
		  view.update(null, "Invalid quantity: " + quantity_str);
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
