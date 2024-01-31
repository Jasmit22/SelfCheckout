package com.thelocalmarketplace.software.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Utilities;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Programmatic interface that handles operations regarding products
 */
public class ProductLogic extends AbstractLogicDependant {
	
	/**
	 * Mapping of added products (that are priced per kg) to their recorded mass
	 */
	private Map<Product, Mass> recordedMasses;
	
	/**
	 * Tracks all of the products that are approved to be excluded from bagging area
	 */
	private List<Product> bulkyProducts;
	 
	/**
	 * The current product pending to be added
	 * Will be null if no product is being added
	 */
	private Product pendingProduct;
	
	/**
	 * Tracks all of the products that are in the customer's cart
	 * Includes products without barcodes
	 * Maps a product to its count
	 */
	private LinkedHashMap<Product, Integer> cart;
	
	/**
	 * Tracks how much money the customer owes
	 */
	private BigDecimal balanceOwed;
	
	
	/**
	 * Base Constructor
	 */
	public ProductLogic(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
		
		// Initialization
		this.cart = new LinkedHashMap<Product, Integer>();
		this.recordedMasses = new HashMap<Product, Mass>();
		this.bulkyProducts = new ArrayList<>();
		
		this.balanceOwed = BigDecimal.ZERO;
		this.recordedMasses = new HashMap<>();
	}

	/**
	 * Puts the station in a state ready to detect item mass
	 * @param product Is the product to read the mass of
	 */
	public void startAddItem(Product product) {
		if (!logic.isSessionActive()) {
			throw new InvalidStateSimulationException("Session not in progress");
		}
		 
		this.logic.getStateLogic().gotoState(States.ADDITEM);
		this.pendingProduct = product;
	  
		System.out.println("Please place item on the scale");
	}
    
	/**
	 * Ends the ADDITEM state so the station goes back to normal operation
	 */
	public void endAddItem() {
		if (!logic.isSessionActive()) {
			throw new InvalidStateSimulationException("Session not in progress");
		}
		else if (!this.logic.getStateLogic().inState(States.ADDITEM)) {
			throw new InvalidStateSimulationException("Not even adding an item");
		}
	    if (logic.getViewController().isReady()) {
	    	logic.getViewController().updateOnPluCodedProductPutOnScale();
	    }
	    
		this.logic.getStateLogic().gotoState(States.NORMAL);
	}
    
	 /**
	  * Triggered by weight discrepancy controller when something is placed on scale during ADDITEM state
	  * @param itemMass Is the mass of the item read
	  */
	public void onItemMassUpdated(Mass itemMass) {
		if (!this.logic.getStateLogic().inState(States.ADDITEM)) {
    		
			// Should not happen since this method should never be called directly
			throw new InvalidStateSimulationException("Not ready to add item");
		}
		else if (this.pendingProduct == null) {
			throw new InvalidStateSimulationException("No product is pending to be added");
		}
    	
		final BigDecimal costToAdd = this.calculateCostOfProduct(pendingProduct, itemMass);

		this.logic.getMassLogic().updateExpectedMass(this.logic.getMassLogic().getExpectedMass().sum(itemMass));
		this.recordedMasses.put(this.pendingProduct, itemMass);
		this.modifyBalance(costToAdd);
		 
		Utilities.modifyCountMapping(this.getCart(), this.pendingProduct, 1);
    	
		this.pendingProduct = null;
    	
		this.endAddItem();
	}
    
	/**
	 * Helper method for calculating total cost of a product that is priced per kilogram
	 * If product is per unit, no further calculation is required
	 * @param product The product
	 * @param mass The mass of the product
	 * @return The cost of the product
	 */
	public BigDecimal calculateCostOfProduct(Product product, Mass mass) {
		if (product.isPerUnit()) {
			return new BigDecimal(product.getPrice());
	 	}
    	
		BigDecimal kg = mass.inGrams().divide(new BigDecimal(1000));
    	
		return new BigDecimal(product.getPrice()).multiply(kg);
	}
    
	public void removeProduct(Product product) {
		if (!product.equals(this.pendingProduct)) {
			if (!this.getCart().containsKey(product)) {
				throw new InvalidStateSimulationException("Product not in cart");
			}
			
			final Mass productMass = this.recordedMasses.get(product);
			final BigDecimal costToDeduct = this.calculateCostOfProduct(product, productMass);
			final boolean isApproved = this.bulkyProducts.contains(product);
			
			this.modifyBalance(costToDeduct.negate());
			
			// Don't remove expected mass of product if it is approved to be excluded from bagging area
			if (!isApproved) {
				this.logic.getMassLogic().updateExpectedMass(this.logic.getMassLogic().getExpectedMass().difference(productMass).abs());    		
			}
			
			Utilities.modifyCountMapping(this.getCart(), product, -1);
			
			if (this.recordedMasses.containsKey(product)) {			 
				this.recordedMasses.remove(product);
			}
			
			if (this.logic.getStateLogic().inState(States.BLOCKED) || isApproved) {
				System.out.println("Product removed from cart");
				
				this.logic.getMassLogic().handleWeightDiscrepancy();
			}
			else {
				this.logic.getStateLogic().gotoState(States.BLOCKED);
				
				System.out.println("Product removed from cart. Please remove the item from the bagging area");
			}
		}
		else if (this.logic.getStateLogic().inState(States.ADDITEM)) {
			this.logic.getStateLogic().gotoState(States.NORMAL);
		}
	 }
	 
