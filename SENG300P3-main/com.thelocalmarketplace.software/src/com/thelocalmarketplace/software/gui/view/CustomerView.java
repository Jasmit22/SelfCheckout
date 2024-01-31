package com.thelocalmarketplace.software.gui.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.IViewable;
import com.thelocalmarketplace.software.gui.view.listeners.customer.CallAttendantButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterMembershipCallback;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterPLUCallback;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterPINCallback;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterBagCountCallback;
import com.thelocalmarketplace.software.gui.view.listeners.customer.RemoveItemButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.customer.SessionEndSessionButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.customer.SessionStartSessionButtonListener;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.AddItemPopUp;
import com.thelocalmarketplace.software.gui.view.partials.customer.AddingOwnBagsPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.CreditPaymentPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.DebitPaymentPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.DisabledStationPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.DiscrepencyOverlayPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.EndSessionPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.NumpadPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.MainPaymentPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.CashPaymentPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.StartSessionPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.VisualCataloguePanel;

/**
 * Represents what the customer sees on their screen
 * 
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

public class CustomerView extends AbstractLogicDependant implements IViewable {

    public JPanel itemsDisplay;
    private JPanel cardPanel;

    private JPanel cardPanelRightDisplay;
    private CardLayout cardLayoutRightDisplay;
    
    private JPanel reminderPanel;
    private CardLayout reminderLayout;
    
    private List<JButton> disabledWhenBlocked = new ArrayList<JButton>();

    private JPanel rightButtonPannel;
    // private CardLayout cardLayout = new CardLayout();
    private JFrame frame;
    private CardLayout cardLayout;
    private List<String> items;

    private Mass display_weight = new Mass(new BigDecimal("0"));
    //private BigDecimal display_total = new BigDecimal("0.0");

    public DisabledStationPanel disabledStationPanel;
    public StartSessionPanel startSessionPanel;
    public NumpadPanel numpad_PINCredit;
    public NumpadPanel numpad_PINDebit; 
    public NumpadPanel numpad_PLUItem_Panel;
    public NumpadPanel numpad_PurchaseBags_Panel;
    public NumpadPanel numpad_MembershipType_Panel;
    public MainPaymentPanel selectPaymentPanel;
    public CreditPaymentPanel creditPaymentPanel;
    public DebitPaymentPanel debitPaymentPanel;
    public CashPaymentPanel cashPaymentPanel;
    
    public AddItemPopUp addItemToBaggingPopUp;
   
    public VisualCataloguePanel visualCatalog;

    public JLabel addItemReminder;
    public JLabel addBagsReminder;
    public JLabel removeItemReminder;
    
    public EndSessionPanel endSessionPanel;
    public JLabel custTotalLabel;
    public JLabel memberLabel;
    public JLabel weightLabel;
    public JButton TypeInMemberNoButton = new JButton("Type In Member #");
    public JButton DontBagItemButton = new JButton("Dont Bag Item");
    public JButton CallAttendantButton = new JButton("Call Attendant");
    public JButton AddOwnBagsButton = new JButton("Add Own Bags");
    public JButton PurchaseBagsButton = new JButton("Purchase Bags");
    public JButton button6 = new JButton("");
    public JButton AddItem_PLUButton = new JButton("Add Item (PLU Code)");
    public JButton AddItem_VisualButton = new JButton("Add Item (Visual Catalog)");
    public JButton PayForOrderButton = new JButton("Pay For Order");
    
    
    public JLayeredPane layeredPane = new JLayeredPane();
    public DiscrepencyOverlayPanel discrepencyPanelAddItem;
    
    public AddingOwnBagsPanel addingOwnBagsPanel;
    
    public String currentCardPanel;
    
    public List<JButton> removeButtons;
    
    /**
	 * Tracks the last panel on before disabled
	 */
	private String lastCardPanel;
	
	public void markLastPanel() {
		this.lastCardPanel = currentCardPanel;
	}
	
	public void gotoLastPanel() {
		this.switchCardLayoutView(this.lastCardPanel);
	}
	
    
    /**
	 * Tracks weather or not the discrepency screen has been added to customer view
	 */
	private boolean hasAddedDiscrepencyPane = false;

    public CustomerView(ISelfCheckoutStationSoftware logic) throws NullPointerException {
        super(logic);

        this.removeButtons = new ArrayList<JButton>();
        
        this.lastCardPanel = "";
        
        this.frame = new JFrame("Customer Screen");
        this.hide();
        
        this.button6.setEnabled(false);

        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(this.cardLayout);
        
        this.reminderLayout = new CardLayout();
        this.reminderPanel = new JPanel(this.reminderLayout);
        
        this.cardLayoutRightDisplay = new CardLayout();
        this.cardPanelRightDisplay = new JPanel(this.cardLayoutRightDisplay);

        this.disabledStationPanel = new DisabledStationPanel();
        
        this.startSessionPanel = new StartSessionPanel();
        this.endSessionPanel = new EndSessionPanel();
        
        this.selectPaymentPanel = new MainPaymentPanel();
        this.creditPaymentPanel = new CreditPaymentPanel();
        this.debitPaymentPanel = new DebitPaymentPanel();
        this.cashPaymentPanel = new CashPaymentPanel();
        
        this.discrepencyPanelAddItem = new DiscrepencyOverlayPanel("The Weight On The Scale Is Not As Expected", this);
        this.addingOwnBagsPanel = new AddingOwnBagsPanel("Please place your bags in bagging area and tap done when finished", this);

        this.layeredPane.setLayout(new OverlayLayout(this.layeredPane));
        
        this.addItemToBaggingPopUp = new AddItemPopUp(this, frame);
        this.visualCatalog = new VisualCataloguePanel(logic, this);

        this.numpad_PLUItem_Panel = new NumpadPanel(this, new EnterPLUCallback(this.logic));
        this.numpad_MembershipType_Panel = new NumpadPanel(this, new EnterMembershipCallback(this.logic));
        this.numpad_PurchaseBags_Panel = new NumpadPanel(this, new EnterBagCountCallback(this.logic));
        this.numpad_PINCredit = new NumpadPanel(this, new EnterPINCallback(this.logic));
        this.numpad_PINDebit = new NumpadPanel(this, new EnterPINCallback(this.logic));
        
        this.CallAttendantButton.addActionListener(new CallAttendantButtonListener(logic));
        
        this.numpad_PLUItem_Panel.buttonENTER.addActionListener(new ActionListener() {
  
			@Override
			public void actionPerformed(ActionEvent e) {
				switchCardLayoutView("checkoutPanel");
				logic.getStateLogic().gotoState(States.NORMAL);
			}
		});
        
        this.numpad_MembershipType_Panel.buttonENTER.addActionListener(new ActionListener() {
        	  
			@Override
			public void actionPerformed(ActionEvent e) {
				switchCardLayoutView("checkoutPanel");
				logic.getStateLogic().gotoState(States.NORMAL);
			}
		});
        
        this.numpad_PurchaseBags_Panel.buttonENTER.addActionListener(new ActionListener() {
        	  
			@Override
			public void actionPerformed(ActionEvent e) {
				switchCardLayoutView("checkoutPanel");
				logic.getStateLogic().gotoState(States.NORMAL);
			}
		});
        
// SESSION SCREENS
        // SwitchPanels: Start Session
        // Listener: Session_StartSessionButtonListener        
        this.startSessionPanel.startSessionButton.addActionListener(new SessionStartSessionButtonListener(this.logic));

        // SwitchPanels: End Session
        this.endSessionPanel.endSessionButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
            	switchCardLayoutView("startSessionPanel");
            }});
        // Listener: SessionEndSessionButtonListener        
        this.endSessionPanel.endSessionButton.addActionListener(new SessionEndSessionButtonListener(this.logic));

