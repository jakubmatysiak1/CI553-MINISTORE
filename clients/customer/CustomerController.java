package clients.customer;

/**
 * The Customer Controller
 */

public class CustomerController
{
  private CustomerModel model = null;
  private CustomerView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CustomerController( CustomerModel model, CustomerView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck( String pn )
  {
    model.doCheck(pn);
  }

  /**
   * Clear interaction from view
   */
  public void doClear()
  {
    model.doClear();
  }
  
  // method to fetch for suggestions
  // developed by jakub
  public void fetchSuggestions(String partialInput) {
	  
	    String[] suggestions = model.getSuggestions(partialInput); // call array of suggestions
	    
	    view.displaySuggestions(suggestions); // pass suggestions from array to user
	    
	    System.out.print("Fetch suggestions method executed!"); // print debugging i utilised
  }
  
}