	/**
	 * Takes a barcode, looks it up in product database, then adds it to customer cart
	 * @param barcode The barcode to use
	 * @throws SimulationException If barcode is not registered to product database
	 * @throws SimulationException If barcode is not registered in available inventory
	 */
	public void addBarcodedProductToCart(Barcode barcode) throws SimulationException {
		System.out.println("addingitem");
		
		BarcodedProduct toadd = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		
		if (!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)) {
			throw new InvalidStateSimulationException("Barcode not registered to product database");
		}
		else if (!ProductDatabases.INVENTORY.containsKey(toadd) || ProductDatabases.INVENTORY.get(toadd) < 1) {
			throw new InvalidStateSimulationException("No items of this type are in inventory");
		}
		
		this.recordedMasses.put(toadd, new Mass(toadd.getExpectedWeight()));
		this.logic.getStateLogic().gotoState(States.BLOCKED);
		
		Utilities.modifyCountMapping(this.getCart(), toadd, 1);
		
		this.modifyBalance(new BigDecimal(toadd.getPrice()));
		this.logic.getMassLogic().addExpectedMassOfBarcodedProduct(barcode);

		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateOnProductAddedToCart(toadd.getDescription(), toadd.getPrice(), toadd);
		}
	}
	 
	/**
	 * Gets a PLU Code of an item, searches the product database, and starts the procedure to add to customer's cart
	 * @param pluCode The PLU Code to use
	 * @throws SimulationException If PLU Code doesn't exist in the product database
	 * @throws SimulationException If PLU Code is not in stock
	 */
	public void startAddPLUProductToCart(PriceLookUpCode pluCode) throws SimulationException {
		PLUCodedProduct pluCodedProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);
	
		if (!ProductDatabases.PLU_PRODUCT_DATABASE.containsKey(pluCode) ) {
			throw new InvalidStateSimulationException("PLU Code is not in the Product Database");
		} 
		else if (!ProductDatabases.INVENTORY.containsKey(pluCodedProduct) || ProductDatabases.INVENTORY.get(pluCodedProduct) <= 0)  {
			throw new InvalidStateSimulationException("No items of this type are in the inventory");
		}
	
		this.startAddItem(pluCodedProduct);
		
		PLUCodedProduct toadd = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);
		
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateOnProductAddedToCart(toadd.getDescription(), toadd.getPrice(), toadd);
		}
	}
	
	/**
	 * Used for resetting
	 */
	public void reset() {
		this.cart.clear();
		this.bulkyProducts.clear();
		this.pendingProduct = null;
		
		this.updateBalance(BigDecimal.ZERO);
	}
	
	/**
	 * Getter for pending product used only to test
	 */
	public Product getPendingProduct() {
		return this.pendingProduct;
	}
	
	/**
	 * Setter for pending product used only to test
	 */
	public void setPendingProduct(Product p) {
		this.pendingProduct = p;
	}
	
	/**
	 * Gets mass of a product according to recordedMasses used for testing
	 */
	public Mass getMassProduct(Product p) {
		if (this.recordedMasses.containsKey(p)) {
			return this.recordedMasses.get(p);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gets the cost of specific product that the customer has added
	 * @return The cost
	 */
	public BigDecimal getCostOfProduct(Product product) {
		return this.calculateCostOfProduct(product, this.recordedMasses.get(product));
	}
	
	/**
	 * Gets the customer's cart
	 * @return A list of products that represent the cart
	 */
	public Map<Product, Integer> getCart() {
		return this.cart;
	}
	
	/**
	 * Gets the balance owed by the customer
	 * @return The balance owed
	 */
	public BigDecimal getBalanceOwed() {
		return this.balanceOwed;
	}
	
	/**
	 * Increments/Decrements the customer's balance
   	 * @param amount Is the amount to increment/decrement by
   	 */
	public void modifyBalance(BigDecimal amount) {
		this.balanceOwed = this.balanceOwed.add(amount);

		if (this.balanceOwed.compareTo(BigDecimal.ZERO) < 0) {
			this.balanceOwed = BigDecimal.ZERO;
    	}
		
		System.out.println("balance changed: " + this.balanceOwed);
		
		this.logic.getViewController().updatePriceOnCustomerView();
  	}
	
	/**
	 * Sets the customer's balance
	 * @param balance The new balance value
	 */
	public void updateBalance(BigDecimal balance) {
		this.balanceOwed = balance;
		
		this.logic.getViewController().updatePriceOnCustomerView();
	}
	
	/**
     * Adds a product to the cart, handling both barcoded and PLU-coded products.
     * The method checks the type of the product and adds it to the cart using the
     * appropriate method for its type.
     *
     * @param uniqueProducts The product to add to the cart. It must be either an
     *                       instance of BarcodedProduct or PLUCodedProduct.
     */
	public void productSelectedToAdd(Product product) {
		 if (product instanceof BarcodedProduct) {
			 BarcodedProduct bp = (BarcodedProduct) product;
			 
			 this.addBarcodedProductToCart(bp.getBarcode());
		 }
		 else if (product instanceof PLUCodedProduct) {
			 PLUCodedProduct plup = (PLUCodedProduct) product;
			 
			 this.startAddPLUProductToCart(plup.getPLUCode());
		 }
		 else {
			 this.startAddItem(product);
		 }
	}

	/**
	 * Triggers the approval of the last product added to not be in bagging area
	 */
	public void grantApprovalSkipBaggingOfLastProductAdded() {
		Product lastProduct = null;
		int count = 1;
		
		for (Entry<Product, Integer> it : this.cart.entrySet()) {
			if (count == this.cart.size()) {
				lastProduct = it.getKey();
			}
			
			count++;
		}
		
		this.bulkyProducts.add(lastProduct);
		
		this.logic.getMassLogic().overrideDiscrepancy();
		this.logic.getMassLogic().handleWeightDiscrepancy();
		
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().removeDiscrepencyCustomerView();
		}
	}
}
