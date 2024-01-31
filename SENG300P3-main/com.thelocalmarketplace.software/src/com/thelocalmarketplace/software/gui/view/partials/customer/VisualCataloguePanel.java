package com.thelocalmarketplace.software.gui.view.partials.customer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.listeners.customer.VisualCatalogueProductSelectButtonListener;

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

public class VisualCataloguePanel extends JPanel {
	
	private static final long serialVersionUID = 6156278343103196489L;
	
	private CustomerView customerView;
	
	private JPanel mainPanel;
	private JPanel cardPanelImages;
	private CardLayout cardLayoutImages;
	
	
	private JButton backButton;
	private JButton leftButton;
	private JButton rightButton;
	
	//private JLayeredPane layeredPane = new JLayeredPane();
	
	private int currentItemPanel;
	private ISelfCheckoutStationSoftware c;
	
	public List<Integer> allPanels = new ArrayList<Integer>();
	public List<JButton> allImageButtons = new ArrayList<JButton>();
	
	 /**
     * Base constructor for the visual catalog
     */
	public VisualCataloguePanel(ISelfCheckoutStationSoftware logic, CustomerView cv) {
    	
    	this.c = logic;
    	this.mainPanel = new JPanel();
    	this.customerView = cv;
    	this.cardLayoutImages = new CardLayout();
        this.cardPanelImages = new JPanel(this.cardLayoutImages);
        
        currentItemPanel = 0;
        
    }

	 /**
     * Disables the catalog except the back button when station is blocked
     */
	
	public void disableAddingButtons() {
		for (JButton b : this.allImageButtons) {
			b.setEnabled(false);
		}
		//this.cardPanelImages.setEnabled(false);
		this.rightButton.setEnabled(false);
		this.leftButton.setEnabled(false);
		this.cardPanelImages.revalidate();
		this.cardPanelImages.repaint();
		System.out.println("disabling visualcatalog");
	}
	
	 /**
     * enables the catalog except the back button when station is blocked
     */
	
