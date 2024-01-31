package com.thelocalmarketplace.software.gui.view;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.card.BlockedCardException;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.InvalidPINException;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.IViewable;
import com.thelocalmarketplace.software.gui.view.listeners.ITypeSelectCallback;
import com.thelocalmarketplace.software.gui.view.partials.MultilineButton;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;
import com.thelocalmarketplace.software.gui.view.partials.simulation.SimulationSelectPanel;


/**
 * This is a view class for the sole purpose of simulating possible customer action
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

public class CustomerSimulationView extends AbstractLogicDependant implements IViewable {
	
	private Map<Card, String> customerBankCards;
	private Map<Card, String> customerMembershipCards;
	
	private Map<Item, String> customerItems;
	
	private Map<ReusableBag, String> customerBags;
	
	private Map<BigDecimal, String> availableCoinDenominations;
	private Map<BigDecimal, String> availableBanknoteDenominations;
	
	/**
	 * Tracks which items the customer placed on the scale (for the purposes of updating the GUI)
	 */
	private Map<Item, String> itemsOnScale;	
	private boolean isConfigured;
	
	private JFrame frame;
	
	private CardLayout cardLayout;
	private JPanel cardPanel;
    public String currentCardPanel; // for Testing
	
	public MultilineButton switchToPlaceItemOnScaleViewButton;
	public MultilineButton switchToRemoveItemFromScaleViewButton;
	public MultilineButton switchToScanBarcodedItemMainScannerViewButton;
	public MultilineButton switchToScanBarcodedItemHandheldScannerViewButton;
	public MultilineButton switchToPlaceBagOnScaleViewButton;
	public MultilineButton switchToSwipeBankCardViewButton;
	public MultilineButton switchToTapBankCardViewButton;
	public MultilineButton switchToInsertBankCardViewButton;
	public MultilineButton switchToSwipeMembershipCardViewButton;
	public MultilineButton switchToSelectCoinViewButton;
	public MultilineButton switchToSelectBanknoteViewButton;
	
	public SimulationSelectPanel<Item> selectItemToPlaceOnScalePanel;
	public SimulationSelectPanel<Item> selectItemToRemoveFromScalePanel;
	
	public SimulationSelectPanel<BarcodedItem> selectItemToScanViaMainScannerPanel;
	public SimulationSelectPanel<BarcodedItem> selectItemToScanViaHandheldScannerPanel;
	
	public SimulationSelectPanel<ReusableBag> selectBagToAddPanel;
	
	public SimulationSelectPanel<Card> selectCardToSwipePanel;
	public SimulationSelectPanel<Card> selectCardToTapPanel;
	public SimulationSelectPanel<Card> selectCardToInsertPanel;
	
	public SimulationSelectPanel<Card> selectMembershipCardToSwipePanel;
	
	public SimulationSelectPanel<BigDecimal> selectCoinDenominationToInsertPanel;
	public SimulationSelectPanel<BigDecimal> selectBanknoteDenominationToInsertPanel;
		
	public ActionListener switchBackToMainViewListener;
	
    
    public CustomerSimulationView(ISelfCheckoutStationSoftware logic) throws NullPointerException {
        super(logic);
        
        this.isConfigured = false;

        this.customerBankCards = new HashMap<>();
        this.customerMembershipCards = new HashMap<>();
        this.customerItems = new HashMap<>();
        this.customerBags = new HashMap<>();
        this.itemsOnScale = new HashMap<>();
        this.availableCoinDenominations = new HashMap<>();
        this.availableBanknoteDenominations = new HashMap<>();
        
        this.cardLayout = new CardLayout();
		this.cardPanel = new JPanel(this.cardLayout);
		
		this.switchToPlaceItemOnScaleViewButton = new MultilineButton(new String[] {"Select Item", "(To Place on Scale)"});
		this.switchToRemoveItemFromScaleViewButton = new MultilineButton(new String[] {"Select Item", "(To Remove from Scale)"});
		this.switchToScanBarcodedItemMainScannerViewButton = new MultilineButton(new String[] {"Scan Item", "(Main Scanner)"});
		this.switchToScanBarcodedItemHandheldScannerViewButton = new MultilineButton(new String[] {"Scan Item", "(Handheld Scanner)"});
		this.switchToPlaceBagOnScaleViewButton = new MultilineButton(new String[] {"Select Bag", "(To Place on Scale)"});		this.switchToSwipeBankCardViewButton = new MultilineButton(new String[] {"Read Bank Card", "(Swipe)"});
		this.switchToTapBankCardViewButton = new MultilineButton(new String[] {"Read Bank Card", "(Tap)"});
		this.switchToInsertBankCardViewButton = new MultilineButton(new String[] {"Read Bank Card", "(Insert)"});
		this.switchToSwipeMembershipCardViewButton = new MultilineButton(new String[] {"Read Membership Card", "(Swipe)"});
		this.switchToSelectCoinViewButton = new MultilineButton(new String[] {"Select Coin", "(To Insert)"});
		this.switchToSelectBanknoteViewButton = new MultilineButton(new String[] {"Select Banknote", "(To Insert)"});
		
		
		this.frame = new JFrame("Customer Simulation Controls");
        this.hide();
		
		// Initialize button listeners
    	this.switchBackToMainViewListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("default"); }
		};
        
    	this.switchToPlaceItemOnScaleViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("additemtoscale"); }
		});
    	
    	this.switchToRemoveItemFromScaleViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("removeitemfromscale"); }
		});
    	
    	this.switchToScanBarcodedItemHandheldScannerViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("scanitemhandheld"); }
		});
    	
    	this.switchToScanBarcodedItemMainScannerViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("scanitemmain"); }
		});
    	
    	this.switchToPlaceBagOnScaleViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("addbagtoscale"); }
		});
    	
    	this.switchToSwipeBankCardViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("swipebankcard"); }
		});
    	
    	this.switchToTapBankCardViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("tapbankcard"); }
		});
    	
    	this.switchToInsertBankCardViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("insertbankcard"); }
		});
    	
    	this.switchToSwipeMembershipCardViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("swipemembershipcard"); }
		});
    	
    	this.switchToSelectCoinViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("selectcoin"); }
		});
    	
    	this.switchToSelectBanknoteViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { showPanel("selectbanknote"); }
		});
    }
    
    /**
     * Used for configuring the simulation view for the purposes of demonstration
     * Each parameter is a name indexed by what it describes
     */
    public void configureCustomerSimulation(Currency currency, Map<Card, String> bankCards, Map<Card, String> membershipCards, Map<Item, String> items, Map<ReusableBag, String> bags, Map<BigDecimal, String> coinDenominations, Map<BigDecimal, String> banknoteDenominations) {
    	this.customerBankCards.putAll(bankCards);
    	this.customerMembershipCards.putAll(membershipCards);
    	this.customerItems.putAll(items);
    	this.customerBags.putAll(bags);
    	this.availableCoinDenominations.putAll(coinDenominations);
    	this.availableBanknoteDenominations.putAll(banknoteDenominations);
    	
    	// Initialize panels
    	this.selectItemToPlaceOnScalePanel = new SimulationSelectPanel<>(this.customerItems, this.customerItems, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Item selected) {
				logic.getHardware().getBaggingArea().addAnItem(selected);
				
				Utilities.swapMapEntry(selected, customerItems, itemsOnScale);
				
				selectItemToPlaceOnScalePanel.configureAllowableOptions(customerItems);
				selectItemToRemoveFromScalePanel.configureAllowableOptions(itemsOnScale);
			}
		});
    	
    	this.selectItemToPlaceOnScalePanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectItemToRemoveFromScalePanel = new SimulationSelectPanel<>(this.customerItems, new HashMap<>(), new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Item selected) {
				logic.getHardware().getBaggingArea().removeAnItem(selected);
				
				Utilities.swapMapEntry(selected, itemsOnScale, customerItems);
				
				selectItemToPlaceOnScalePanel.configureAllowableOptions(customerItems);
				selectItemToRemoveFromScalePanel.configureAllowableOptions(itemsOnScale);
			}
		});
    	
    	this.selectItemToRemoveFromScalePanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectItemToScanViaHandheldScannerPanel = new SimulationSelectPanel<>(this.getBarcodedItems(this.customerItems), this.getBarcodedItems(customerItems), new ITypeSelectCallback<>() {

			@Override
			public void onSelection(BarcodedItem selected) {
				try {
					logic.getHardware().getHandheldScanner().scan(selected);
				}catch (Exception e) {
					logic.getViewController().getCustomerView().notify(e.toString());
				}
			}
		});
    	
    	this.selectItemToScanViaHandheldScannerPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectItemToScanViaMainScannerPanel = new SimulationSelectPanel<>(this.getBarcodedItems(this.customerItems), this.getBarcodedItems(customerItems), new ITypeSelectCallback<>() {

			@Override
			public void onSelection(BarcodedItem selected) {
				try {
				logic.getHardware().getMainScanner().scan(selected);
				}catch (Exception e) {
					
					String[] message = e.toString().split(": ");
					System.out.println(message);
					logic.getViewController().getCustomerView().notify(message[1]);
				}
			}
		});
    	
    	this.selectItemToScanViaMainScannerPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);

    	this.selectBagToAddPanel = new SimulationSelectPanel<>(this.customerBags, this.customerBags, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(ReusableBag selected) {
				logic.getHardware().getBaggingArea().addAnItem(selected);
				
				customerBags.remove(selected);
				
				selectBagToAddPanel.configureAllowableOptions(customerBags);
				
			}
		});
    	
    	this.selectBagToAddPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	
    	
    	this.selectCardToSwipePanel = new SimulationSelectPanel<>(this.customerBankCards, this.customerBankCards, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Card selected) {
				try {
					logic.getHardware().getCardReader().swipe(selected);
				} catch (IOException e) {
					System.out.println("Failed to swipe card: " + customerBankCards.get(selected));
				}
			}
		});
    	
    	
    	this.selectCardToSwipePanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectCardToTapPanel = new SimulationSelectPanel<>(this.customerBankCards, this.customerBankCards, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Card selected) {
				try {
					logic.getHardware().getCardReader().tap(selected);
				} catch (IOException e) {
					System.out.println("Failed to tap card");
				}
			}
		});
    	
    	this.selectCardToTapPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectCardToInsertPanel = new SimulationSelectPanel<>(this.customerBankCards, this.customerBankCards, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Card selected) {
				try {
					logic.getHardware().getCardReader().insert(selected, logic.getCardPaymentLogic().getEnteredPIN());
				} 
				catch (InvalidPINException e) {
					System.out.println("Invalid PIN");
				}
				catch (BlockedCardException e) {
					System.out.println("Card is blocked");
				}
				catch (Exception e) {
					System.out.println("Failed to read inserted card"+e.toString());
				}
				//Remove the card after inserting it as it is no longer needed.
				logic.getHardware().getCardReader().remove();
			}
		});
    	
    	this.selectCardToInsertPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectCoinDenominationToInsertPanel = new SimulationSelectPanel<>(this.availableCoinDenominations, this.availableCoinDenominations, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(BigDecimal value) {
				Coin coin = new Coin(currency, value);
				
				try {
					logic.getHardware().getCoinSlot().receive(coin);
				} catch (DisabledException | CashOverloadException e) {
					System.out.println("Failed to insert coin");
				}
			}
		});
    	
    	this.selectCoinDenominationToInsertPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectMembershipCardToSwipePanel = new SimulationSelectPanel<>(this.customerMembershipCards, this.customerMembershipCards, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(Card selected) {
				try {
					logic.getHardware().getCardReader().swipe(selected);
				} catch (IOException e) {
					System.out.println("Failed to swipe membership card: " + customerMembershipCards.get(selected));
				}
			}
		});
    	
    	this.selectMembershipCardToSwipePanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	this.selectBanknoteDenominationToInsertPanel = new SimulationSelectPanel<>(this.availableBanknoteDenominations, this.availableBanknoteDenominations, new ITypeSelectCallback<>() {

			@Override
			public void onSelection(BigDecimal value) {
				Banknote banknote = new Banknote(currency, value);
				
				try {
					logic.getHardware().getBanknoteInput().receive(banknote);
				} catch (DisabledException | CashOverloadException e) {
					System.out.println("Failed to insert banknote");
				}
			}
		});
    	
    	this.selectBanknoteDenominationToInsertPanel.switchBackToMainButton.addActionListener(this.switchBackToMainViewListener);
    	
    	
    	this.isConfigured = true;
    }

    @Override
	public void start() {
    	
	    // Create and register window frame
	    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.frame.setLocation(0, 610);
	    this.frame.setSize(1100, 300);
	    
	    Container pane = this.frame.getContentPane();
	    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	    
	    JPanel body = new JPanel();
	    Utilities.setComponentColour(body);
	    
	    this.cardPanel.add(this.createDefaultPanel(), "default");
	    
	    if (this.isConfigured) {	    	
	    	this.cardPanel.add(this.selectItemToPlaceOnScalePanel, "additemtoscale");
	    	this.cardPanel.add(this.selectItemToRemoveFromScalePanel, "removeitemfromscale");
	    	this.cardPanel.add(this.selectItemToScanViaHandheldScannerPanel, "scanitemhandheld");
	    	this.cardPanel.add(this.selectItemToScanViaMainScannerPanel, "scanitemmain");
	    	this.cardPanel.add(this.selectCardToSwipePanel, "swipebankcard");
	    	this.cardPanel.add(this.selectCardToTapPanel, "tapbankcard");
	    	this.cardPanel.add(this.selectCardToInsertPanel, "insertbankcard");
	    	this.cardPanel.add(this.selectMembershipCardToSwipePanel, "swipemembershipcard");
	    	this.cardPanel.add(this.selectBagToAddPanel, "addbagtoscale");
	    	this.cardPanel.add(this.selectCoinDenominationToInsertPanel, "selectcoin");
	    	this.cardPanel.add(this.selectBanknoteDenominationToInsertPanel, "selectbanknote");
	    }
	    
	    this.showPanel("default");

	    body.add(this.cardPanel);
	    
	    pane.add(new TopTitlePanel("Customer Simulation Control Panel (" + this.logic.getName() + ")"));
	    pane.add(body);
	    
	    this.frame.revalidate();
	    this.frame.repaint();
	}
    
    private JPanel createDefaultPanel() {
    	JPanel p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	
    	JPanel row1 = new JPanel();
    	JPanel row2 = new JPanel();
    	
    	row1.add(this.switchToPlaceItemOnScaleViewButton);
    	row1.add(this.switchToRemoveItemFromScaleViewButton);
    	row1.add(this.switchToScanBarcodedItemMainScannerViewButton);
    	row1.add(this.switchToScanBarcodedItemHandheldScannerViewButton);
    	row1.add(this.switchToPlaceBagOnScaleViewButton);
    	row2.add(this.switchToSwipeBankCardViewButton);
    	row2.add(this.switchToTapBankCardViewButton);
    	row2.add(this.switchToInsertBankCardViewButton);
    	row2.add(this.switchToSwipeMembershipCardViewButton);
    	row2.add(this.switchToSelectCoinViewButton);
    	row2.add(this.switchToSelectBanknoteViewButton);
    	
    	p.add(row1);
    	p.add(row2);
    	
    	Utilities.setComponentColour(p);
    	
    	return p;
    }
    
    private Map<BarcodedItem, String> getBarcodedItems(Map<Item, String> map) {
    	HashMap<BarcodedItem, String> m  = new HashMap<>();
    	
    	for (Entry<Item, String> e : map.entrySet()) {
    		if (e.getKey() instanceof BarcodedItem)  {
    			m.put((BarcodedItem) e.getKey(), e.getValue());
    		}
    	}
    	
    	return m;
    }
	
    @Override
    public void show() {
    	this.frame.setVisible(true);
    }
    
    @Override
    public void hide() {
    	this.frame.setVisible(false);
    }
    
    private void showPanel(String panel) {
    	this.cardLayout.show(cardPanel, panel);
    	this.currentCardPanel = panel;
    }
    
    public String getCurrentCardLayoutView() {
        return currentCardPanel;
    }
    
}