// BAGS 
        // SwitchPanels - Purchase Bags - to Panel
        this.PurchaseBagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	logic.getBuyBagsLogic().startBuyBags();
                switchCardLayoutView("numpad_PurchaseBags_Panel");
            }
        });
     
        // SwitchPanels - Purchase Bags - back to checkout 
        this.numpad_PurchaseBags_Panel.returnCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("checkoutPanel");
                logic.getBuyBagsLogic().endBuyBags();
            }
        });

        this.AddOwnBagsButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		logic.getMassLogic().startAddBags();
        	}
        });

        this.DontBagItemButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dontBagItemPushed();
        	}
        });

        this.TypeInMemberNoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("numpad_MembershipType_Panel");
                
                // Change state so station is ready to add membership
                logic.getStateLogic().gotoState(States.ADDMEMBERSHIP);
            }
        });

        // Switch Panels: Customer returns to Checkout Panel
        this.numpad_MembershipType_Panel.returnCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("checkoutPanel");
            }
        });
        
// CHECKOUT: ADD ITEM PLU 
        // Switch Panels: Customer wants to add Item VIA PLU        
        this.AddItem_PLUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("numpad_PLUItem_Panel");
            }
        });

        // Switch Panels: Customer returns to Checkout Panel
        this.numpad_PLUItem_Panel.returnCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("checkoutPanel");
            }
        });

