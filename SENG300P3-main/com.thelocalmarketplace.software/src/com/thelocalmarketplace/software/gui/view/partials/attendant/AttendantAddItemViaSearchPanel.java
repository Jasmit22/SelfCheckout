package com.thelocalmarketplace.software.gui.view.partials.attendant;

import javax.swing.*;
import javax.swing.border.Border;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

/**
 * This panel allows an attendant to add items to a transaction via a text search interface.
 */

/**
 * @author Connell Reffo		(10186960)
 * @author Tara Strickland 		(10105877)
 * @author Julian Fan			(30235289)
 * @author Samyog Dahal			(30194624)
 * @author Phuong Le			(30175125)
 * @author Daniel Yakimenka		(10185055)
 * @author Merick Parkinson		(30196225)
 * @author Julie Kim			(10123567)
 * @author Ajaypal Sallh		(30023811)
 * @author Nathaniel Dafoe		(30181948)
 * @author Anmol Ratol			(30231177)
 * @author Chantal del Carmen	(30129615)
 * @author Dana Al Bastrami		(30170494)
 * @author Maria Munoz			(30175339)
 * @author Ernest Shukla		(30156303)
 * @author Hillary Nguyen		(30161137)
 * @author Robin Bowering		(30123373)
 * @author Anne Lumumba			(30171346)
 * @author Jasmit Saroya		(30170401)
 * @author Fion Lei				(30134327)
 * @author Royce Knoepfli		(30172598)
 */

public class AttendantAddItemViaSearchPanel extends JPanel {

    private static final long serialVersionUID = -3351284492475906037L;

    public JButton switchToStartButton;
    private JButton startSearch;
    private JButton backToSearch;
    private JTextField textField;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultListModel<String> model;
    private JList<String> list;
    private JLabel messageLabel;
    private JLabel messageLabel2;
    private HashMap<String, Product> productMap;

    
    /**
     * Constructs the AttendantAddItemViaSearchPanel.
     *
     * @param logic The SelfCheckoutStationSoftware logic to be used for handling actions.
     */
    public AttendantAddItemViaSearchPanel(SelfCheckoutStationSoftware logic) {
        initializeComponents();
        setupActions(logic);
        buildLayout();
    }

    /**
     * Initializes GUI components and their properties.
     */
    private void initializeComponents() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFixedCellHeight(60);
        list.setFixedCellWidth(300);
        list.setCellRenderer(new CenterListCellRenderer());

        backToSearch = new JButton("Back");
        switchToStartButton = new JButton("Back");
        startSearch = new JButton("Search");

        textField = new JTextField(25);
        textField.setHorizontalAlignment(JTextField.CENTER);

        messageLabel = new JLabel("");
        messageLabel2 = new JLabel("");
        messageLabel2.setVerticalAlignment(SwingConstants.TOP);

        productMap = new HashMap<>();
    }

    /**
     * Sets up the actions associated with GUI components.
     *
     * @param logic The SelfCheckoutStationSoftware logic to be used for handling actions.
     */
    private void setupActions(SelfCheckoutStationSoftware logic) {
        startSearch.addActionListener(e -> {
            cardLayout.show(cardPanel, "ResultPanel");
            performSearch(logic);
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (!logic.isSessionActive()) {
                    messageLabel.setForeground(Color.RED);
                    displayMessage("The session is inactive");
                } else if (evt.getClickCount() == 1) {
                    String selectedValue = list.getSelectedValue();
                    Product selectedProduct = productMap.get(selectedValue);
                    if (selectedProduct != null) {
                        logic.getProductLogic().productSelectedToAdd(selectedProduct);
                        
                        messageLabel.setForeground(Color.GREEN);
                        displayMessage("Item has been added");
                    }
                }
            }
        });

        backToSearch.addActionListener(e -> {
            cardLayout.show(cardPanel, "SearchPanel");
            displayMessage("");
            messageLabel2.setText("");
        });
    }

    /**
     * Builds the layout of the GUI and adds components to the panels.
     */
    private void buildLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new TopTitlePanel("Add Item By Text Search"));
        add(cardPanel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(textField);
        inputPanel.add(startSearch);
        inputPanel.add(switchToStartButton);
        inputPanel.add(messageLabel2, BorderLayout.PAGE_END);

        searchPanel.add(inputPanel, BorderLayout.CENTER);
       

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(messageLabel, BorderLayout.PAGE_START);
        resultPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        resultPanel.add(backToSearch, BorderLayout.PAGE_END);

        cardPanel.add(searchPanel, "SearchPanel");
        cardPanel.add(resultPanel, "ResultPanel");

        
        Utilities.setComponentColour(searchPanel);
        Utilities.setComponentColour(backToSearch);
        Utilities.setComponentColour(resultPanel);
        Utilities.setComponentColour(switchToStartButton);

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel2.setForeground(Color.RED);
        messageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Displays a message to the user.
     *
     * @param message The message to display.
     */
    private void displayMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Performs the product search based on the input from the text field.
     *
     * @param logic The SelfCheckoutStationSoftware logic to be used for handling the search.
     */
    private void performSearch(SelfCheckoutStationSoftware logic) {
        String description = textField.getText();
        
        if (description != null && !description.trim().isEmpty()) {
        	List<Product> products = logic.getAddItemByTextLogic().findProductByTextDescription(description);
        	
        	if (products.isEmpty()) {
        		cardLayout.show(cardPanel, "SearchPanel");
        		messageLabel2.setText("No products found.");
        	}
        	else {
        		updateProductList(products);
        	}
        }
    	else if (description.trim().isEmpty()) {
    		cardLayout.show(cardPanel, "SearchPanel");
    		messageLabel2.setText("Description cannot be empty.");
    	}
    }
    
    /**
     * Updates the product list model with search results.
     *
     * @param products The list of products to display in the JList.
     */
    private void updateProductList(List<Product> products) {
        model.clear();
        productMap.clear();
        
        for (Product product : products) {
            if (product instanceof BarcodedProduct) {
                BarcodedProduct barcodedItem = (BarcodedProduct) product;
                String productString = barcodedItem.getDescription();
                
                model.addElement(productString);
                productMap.put(productString, barcodedItem);
            }            
            else if (product instanceof PLUCodedProduct) {
                PLUCodedProduct pluCodedItem = (PLUCodedProduct) product;
                String productString = pluCodedItem.getDescription();
                
                model.addElement(productString);
                productMap.put(productString, pluCodedItem);
            }
        }
    }
    
    /**
	 * Centers the text of the JList and adds borders between the items
	 */
    private class CenterListCellRenderer extends DefaultListCellRenderer {
    	
			private static final long serialVersionUID = -1928214182604776405L;
			
			private final Border lineBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	        private final Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	        private final Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

	        @Override
	        public Component getListCellRendererComponent(
	                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	            setHorizontalAlignment(CENTER); // Center text
	            setBorder(compoundBorder); // Add a line border with padding
	            return this;
	        }
	    }
    }
