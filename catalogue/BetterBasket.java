package catalogue;

import java.io.Serializable;

import java.util.HashMap;

/**
 * A BetterBasket class that extends Basket.
 * Provides functionality to merge products with the same product number
 * and sort the items based on the product number.
 * 
 * @author Your Name
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
    
    @Override
    public boolean remove(Object pr) {
    	
        if (pr instanceof Product) {
            Product product = (Product) pr;
            product_map.remove(product.getProductNum());
        }
        
        return super.remove(pr);
    }
    
    @Override
    public void clear_basket() {
    	product_map.clear();
        super.clear_basket();
    }
    
    @Override
    public String getDetails() {
        return super.getDetails();
    }
}


