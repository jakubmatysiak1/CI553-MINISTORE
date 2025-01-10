package dbAccess;

/**
 * Implements Read /Write access to the stock list
 * The stock list is held in a relational DataBase
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReadWriter;

import java.sql.ResultSet;
import java.sql.SQLException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods
// 

/**
  * Implements read/write access to the stock database.
  */
public class StockRW extends StockR implements StockReadWriter 
{
  /*
   * Connects to database
   */
	public StockRW() throws StockException {
	    super();

	    // code to ensure the database column and rows are updated
	    // developed by jakub
	    try {
	        ensureSchemaUpdated(); // attempt to create the imagepath column
	    } catch (SQLException e) {
	        throw new StockException("Failed to update schema: " + e.getMessage());
	        // if already created
	        // output in console / catch error
	    }
	}
  
  /**
   * Customer buys stock, quantity decreased if sucessful.
   * @param pNum Product number
   * @param amount Amount of stock bought
   * @return true if succeeds else false
   */
  public synchronized boolean buyStock( String pNum, int amount )
         throws StockException
  {
    DEBUG.trace("DB StockRW: buyStock(%s,%d)", pNum, amount);
    int updates = 0;
    try
    {
      getStatementObject().executeUpdate(
        "update StockTable set stockLevel = stockLevel-" + amount +
        "       where productNo = '" + pNum + "' and " +
        "             stockLevel >= " + amount + ""
      );
      updates = 1; // getStatementObject().getUpdateCount();
    } catch ( SQLException e )
    {
      throw new StockException( "SQL buyStock: " + e.getMessage() );
    }
    DEBUG.trace( "buyStock() updates -> %n", updates );
    return updates > 0;   // sucess ?
  }

  /**
   * Adds stock (Re-stocks) to the store.
   *  Assumed to exist in database.
   * @param pNum Product number
   * @param amount Amount of stock to add
   */
  public synchronized void addStock( String pNum, int amount )
         throws StockException
  {
    try
    {
      getStatementObject().executeUpdate(
        "update StockTable set stockLevel = stockLevel + " + amount +
        "         where productNo = '" + pNum + "'"
      );
      //getConnectionObject().commit();
      DEBUG.trace( "DB StockRW: addStock(%s,%d)" , pNum, amount );
    } catch ( SQLException e )
    {
      throw new StockException( "SQL addStock: " + e.getMessage() );
    }
  }

  /**
   * Modifies Stock details for a given product number.
   *  Assumed to exist in database.
   * Information modified: Description, Price
   * @param detail Product details to change stocklist to
   */
  
  public synchronized void modifyStock( Product detail )
         throws StockException
  {
    DEBUG.trace( "DB StockRW: modifyStock(%s)", 
                 detail.getProductNum() );
    try
    {
      if ( ! exists( detail.getProductNum() ) )
      {
    	getStatementObject().executeUpdate( 
         "insert into ProductTable values ('" +
            detail.getProductNum() + "', " + 
             "'" + detail.getDescription() + "', " + 
             "'images/Pic" + detail.getProductNum() + ".jpg', " + 
             "'" + detail.getPrice() + "' " + ")"
            );
    	getStatementObject().executeUpdate( 
           "insert into StockTable values ('" + 
           detail.getProductNum() + "', " + 
           "'" + detail.getQuantity() + "' " + ")"
           ); 
      } else {
    	getStatementObject().executeUpdate(
          "update ProductTable " +
          "  set description = '" + detail.getDescription() + "' , " +
          "      price       = " + detail.getPrice() +
          "  where productNo = '" + detail.getProductNum() + "' "
         );
       
    	getStatementObject().executeUpdate(
          "update StockTable set stockLevel = " + detail.getQuantity() +
          "  where productNo = '" + detail.getProductNum() + "'"
        );
      }
      
    } catch ( SQLException e )
    {
      throw new StockException( "SQL modifyStock: " + e.getMessage() );
    }
  }
  
  // ensures no duplicate product exists and inserting the product into ProductTable
  // developed by jakub
  @Override
  public synchronized void addProduct(Product product) throws StockException {
	  
      try {

          if (exists(product.getProductNum())) { // check for existing productnum already
              throw new StockException("Product already exists: " + product.getProductNum());
              // if exists, catch error
          }

          
          // sql query to create the new product in the database
          // utilises insert to add the product
          String sqlProduct = String.format(
              "INSERT INTO ProductTable (productNo, description, price, imagePath) VALUES ('%s', '%s', %.2f, '%s')",
              product.getProductNum(),
              product.getDescription(),
              product.getPrice(),
              product.getImagePath() != null ? product.getImagePath() : "NULL" // if image is null, set the field to null in db
          );
          getStatementObject().executeUpdate(sqlProduct); // execute the sql


          String sqlStock = String.format(
              "INSERT INTO StockTable (productNo, stockLevel) VALUES ('%s', %d)", // query to inset stock
              product.getProductNum(), // retreive product id from create window
              product.getQuantity() // retreive quantiy from create window
          );
          getStatementObject().executeUpdate(sqlStock); // update the db with the quantity

          System.out.println("Product added successfully: " + product.getProductNum()); // output if success
          
      } catch (SQLException e) {
          throw new StockException("Failed to add product: " + e.getMessage()); // catch error if failed
      }
  }
  
  // ensures db contains the neccessary fields required for new product creation
  // developed by jakub
  private void ensureSchemaUpdated() throws SQLException {
	  
	    try {
	        ResultSet rs = getConnectionObject()
	            .getMetaData()
	            .getColumns(null, null, "PRODUCTTABLE", "IMAGEPATH"); // check for imagepath column
	        
	        if (!rs.next()) { // if column does not exist...
	            getStatementObject().executeUpdate("ALTER TABLE ProductTable ADD COLUMN imagePath VARCHAR(255)"); // cretae the column in the db
	            System.out.println("imagePath column added to ProductTable."); // output success
	            
	        } else {
	            System.out.println("imagePath column already exists in ProductTable."); // if exists, this is the msg that outputs informing it already exists
	        }
	        
	    } catch (SQLException e) {
	    	
	        if (e.getSQLState().equals("42X14")) { // state 42x14 meaning column already exists
	            System.out.println("imagePath column already exists."); // catch error from db
	            
	        } else {
	            throw e;
	        }
	    }
	}
}