// ADD ITEM VISUAL CAT
        // AddItem_VisualButton
        // Opens the Panel for this
        this.AddItem_VisualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("visualCatalog");
            }
        });

// PAY FOR ORDER 
        // CustomerWantsToPayBillButton
        this.PayForOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCardLayoutView("selectPaymentPanel");
                logic.selectPaymentMethod(PaymentMethods.NONE);
            }
        });
                
        // Return to checkout button in enter membership numpad should return back to normal state
        this.numpad_MembershipType_Panel.returnCheckout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { logic.getStateLogic().gotoState(States.NORMAL); }
		});
        
        this.numpad_PurchaseBags_Panel.returnCheckout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { logic.getStateLogic().gotoState(States.NORMAL); }
		});

 
 // PAYMENT SELECT 
         // Debit Button 
         this.selectPaymentPanel.debitButton.addActionListener(new ActionListener() {
             @Override public void actionPerformed(ActionEvent e) {
                 switchCardLayoutView("debitPaymentPanel");
                 
                 logic.selectPaymentMethod(PaymentMethods.DEBIT);
                 logic.getStateLogic().gotoState(States.CHECKOUT);
         }});
         // Credit Button
         this.selectPaymentPanel.creditButton.addActionListener(new ActionListener() {
             @Override public void actionPerformed(ActionEvent e) {
                 switchCardLayoutView("creditPaymentPanel");
                 
                 logic.selectPaymentMethod(PaymentMethods.CREDIT);
                 logic.getStateLogic().gotoState(States.CHECKOUT);
         }});
         // Cash Button
         this.selectPaymentPanel.cashButton.addActionListener(new ActionListener() {
             @Override public void actionPerformed(ActionEvent e) {
                 switchCardLayoutView("cashPaymentPanel");
                 
                 logic.selectPaymentMethod(PaymentMethods.CASH);
                 logic.getStateLogic().gotoState(States.CHECKOUT);
         }});
         
         // Back to Checkout 
         this.selectPaymentPanel.backToCheckoutButton.addActionListener(new ActionListener() {
             @Override public void actionPerformed(ActionEvent e) {
                 switchCardLayoutView("checkoutPanel");
                 
                 logic.selectPaymentMethod(PaymentMethods.NONE);
                 logic.getStateLogic().gotoState(States.NORMAL);
         }});      

// CASH PANEL 
        
         this.cashPaymentPanel.backToPaymentButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 switchCardLayoutView("selectPaymentPanel");
                 
                 logic.selectPaymentMethod(PaymentMethods.NONE);
                 logic.getStateLogic().gotoState(States.NORMAL);
             }
         });
         
// DEBIT PANEL 
         // RETURN TO SELECT PAYMENT 
         this.debitPaymentPanel.backToPaymentButton.addActionListener(new ActionListener() {
         	@Override public void actionPerformed(ActionEvent e) {
         		switchCardLayoutView("selectPaymentPanel");
         		
         		logic.selectPaymentMethod(PaymentMethods.NONE);
         		logic.getStateLogic().gotoState(States.NORMAL);
         	}
         });
         
         this.debitPaymentPanel.PINButton.addActionListener(new ActionListener() {
        	 @Override public void actionPerformed(ActionEvent e) {
        		 switchCardLayoutView("numpad_PINDebit");
        		 // Logic 
        	 }
         });
         
         this.numpad_PINDebit.returnCheckout.addActionListener(new ActionListener() {
        	 @Override public void actionPerformed(ActionEvent e) {
        		 switchCardLayoutView("debitPaymentPanel");
        		 // Logic
        	 }
         });
         
         
