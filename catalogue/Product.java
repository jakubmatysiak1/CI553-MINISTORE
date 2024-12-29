package catalogue;

import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class Product implements Serializable
{
  private static final long serialVersionUID = 20092506;
  private String theProductNum;       // Product number
  private String theDescription;      // Description of product
  private double thePrice;            // Price of product
  private int    theQuantity;         // Quantity involved
  
  // path to the product image
  // developed by jakub
  private String imagePath; 

  /**
   * Construct a product details
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice The price of the product
   * @param aQuantity The Quantity of the product involved
   */
  
  // updated constructors to pass information to other methods to create product
  // developed by jakub
  public Product(String aProductNum, String aDescription, double aPrice, String anImagePath) {
      theProductNum = aProductNum;
      theDescription = aDescription;
      thePrice = aPrice;
      imagePath = anImagePath;
      theQuantity = 0;
  }
  
  public Product(String productNum, String description, double price, int quantity) {
	    this.theProductNum = productNum;
	    this.theDescription = description;
	    this.thePrice = price;
	    this.theQuantity = quantity;
	    this.imagePath = null;
	}
  
  public String getProductNum()  { return theProductNum; }
  public String getDescription() { return theDescription; }
  public double getPrice()       { return thePrice; }
  public int    getQuantity()    { return theQuantity; }
  
  public void setProductNum( String aProductNum )
  { 
    theProductNum = aProductNum;
  }
  
  public void setDescription( String aDescription )
  { 
    theDescription = aDescription;
  }
  
  public void setPrice( double aPrice )
  { 
    thePrice = aPrice;
  }
  
  public void setQuantity( int aQuantity )
  { 
    theQuantity = aQuantity;
  }
  
  public String getImagePath() {
      return imagePath;
  }
  
  
  // functionality to process image path
  // developed by jakub
  public void setImagePath(String anImagePath) {
      imagePath = anImagePath;
  }
  
  // functionality to convert product info to readable string
  // developed by jakub
  @Override
  public String toString() {
      return String.format("Product[ID=%s, Description=%s, Price=%.2f, Quantity=%d, Image=%s]",
              theProductNum, theDescription, thePrice, theQuantity, imagePath);
  }
}