	public void enableAddingButtons() {
		for (JButton b : this.allImageButtons) {
			b.setEnabled(true);
		}
		this.rightButton.setEnabled(true);
		this.leftButton.setEnabled(true);
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	
	
	
	 /**
     * Creates a button with the name text on it and the image at path overlayed, with size width by height
     */
	
    public JButton createButton(String text, String path, int width, int height, String price) {
    	//Rectangle r = new Rectangle(22, 22);
    	
    	try {
    		 JButton button = new JButton();
    		 button.setText(price);
             Image img = (new ImageIcon(getClass().getResource(path)).getImage()).getScaledInstance(width,height, Image.SCALE_SMOOTH);

        	//ImageIcon defaultIcon = new ImageIcon(getClass().getResource(path));
            ImageIcon i = new ImageIcon(img);
        	button.setIcon(i);
        	button.setDisabledIcon(i);
        	return button;
        }catch(Exception e) {
        	 JButton button = new JButton(text);
             Image img = (new ImageIcon(getClass().getResource("/placeholder.png")).getImage()).getScaledInstance(width,height, Image.SCALE_SMOOTH);

        	//ImageIcon defaultIcon = new ImageIcon(getClass().getResource(path));
            ImageIcon i = new ImageIcon(img);
        	button.setIcon(i);
        	button.setDisabledIcon(i);
        	return button;
        }
    }
    
    /**
     * Shows panel in card layout
     */
    public void showPanel(String panel) {
    	this.cardLayoutImages.show(this.cardPanelImages, panel);
    }
    
    /**
     * Initializes the visual catalog
     */
	public JPanel getVisualCatalog(String label, int width, int height){
		
		//puts image buttons on mainPanel
		JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
		Dimension buttonMaxSize = new Dimension(width / 3, height / 3);
		TitledBorder b = BorderFactory.createTitledBorder(label);
		b.setTitleColor(Color.LIGHT_GRAY);
		
        panel.setBorder(b);
        panel.setMaximumSize(new Dimension(width-5, height));
        int panel_label = 0;
        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);
        this.mainPanel.setLayout(layout);
        backButton = new JButton("Back");
        this.mainPanel.add(backButton, BorderLayout.NORTH); 
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	backButtonPushed();
            	
            }
        });
        
        
        leftButton = new JButton(" ");
        this.mainPanel.add(leftButton, BorderLayout.WEST); 
        
        Image img = (new ImageIcon(getClass().getResource("/left.png")).getImage()).getScaledInstance(30,30, Image.SCALE_SMOOTH);
    	leftButton.setIcon(new ImageIcon(img));
        
        //leftButton.setPreferredSize(new Dimension(5,5));
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	leftPushed();
            	
            }
        });
       
        
        rightButton = new JButton(" ");
        this.mainPanel.add(rightButton, BorderLayout.EAST); 
        
        Image img2 = (new ImageIcon(getClass().getResource("/right.png")).getImage()).getScaledInstance(30,30, Image.SCALE_SMOOTH);
    	rightButton.setIcon(new ImageIcon(img2));
       // rightButton.setPreferredSize(new Dimension(5,5));
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	rightPushed();
            	
            }
        });
       
        int cnt = 0;
		
		for (var entry : ProductDatabases.INVENTORY.entrySet()) {
			Product p = entry.getKey();
			String descriptor = "";
			String price = "";
			if (p instanceof BarcodedProduct) {
				BarcodedProduct bp = (BarcodedProduct)p;
				
				descriptor = bp.getDescription();
				price = "<html>" +descriptor+" "+"<font color = 'green'><br/> $"+bp.getPrice()+"</font></html>";
				
			}else {
				PLUCodedProduct pc = (PLUCodedProduct) p;
				
				descriptor = pc.getDescription();
				price = "<html>" +descriptor+" "+"<font color = 'green'> <br/> $"+pc.getPrice()+" / kg</font></html>";
			}

			if (entry.getValue() > 0) {
				if (cnt % 9 == 0 && cnt != 0) {
					this.cardPanelImages.add(panel,Integer.toString(panel_label));
					this.allPanels.add(panel_label);
					panel = new JPanel(new GridLayout(3, 3, 10, 10));
					panel.setBorder(b);
			        panel.setMaximumSize(new Dimension(width-5, height));
			        panel_label++;
				}
				String path =( "/"+descriptor+ ".png").toLowerCase();
				JButton button1 = this.createButton(descriptor, path , width/7,height/7, price);

				VisualCatalogueProductSelectButtonListener buttonListener = new VisualCatalogueProductSelectButtonListener(this.c, p);
		        button1.addActionListener(buttonListener);
		       
				button1.setMaximumSize(buttonMaxSize);
				this.allImageButtons.add(button1);
		        panel.add(button1);
		        cnt++;
			}
		}
		
		if (allPanels.contains(panel_label)) {
			
		}else {
			this.cardPanelImages.add(panel,Integer.toString(panel_label));
			allPanels.add(panel_label);
		}
		System.out.println(allPanels.size());
		
		//this.showPanel(Integer.toString(this.currentItemPanel));
		this.mainPanel.add(this.cardPanelImages, BorderLayout.CENTER);
		return this.mainPanel;
	}
	

    /**
     * Gets current item panel used for tests
     */
	
	public int getCurrentPanel() {
		return this.currentItemPanel;
	}
	

    /**
     * Handles right arrow push
     */
	public void rightPushed() {
		if (this.currentItemPanel==Collections.max(this.allPanels)) {
			this.currentItemPanel = 0;
		} else {
			this.currentItemPanel++;
		}
		
		this.showPanel(Integer.toString(this.currentItemPanel));
	}
	
	/**
     * Handles left arrow push
     */
	public void leftPushed() {
		if (this.currentItemPanel==0) {
			this.currentItemPanel = Collections.max(this.allPanels);
		} else {
			this.currentItemPanel--;
		}
		
		this.showPanel(Integer.toString(this.currentItemPanel));
	}
	

	/**
     * Handles back button push
     */
	public void backButtonPushed() {
		this.customerView.showPanel("mainMenu");
	}
}