// CREDIT PANEL 
         // RETURN TO SELECT PAYMENT 
         this.creditPaymentPanel.backToPaymentButton.addActionListener(new ActionListener() {
         	@Override public void actionPerformed(ActionEvent e) {
         		switchCardLayoutView("selectPaymentPanel");
         		
         		logic.selectPaymentMethod(PaymentMethods.NONE);
         		logic.getStateLogic().gotoState(States.NORMAL);
         	}
         });
         
         this.creditPaymentPanel.PINButton.addActionListener(new ActionListener() {
        	 @Override public void actionPerformed(ActionEvent e) {
        		 switchCardLayoutView("numpad_PINCredit");
        	 }
         });
         
         this.numpad_PINCredit.returnCheckout.addActionListener(new ActionListener() {
        	 @Override public void actionPerformed(ActionEvent e) {
        		 switchCardLayoutView("creditPaymentPanel");
        	 }
         });
         
         this.numpad_PINCredit.buttonENTER.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { switchCardLayoutView("creditPaymentPanel"); }
		});
         
         this.numpad_PINDebit.buttonENTER.addActionListener(new ActionListener() {
 			
 			@Override
 			public void actionPerformed(ActionEvent e) { switchCardLayoutView("debitPaymentPanel"); }
 		});
    }
    

    @Override
    public void start() {

        // Create Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        
        layeredPane.setSize(1000,600);
        //layeredPane.setLayout(new FlowLayout());
        
        //frame.setContentPane(layeredPane);
        Container pane = frame.getContentPane();

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // Create and add panels to the card panel
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        // Add Panels to the card panel
        this.cardPanel.add(disabledStationPanel, "disabledStationPanel");
        this.cardPanel.add(startSessionPanel, "startSessionPanel");
        this.cardPanel.add(checkoutPanel(), "checkoutPanel"); // Special Case <-- COULD NOT Separate
        this.cardPanel.add(endSessionPanel, "endSessionPanel");
        this.cardPanel.add(numpad_PLUItem_Panel, "numpad_PLUItem_Panel");
        this.cardPanel.add(numpad_MembershipType_Panel, "numpad_MembershipType_Panel");
        this.cardPanel.add(numpad_PurchaseBags_Panel, "numpad_PurchaseBags_Panel");
        this.cardPanel.add(addItemToBaggingPopUp, "addItemToBaggingPopUp");
        this.cardPanel.add(visualCatalog, "visualCatalog");
       // this.cardPanel.add(this.discrepencyPanelAddItem, "discrepency detected");
        this.cardPanel.add(selectPaymentPanel, "selectPaymentPanel");
        this.cardPanel.add(creditPaymentPanel,"creditPaymentPanel");
        this.cardPanel.add(numpad_PINCredit, "numpad_PINCredit");
        this.cardPanel.add(debitPaymentPanel,"debitPaymentPanel");
        this.cardPanel.add(numpad_PINDebit, "numpad_PINDebit");
        this.cardPanel.add(cashPaymentPanel, "cashPaymentPanel");
        

        pane.add(new TopTitlePanel("Station: " + this.logic.getName()));
        layeredPane.add(this.cardPanel,JLayeredPane.MODAL_LAYER);
        
        pane.add(layeredPane);

        this.cardPanel.setLayout(cardLayout);
        this.setColors(cardPanel); // Set Colours to Dark Mode

        this.showPanel("mainMenu");
        frame.revalidate();
        frame.repaint();
    }
    
    public void notify(String message) {
    	JOptionPane.showMessageDialog(this.frame, message);
    }

    @Override
    public void show() {
        this.frame.setVisible(true);
    }

    @Override
    public void hide() {
        this.frame.setVisible(false);
    }

    // --------------------------------------------------------------------------
    // Functions/Methods in Customer View
    // --------------------------------------------------------------------------

    /**
     * Recursively sets the background and foreground colors for a given container
     * and its components.
     *
     * @param container The container for which colors are set
     */
    void setColors(Container container) {
        container.setBackground(Color.BLACK);
        container.setForeground(Color.WHITE);

        for (Component component : container.getComponents()) {
//            if (component instanceof Container) {
                setColors((Container) component);
//            } else {
//                component.setBackground(Color.BLACK);
//                component.setForeground(Color.WHITE);
//            }
        }
    }
    

    /**
     * Switches the displayed panel in the card layout based on the provided panel
     * identifier.
     * Repaints the cardPanel to reflect the changes.
     *
     * @param panelIdentifier The identifier of the panel to be displayed
     */
    public void switchCardLayoutView(String panel) {
    	this.currentCardPanel = panel;
        cardLayout.show(cardPanel, panel);
        cardPanel.revalidate(); // Repaint the cardPanel to reflect the changes
        cardPanel.repaint();
    }

    
    //For Testings
    public String getCurrentCardLayoutView() {
        return currentCardPanel;
    }
    
