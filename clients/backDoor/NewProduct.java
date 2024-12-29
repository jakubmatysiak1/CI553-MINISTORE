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
	
    private final JTextField txtProductId = new JTextField();
    private final JTextField txtProductName = new JTextField();
    private final JTextField txtProductPrice = new JTextField();
    private final JButton btnChooseImage = new JButton("Choose Image");
    private final JButton btnSave = new JButton("Save");
    private final JLabel lblImagePath = new JLabel("No file chosen");

    private final StockReadWriter stock;

    public NewProduct(BackDoorModel model) {
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
    }

    private void chooseImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        
        int returnValue = fileChooser.showOpenDialog(this);
        
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

        try {
            if (productId.isEmpty() || productName.isEmpty() || productPriceStr.isEmpty() || imagePath.equals("No file chosen")) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            double productPrice = Double.parseDouble(productPriceStr);
            Product newProduct = new Product(productId, productName, productPrice, imagePath);
            stock.addProduct(newProduct);

            JOptionPane.showMessageDialog(this, "Product added successfully!");
            dispose();
            
        } catch (NumberFormatException ex) {
        	
            JOptionPane.showMessageDialog(this, "Invalid price format.");
            
        } catch (StockException | IllegalArgumentException ex) {
        	
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
