package catalogue;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

/**
 * Write a description of class BetterBasket here.
 * 
 * @author  Your Name 
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable
{
  private static final long serialVersionUID = 1L;
  

  // You need to add code here
  // merge the items for same product,
  // or sort the item based on the product number
  
  // add product method
  // before i add a product, i need to check if the same product already exists in the ArrayList
  // this will avoid duplicates
  public boolean add(Product pr) {
	    for (Product existingProduct : this) {
	        if (existingProduct.getProductNum().equals(pr.getProductNum())) {  // Removed semicolon here
	            existingProduct.setQuantity(existingProduct.getQuantity() + pr.getQuantity());
	            return true;
	        }
	    }
	    return super.add(pr); 
	    // if product doesn't match, add it to the list
	}
  
  // sorting by ID feature
  // links back to the cashier model giving the button functionality
  
  private boolean sortStatus = false; 
  // boolean storing the current sort status, either ascending or descending
  
  public void sortID() {
	 if (sortStatus) {
		 Collections.sort(this, Comparator.comparing(Product::getProductNum));
	 }
	 else {
		 Collections.sort(this, Comparator.comparing(Product::getProductNum).reversed());
	 }
	 sortStatus = !sortStatus;
  }
}