// DISPLAY TOTALS

    /**
     * Updates the displayed weight with the given mass.
     *
     * @param mass The mass to be displayed.
     */
    public void updateDisplayWeight(Mass mass) {
        this.display_weight = mass;
        this.weightLabel.setText("Weight: " + display_weight.inGrams().doubleValue()+ " g");
    }

    /**
     * Gets the currently displayed weight.
     *
     * @return The displayed weight.
     */
    public Mass getDisplayWeight() {
        return this.display_weight;
    }

    /**
     * Updates the displayed total with the given BigDecimal.
     *
     * @param total The total to be displayed.
     */
    public void updateDisplayTotal(BigDecimal total) {
        this.custTotalLabel.setText("Total is: $" + total.setScale(2, RoundingMode.HALF_DOWN).toString());
        
        this.selectPaymentPanel.setTotal(total);
        this.debitPaymentPanel.setTotal(total);
        this.creditPaymentPanel.setTotal(total);
        this.cashPaymentPanel.setTotal(total);
    }

    /**
     * Updates the displayed Member No. with the given Membership Number.
     *
     * @param total The total to be displayed.
     */
    public void updateDisplayMember(String memNo) {
    	if (memNo == null) {
    		memNo = "?";
    	}
    	
        this.memberLabel.setText("Member No: " + memNo);
    }

    // --------------------------------------------------------------------------
    // Customer View Methods related to adding Items to the Checkout Display
    // --------------------------------------------------------------------------

    // Spent two hours trying to seperate this
    // Errors: If I move the panel, CustomerView and SimulationView could not find
    // the CheckoutPanel
    // Errors: If I move addNewLabel, then SimulationView could not add items

    /**
     * Adds a new item to the list and updates the UI to reflect the changes.
     * Very important for Simulation View to add item to the UI
     * 
     * @param item The item to be added to the list.
     */
    public void addNewItem(String item) {
        // Add logic to add the item to the list and update the UI
        // For example:
        items.add(item);
        updateUI();
    }

    /**
     * Retrieves the list of items currently stored in the simulation.
     * Very important for Simulation View to add item to the UI
     * 
     * @return The list of items in the simulation.
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * Updates the UI based on the current list of items.
     * Removes all existing components from the frame and adds new components for
     * each item.
     * Repaints and revalidates the frame to reflect the changes.
     * *Very important for Simulation View to add item to the UI
     */
    private void updateUI() {
        frame.removeAll();
        // Add logic to update the UI based on the items list
        // For example:
        for (String item : items) {
            JLabel label = new JLabel(item);
            frame.add(label);
        }

        frame.revalidate();
        frame.repaint();
    }

    /**
     * Adds a new label for a product to the left panel of the Checkout interface.
     * Each label is accompanied by a fixed-size remove button.
     *
     * @param text The text to be displayed on the label for the added product.
     */
    public void addNewItemToCheckoutDisplay(String text, long l, Product p) {
        // Create a panel for each item that includes a label and a fixed-size remove
        // button
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));

        Border line1 = new LineBorder(Color.WHITE);
        Border margin1 = new EmptyBorder(5, 15, 5, 15);
        Border compound1 = new CompoundBorder(line1, margin1);
        itemPanel.setBorder(compound1);
        
        JLabel label = new JLabel(text);
        JButton removeButton = new JButton("Remove");

        // Set a fixed size for the remove button
        removeButton.setPreferredSize(new Dimension(50, 50));
        
        // Set the maximum size for the itemPanel
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Removed the Item: " + text); // For Testing
                removeItem(itemPanel);
                
            }
        });
        
        removeButton.addActionListener(new RemoveItemButtonListener(logic,p));
        
        removeButton.setForeground(Color.WHITE);
        removeButton.setBackground(Color.BLACK);
        Border line = new LineBorder(Color.WHITE);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        removeButton.setBorder(compound);
        this.removeButtons.add(removeButton);
        itemPanel.add(label);
        itemPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add some space between label and button
        if (p != null) {
        	itemPanel.add(removeButton);
        }
        
        
        // Add the item panel to the left panel
        itemsDisplay.add(itemPanel);
        itemsDisplay.revalidate();
        itemsDisplay.repaint();
    }

    /**
     * Removes an item from the left panel and notifies listeners about the item
     * removal.
     * 
     * @param itemPanel The JPanel containing the item to be removed.
     */
    private void removeItem(JPanel itemPanel) {

        // Identify and remove the item from the GUI
        itemsDisplay.remove(itemPanel);
        itemsDisplay.revalidate();
        itemsDisplay.repaint();

    }
    
    /**
     *disables all remove buttons except the current button
      */
    
    public void disableRemoveButtons() {
    	int cnt = 0;
    	for (JButton b : this.removeButtons) {
    		if (cnt != this.removeButtons.size()) {
    			b.setEnabled(false);
    		}
    		cnt ++;
    	}
    }
    
    /**
     *enables all remove buttons except the current button
      */
    
    public void enableRemoveButtons() {
    	for (JButton b : this.removeButtons) {
    		b.setEnabled(true);
    	}
    }
    
    /**
     *shows reminder panel that is showed
     */
    public void showReminder(String panel) {
    	this.reminderLayout.show(this.reminderPanel, panel);
    	this.reminderPanel.setVisible(true);
    }
    
    /**
     * unshows reminder panel that is showed
     */
    public void unshowReminder() {
    	this.reminderPanel.setVisible(false);
    }
    
    public void dontBagItemPushed() {
    	//this.logic.getMassLogic().skipBaggingRequest();
    	this.logic.getViewController().requestApprovalDontBag();
    	this.discrepencyPanelAddItem.resetView();
    	this.discrepencyPanelAddItem.setLabel("An Attendant Has Been Notified to Approve Request to Not Bag Item");
    }

    // --------------------------------------------------------------------------
    // CHECKOUT PANEL GUI & ACTION
    // --------------------------------------------------------------------------

    public JPanel checkoutPanel() {
        // Create main panel with GridBagLayout
        JPanel checkoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Upper Left Panel (Total Display)
        JPanel totalDisplayPanel = createTotalDisplayPanel("Total", 500, 100);
        this.custTotalLabel = new JLabel("Total is: $0.00");
        this.custTotalLabel.setFont(new Font("Monospaced", Font.BOLD, 14)); // Adjust font and style
        totalDisplayPanel.add(custTotalLabel);

        this.memberLabel = new JLabel("Member No: ?"); // Assuming display_member is the variable for
                                                                      // member number
        this.memberLabel.setFont(new Font("Monospaced", Font.BOLD, 14)); // Adjust font and style
        totalDisplayPanel.add(memberLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        checkoutPanel.add(totalDisplayPanel, gbc);

        // Upper Right Panel (Weight Display)
        JPanel weightDisplayPanel = createWeightDisplayPanel("Weight Display", 500, 100);
        this.weightLabel = new JLabel("Weight: " + display_weight.inGrams().doubleValue()+" g");
        weightLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        weightDisplayPanel.add(weightLabel);
        gbc.gridx = 2;
        gbc.gridy = 0;
        checkoutPanel.add(weightDisplayPanel, gbc);
        
        
        //initializing add item reminder
        
        JPanel addItemReminderPanel = new JPanel();
        
        this.addItemReminder = new JLabel("<html><font color='red'>Please place your item in the bagging area</font></html>");
        addItemReminder.setFont(new Font("Monospaced", Font.BOLD, 15));
        
        this.addBagsReminder = new JLabel("<html><font color='red'>Please place the bags in the bagging area</font></html>");
        addBagsReminder.setFont(new Font("Monospaced", Font.BOLD, 15));
        
        this.removeItemReminder = new JLabel("<html><font color='red'>Please remove your item from the bagging area</font></html>");
        removeItemReminder.setFont(new Font("Monospaced", Font.BOLD, 15));
        //this.addItemReminder.setForeground(new Color(100,200,100));
        //this.addItemReminder.setHorizontalAlignment(JLabel.CENTER);
        
        this.reminderPanel.add(this.addItemReminder, "addItem");
        this.reminderPanel.add(this.addBagsReminder, "addBag");
        this.reminderPanel.add(this.removeItemReminder, "removeItem");
        addItemReminderPanel.add(this.reminderPanel);
        this.reminderPanel.setVisible(false);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        checkoutPanel.add(addItemReminderPanel,gbc);
        
        // Left Panel (Product List / Checkout / Receipt)
        this.itemsDisplay = createWeightandTotalPanel("", 500, 2500);
        this.itemsDisplay.setMaximumSize(new Dimension(500, 2500));
        this.itemsDisplay.setMinimumSize(new Dimension(500, 2500));
        this.itemsDisplay.setPreferredSize(new Dimension(500, 2500));
        this.itemsDisplay.setLayout(new BoxLayout(itemsDisplay, BoxLayout.Y_AXIS)); // Box Layout
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.20;
        gbc.fill = GridBagConstraints.BOTH;
        checkoutPanel.add(itemsDisplay, gbc);

        // Add "Scroll" to Left Panel / Product List
        JScrollPane leftScrollPane = new JScrollPane(itemsDisplay);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 2;
        gbc.weighty = 1.20;
        gbc.fill = GridBagConstraints.BOTH;
        checkoutPanel.add(leftScrollPane, gbc);
        // this.updateDisplayTotal(new BigDecimal(2));

        // Right Panel (TouchScreen Buttons)

        JPanel touchScreenButtonsPanel = this.visualCatalog.getVisualCatalog("Visual Catalogue", 500, 500);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.cardPanelRightDisplay.add(touchScreenButtonsPanel, "visualCatalog");

        JPanel visualCatalog = createButtonPanelRightPanel("Touch Screen", 500, 500);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.cardPanelRightDisplay.add(visualCatalog, "mainMenu");

        checkoutPanel.add(this.cardPanelRightDisplay, gbc);

        return checkoutPanel;
    }
    
    
    public void enableDontBagItemButton() {
    	this.DontBagItemButton.setEnabled(true);
    }
    
    public void disableDontBagItemButton() {
    	this.DontBagItemButton.setEnabled(false);
    }
    
    
    /*
     * overlays discrepency panel
     */
    public void displayDiscrepency() {
    	if (!this.hasAddedDiscrepencyPane) {
    		this.hasAddedDiscrepencyPane = true;
    		System.out.println("Attempting to add to pane right now");
        	layeredPane.add(this.discrepencyPanelAddItem, JLayeredPane.POPUP_LAYER);
        	layeredPane.revalidate();
        	layeredPane.repaint();
    		
    	}
    }
    
    /*
     *removes  overlays discrepency panel
     */
    public void removeDiscrepency() {
    	layeredPane.remove(this.discrepencyPanelAddItem);
    	layeredPane.revalidate();
    	layeredPane.repaint();
    	this.hasAddedDiscrepencyPane = false;
    	 //frame.remove(this.discrepencyPanelAddItem);
    }
    
    
    /*
     * overlays add own bags panel
     */
    public void displayAddOwnBags() {
		this.addingOwnBagsPanel.resetPanel();
    	layeredPane.add(this.addingOwnBagsPanel, JLayeredPane.POPUP_LAYER);
    	layeredPane.revalidate();
    	layeredPane.repaint();
    }
    
    /*
     *removes  overlays discrepency panel
     */
    public void removeAddOwnBags() {
    	layeredPane.remove(this.addingOwnBagsPanel);
    	layeredPane.revalidate();
    	layeredPane.repaint();
    	 //frame.remove(this.discrepencyPanelAddItem);
    }
    

    /*
     *triggered when a customer is done adding their own bags
     */
    public void doneAddingOwnBags() {
    	logic.getMassLogic().endAddBags();
    }
    
    /*
     *disables buttons on block
     */
    
    public void disableButtonsBlocked() {
    	try {    		
    		for(JButton j : this.disabledWhenBlocked) {
    			j.setEnabled(false);
    		}
    		this.visualCatalog.disableAddingButtons();
    		this.rightButtonPannel.revalidate();
    		this.rightButtonPannel.repaint();
    		this.disableRemoveButtons();
    	}
    	catch (Exception e) {}
    }
    
    /*
     * Enables buttons on block
     */
    public void enableButtonsBlocked() {
    	try {    		
    		for(JButton j : this.disabledWhenBlocked) {
    			j.setEnabled(true);
    		}
    		this.enableRemoveButtons();
    		this.visualCatalog.enableAddingButtons();
    		this.rightButtonPannel.revalidate();
    		this.rightButtonPannel.repaint();
    	}
    	catch (Exception e) {}
    }
    


    public void showPanel(String panel) {
        this.cardLayoutRightDisplay.show(this.cardPanelRightDisplay, panel);
        this.currentCardPanel = panel;
    }

    // Helper method to create the upper left panel
    private JPanel createTotalDisplayPanel(String label, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK), label);
        b.setTitleColor(Color.LIGHT_GRAY);
        
        panel.setBorder(b);
        
        // Set the preferred size to have a fixed width and height
        panel.setPreferredSize(new Dimension(width, height));
        // Set the maximum size to enforce the desired height
        panel.setMaximumSize(new Dimension(width, height));

        return panel;
    }

    // Helper method to create the upper right panel
    private JPanel createWeightDisplayPanel(String label, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK), label);
        b.setTitleColor(Color.LIGHT_GRAY);
        
        panel.setBorder(b);
        
        // Set the preferred size to have a fixed width and height
        panel.setPreferredSize(new Dimension(width, height));
        // Set the maximum size to enforce the desired height
        panel.setMaximumSize(new Dimension(width, height));

        return panel;
    }

    /*
     * For Upper Left (TOTAL) and Upper Right (WEIGHT) Panels
     */
    private JPanel createWeightandTotalPanel(String label, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(label));

        // Set the preferred size to have a fixed width and height
        panel.setPreferredSize(new Dimension(width, height));
        // Set the maximum size to enforce the desired height
        panel.setMaximumSize(new Dimension(width, height));

        return panel;
    }
    /*
     * FOR TOUCH SCREEN Button in Right Panel
     */

    
    private JPanel createButtonPanelRightPanel(String label, int width, int height) {
    	TitledBorder b = BorderFactory.createTitledBorder(label);
    	b.setTitleColor(Color.LIGHT_GRAY);
    	
        this.rightButtonPannel = new JPanel(new GridLayout(3, 3, 10, 10));
        this.rightButtonPannel.setBorder(b);
        this.rightButtonPannel.setMaximumSize(new Dimension(width - 5, height));
        
        // Display the Buttons on the GUI Panel
        this.rightButtonPannel.add(TypeInMemberNoButton);
        this.rightButtonPannel.add(DontBagItemButton);
        this.rightButtonPannel.add(CallAttendantButton);
        this.rightButtonPannel.add(AddOwnBagsButton);
        this.rightButtonPannel.add(PurchaseBagsButton);
        this.rightButtonPannel.add(button6);
        this.rightButtonPannel.add(AddItem_PLUButton);
        this.rightButtonPannel.add(AddItem_VisualButton);
        this.rightButtonPannel.add(PayForOrderButton);

        
        //add buttons that we want disabled when the station is blocked
        this.disabledWhenBlocked.add(TypeInMemberNoButton);
        this.disabledWhenBlocked.add(AddOwnBagsButton);
        this.disabledWhenBlocked.add(PurchaseBagsButton);
        this.disabledWhenBlocked.add(AddItem_PLUButton);
        this.disabledWhenBlocked.add(AddItem_VisualButton);
        this.disabledWhenBlocked.add(PayForOrderButton);
        
        
        // Set a smaller font size for each button
        Font smallFont = new Font(TypeInMemberNoButton.getFont().getName(), Font.BOLD, 10); // Adjust the font size as
                                                                                            // needed
        TypeInMemberNoButton.setFont(smallFont);
        DontBagItemButton.setFont(smallFont);
        CallAttendantButton.setFont(smallFont);
        AddOwnBagsButton.setFont(smallFont);
        PurchaseBagsButton.setFont(smallFont);
        button6.setFont(smallFont);
        AddItem_PLUButton.setFont(smallFont);
        AddItem_VisualButton.setFont(smallFont);
        PayForOrderButton.setFont(smallFont);

        // Set maximum size for each button to control the width
        Dimension buttonMaxSize = new Dimension(width / 5, height / 5);
        TypeInMemberNoButton.setMaximumSize(buttonMaxSize);
        DontBagItemButton.setMaximumSize(buttonMaxSize);
        CallAttendantButton.setMaximumSize(buttonMaxSize);
        AddOwnBagsButton.setMaximumSize(buttonMaxSize);
        PurchaseBagsButton.setMaximumSize(buttonMaxSize);
        button6.setMaximumSize(buttonMaxSize);
        AddItem_PLUButton.setMaximumSize(buttonMaxSize);
        AddItem_VisualButton.setMaximumSize(buttonMaxSize);
        PayForOrderButton.setMaximumSize(buttonMaxSize);
        
        DontBagItemButton.setEnabled(false);
        return this.rightButtonPannel;
    }



}