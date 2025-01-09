package catalogue;

import java.io.Serializable;

import java.util.HashMap;

/**
 * A BetterBasket class that extends the Basket class.
 * This class provides additional functionality to merge products with the same 
 * product number and manage the basket's content using a HashMap.
 * 
 * <p>
 * Features:
 * <ul>
 *     <li>Merges quantities of products with the same product number when added.</li>
 *     <li>Overrides basket methods for add, remove, and clear operations.</li>
 * </ul>
 * </p>
 * 
 * @author Jakub Matysiak
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    // function to merge products together with the same product number in the basket
    // developed by jakub
    
    private HashMap<String, Product> product_map = new HashMap<>();

    @Override
    public boolean add(Product pr) {
    	
        String product_number = pr.getProductNum();

        if (product_map.containsKey(product_number)) {
            Product existing_product = product_map.get(product_number);
            existing_product.setQuantity(existing_product.getQuantity() + pr.getQuantity());
            return true;
        } else {
        	product_map.put(product_number, pr);
            return super.add(pr);
        }
    }
    
    /**
     * Removes a product from the basket. If the product exists in the hashmap,
     * it is also removed from there.
     * 
     * @param pr the product to be removed
     * @return if the product was removed; otherwise
     */
    
    @Override
    public boolean remove(Object pr) {
    	
        if (pr instanceof Product) {
            Product product = (Product) pr;
            product_map.remove(product.getProductNum());
        }
        
        return super.remove(pr);
    }
    
    /**
     * Clears all items from the basket, used for tracking products.
     */
    
    @Override
    public void clear_basket() {
    	product_map.clear();
        super.clear_basket();
    }
    
    /**
     * Gets the details of the basket
     * This method uses the parent class implementation.
     * 
     * @return a string representation of the basket's details
     */
    
    @Override
    public String getDetails() {
        return super.getDetails();
    }
    
    // comments and code developed by jakub
    // better basket branch
}


