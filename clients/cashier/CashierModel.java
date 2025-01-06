package clients.cashier;

import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable
{
  private enum State { process, checked }

  private State       theState   = State.process;   // Current state
  private Product     theProduct = null;            // Current product
  private BetterBasket    theBasket  = null; // updated to utilise betterbasket by jakub

  private String      pn = "";                      // Product being processed

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  
  // variables required for junit testing
  private int checkedQuantity = 0;
  private int purchasedQuantity = 0;
  private boolean cleared = false;
  private boolean bought = false;

  /**
   * Construct the model of the Cashier
   * @param mf The factory to create the connection objects
   */
  
  // default consturctor for testing
  // developed by jakub
  public CashierModel() {

	  this.checkedQuantity = 0;
      this.purchasedQuantity = 0;
      this.cleared = false;
      this.bought = false;
  }

  public CashierModel(MiddleFactory mf)
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
    }
    theState   = State.process;                  // Current state
  }
  
  /**
   * Get the Basket of products
   * @return basket
   */
  
  // updated to utilise betterBasket
  // by jakub
  public BetterBasket getBasket() // Updated return type
  {
      return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  
  // updated doCheck method to include dynamic quantity variable rather than static quantity
  // developed by jakub
  
  public void doCheck(String productNum, int quantity )
  {
    String theAction = "";
    theState  = State.process;                  // State process
    pn  = productNum.trim();                    // Product no.
    
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails(pn);   //  Get details
        if ( pr.getQuantity() >= quantity )       //  In stock?
        {                                       //  T
          theAction =                           //   Display 
            String.format( "%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity     
          theProduct = pr;                      //   Remember prod.
          theProduct.setQuantity( quantity );     //    & quantity
          theState = State.checked;             //   OK await BUY 
        } else {                                //  F
          theAction =                           //   Not in Stock
            pr.getDescription() +" not in stock";
        }
      } else {                                  // F Stock exists
        theAction =                             //  Unknown
          "Unknown product number " + pn;       //  product no.
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCheck", e.getMessage() );
      theAction = e.getMessage();
    }
    this.checkedQuantity = quantity;
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Buy the product
   */
  
  // doClear method utilised for resetting the cashier and resetting the stock levels back to original amount when cleared
  public void doClear() {
	  
	    String theAction = "";

	    try {
	        if (theBasket != null && theBasket.size() > 0) {
	            for (Product product : theBasket) {
	                int quantityToRestore = product.getQuantity();
	                
	                theStock.addStock(product.getProductNum(), quantityToRestore);
	            }
	            theBasket = null;
	            
	            theAction = "Order cleared and stock levels restored";
	            
	        } else {
	        	
	            theAction = "No items in the basket to clear";
	        }
	        
	    } catch (StockException e) {
	        DEBUG.error("CashierModel.doClear\n%s", e.getMessage());
	        theAction = e.getMessage();
	    }

	    theBasket = null;
	    
	    setChanged();
	    
	    notifyObservers(theAction);
	    
        this.cleared = true;

	}

  // updated doBuy method to include dynamic quantity variable rather than static quantity
  // developed by jakub
  
  public void doBuy(int quantity)
  {
    String theAction = "";
    
    try
    {
      if ( theState != State.checked )          // Not checked
      {                                         //  with customer
        theAction = "please check its availablity";
      } else {
        boolean stockBought =                   // Buy
          theStock.buyStock(                    //  however
            theProduct.getProductNum(),         //  may fail              
            quantity );         //
        if ( stockBought )                      // Stock bought
        {                                       // T
          makeBasketIfReq();                    //  new Basket ?
          theProduct.setQuantity(quantity);
          theBasket.add( theProduct );          //  Add to bought
          theAction = "Purchased " +            //    details
                  theProduct.getDescription() + " x" + quantity;
        } else {                                // F
          theAction = "!!! Not in stock";       //  Now no stock
        }
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doBuy", e.getMessage() );
      theAction = e.getMessage();
    }
    theState = State.process;                   // All Done
    setChanged(); notifyObservers(theAction);
    
    this.purchasedQuantity = quantity;

  }
  
  /**
   * Customer pays for the contents of the basket
   */
  public void doBought()
  {
    String theAction = "";
    int    amount  = 1;                       //  & quantity
    try
    {
      if ( theBasket != null &&
           theBasket.size() >= 1 )            // items > 1
      {                                       // T
        theOrder.newOrder( theBasket );       //  Process order
        theBasket = null;                     //  reset
      }                                       //
      theAction = "Start New Order";            // New order
      theState = State.process;               // All Done
       theBasket = null;
    } catch( OrderException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCancel", e.getMessage() );
      theAction = e.getMessage();
    }
    theBasket = null;
    setChanged(); notifyObservers(theAction); // Notify
    
    this.bought = true;
  }

  /**
   * ask for update of view callled at start of day
   * or after system reset
   */
  public void askForUpdate()
  {
    setChanged(); notifyObservers("Welcome");
  }
  
  /**
   * make a Basket when required
   */
  private void makeBasketIfReq()
  {
    if ( theBasket == null )
    {
      try
      {
        int uon   = theOrder.uniqueNumber();     // Unique order num.
        theBasket = makeBasket();                //  basket list
        theBasket.setOrderNum( uon );            // Add an order number
      } catch ( OrderException e )
      {
        DEBUG.error( "Comms failure\n" +
                     "CashierModel.makeBasket()\n%s", e.getMessage() );
      }
    }
  }

  /**
   * return an instance of a new Basket
   * @return an instance of a new Basket
   */
  
  // updated to utilise betterBasket
  // by jakub
  protected BetterBasket makeBasket()
  {
    return new BetterBasket();
  }
  
  // methods for getters to junit testing
  public int getCheckedQuantity() {
      return checkedQuantity;
  }

  public int getPurchasedQuantity() {
      return purchasedQuantity;
  }

  public boolean isCleared() {
      return cleared;
  }

  public boolean isBought() {
      return bought;
  }
}
  
