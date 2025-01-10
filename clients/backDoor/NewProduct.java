package clients.backDoor;

import middle.StockException;
import middle.StockReadWriter;

import javax.swing.*;

import catalogue.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

// provides a window to create new prodcuts, provides entry fields and filechoosers.
// class developed by jakub
public class NewProduct extends JFrame {
	
    private final JTextField txtProductId = new JTextField(); // product id field
    private final JTextField txtProductName = new JTextField(); // product name field
    private final JTextField txtProductPrice = new JTextField(); // product price field
    private final JButton btnChooseImage = new JButton("Choose Image"); // choose image btn
    private final JButton btnSave = new JButton("Save"); // product save btn
    private final JLabel lblImagePath = new JLabel("No file chosen"); // image path label

    private final StockReadWriter stock;

    public NewProduct(BackDoorModel model) { // model object to open new window
        this.stock = model.getStock();

        setTitle("Create New Product");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Product ID:"));
        add(txtProductId);

        add(new JLabel("Product Name:"));
        add(txtProductName);

        add(new JLabel("Product Price:"));
        add(txtProductPrice);

        add(btnChooseImage);
        add(lblImagePath);

        add(btnSave);

        btnChooseImage.addActionListener(this::chooseImage);
        btnSave.addActionListener(this::saveProduct);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // all of above is code which setups the window
        // adds all objects like btn and fields
        // adds listeners to check for user input for btn
    }

    // method to choose file img
    private void chooseImage(ActionEvent e) {
    	
        JFileChooser fileChooser = new JFileChooser(); // lanches file chooser from operating system
        
        int returnValue = fileChooser.showOpenDialog(this); // retrives path selected
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            lblImagePath.setText(selectedFile.getAbsolutePath());
        }
    }

    private void saveProduct(ActionEvent e) {
    	
        String productId = txtProductId.getText().trim();
        String productName = txtProductName.getText().trim();
        String productPriceStr = txtProductPrice.getText().trim();
        String imagePath = lblImagePath.getText();
        // all data collected from input fields

        try {
            if (productId.isEmpty() || productName.isEmpty() || productPriceStr.isEmpty() || imagePath.equals("No file chosen")) { // a check to ensure fields are not empty
                throw new IllegalArgumentException("All fields must be filled");
            }

            double productPrice = Double.parseDouble(productPriceStr);
            
            Product newProduct = new Product(productId, productName, productPrice, imagePath); // create new product object
            
            stock.addProduct(newProduct); // add object to the db through method

            JOptionPane.showMessageDialog(this, "Product added successfully!"); // msg to show success
            dispose();
            
        } catch (NumberFormatException ex) {
        	
            JOptionPane.showMessageDialog(this, "Invalid price format.");
            
        } catch (StockException | IllegalArgumentException ex) {
        	
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        
        // exceptions catching invalid inputs
        // like wrong price format or missing fields etc
    }
}
