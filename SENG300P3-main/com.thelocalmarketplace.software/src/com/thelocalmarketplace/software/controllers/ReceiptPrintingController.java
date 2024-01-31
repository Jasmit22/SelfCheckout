package com.thelocalmarketplace.software.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Enumerations.Status;

/**
 * Controller for Receipt Printing
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
public class ReceiptPrintingController extends AbstractLogicDependant implements ReceiptPrinterListener {
	
	private static final int FULL_PAPER = 100;
	private static final int PAPER_BUFFER = 15;
	
	private static final int FULL_INK = 1 << 20;
	private static final int INK_BUFFER = 200;
	
	/**
	 * A recording of the receipt if a print fails
	 */
	private String duplicateReceipt;
	private String memberNumber;
	
	/**
	 * The estimated total capacity of ink remaining in the printer
	 */
	private double unitsOfInkLeft;
	
	/**
	 * The estimated total capacity of paper remaining in the printer
	 */
	private double unitsOfPaperLeft;
	
	/**
	 * Counts the lines printed on paper, used to calculate total amount of paper utilized
	 */
	private double totalLines;
	
	
	/**
	 * Base constructor
	 */
    public ReceiptPrintingController(SelfCheckoutStationSoftware logic) throws NullPointerException {
    	super(logic);
    	
    	// Initialization
    	this.duplicateReceipt = "";
    	this.memberNumber = "NONE";
    	
    	this.unitsOfInkLeft = 0;
    	
    	this.unitsOfPaperLeft = 0;
    	this.totalLines = 4;
    	
    	this.logic.getHardware().getPrinter().register(this);
    }
    
    /**
     * Generates a string that represents a receipt to be printed
     * @return The receipt as a string.
     */
    public String createPaymentRecord(BigDecimal change) {
        StringBuilder paymentRecord = new StringBuilder();
        Map<Product, Integer> cartItems = this.logic.getProductLogic().getCart();
        BigDecimal total = BigDecimal.ZERO;
        
        //Begin the receipt.
        paymentRecord.append("Customer Receipt\n");
        // If Customer entered a Valid Member Number
        if (!memberNumber.isEmpty() || !memberNumber.isBlank()) {
        	paymentRecord.append("Member Number: ").append(memberNumber);
        }
        
        paymentRecord.append("\n=========================\n");
        
        int i = 0;
        
        // Iterate through each item in the cart, adding printing them on the receipt.
        for (Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            BigDecimal price = this.logic.getProductLogic().getCostOfProduct(product);
            price.setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal totalItemCost = price.multiply(new BigDecimal(quantity));
            
            total = total.add(totalItemCost).setScale(2, RoundingMode.HALF_EVEN);
            
            paymentRecord.append("Item " + ++i + ":\n");
            paymentRecord.append(" - Qty: ");
            paymentRecord.append(quantity);
            paymentRecord.append(", Price: $");
            paymentRecord.append(price);
            paymentRecord.append(", Total: $");
            paymentRecord.append(totalItemCost.setScale(2, RoundingMode.HALF_EVEN));
            paymentRecord.append("\n");
            
            this.totalLines++;
        }

        total.setScale(2, RoundingMode.HALF_EVEN);
        change.setScale(2, RoundingMode.HALF_EVEN);
        
        paymentRecord.append("=========================\n");
        paymentRecord.append("Total Cost: $").append(total.toString()).append("\n");
        paymentRecord.append("Change Given: $").append(change.toString()).append("\n");
       
        System.out.print(paymentRecord);
                
        return paymentRecord.toString();
    }
    
    /**
     * Generates receipt and calls receipt printing hardware to print it.
     * @param change
     */
    public void handlePrintReceipt(BigDecimal change) {
        String receiptText = createPaymentRecord(change);
        
        try {        	
        	this.printReceipt(receiptText);
        	
        	// Calculating the new total Capacity 
        	this.calculatePaperCapacity(unitsOfPaperLeft, totalLines);
        	
        	// Resetting the lines per receipt counter to track paper used
        	this.resetTotalLines();
        	
        	// Checking the paper capacity
        	this.thePrinterHasLowPaper();
        	
        	// End session
            this.logic.stopSession();
        }
        catch (Exception e) {
        	this.duplicateReceipt = receiptText;
        	
        	this.onPrintingFail();
        }
    }

    /**
     * Helper method for printing receipt
     * @param receiptText Is the string to print
     * @throws OverloadedDevice 
     * @throws EmptyDevice 
     */
	private void printReceipt(String receiptText) throws EmptyDevice, OverloadedDevice {
		for (char c : receiptText.toCharArray()) {
	    	this.logic.getHardware().getPrinter().print(c);
	    	this.unitsOfInkLeft--;
	    }
	    
	    this.logic.getHardware().getPrinter().cutPaper(); 
	    
	    // Display receipt in view
	    if (this.logic.getViewController().isReady()) {	    	
	    	this.logic.getViewController().getCustomerView().notify(receiptText);
	    }
	}
	
	/**
	 * Prints out a duplicate receipt. Only meant to be used by the attendant.
	 * Returns the machine to normal as there is no longer a receipt that hasn't been printed.
	 */
	public void printDuplicateReceipt() {
		try {
			// Try to print out the receipt once more.
			this.printReceipt(duplicateReceipt);
			
			// Returns the machine to normal as the receipt is printed and the machine can resume.
			this.logic.getStateLogic().gotoState(States.NORMAL);
			
			// Removes the receipts as it is no longer needed.
			this.duplicateReceipt = "";
		} catch (Exception e) {
			
			// If the receipt fails to print again, this will be called.
			this.onPrintingFail();
		}
	}
	
	/**
	 * Executes if a receipt fails to print. Prints message to console and sets the state control software to suspended.
	 * (Good place to trigger UI listeners)
	 */
	private void onPrintingFail() {
		if (this.logic.getViewController().isReady()) {			
			this.logic.getViewController().getCustomerView().notify("Failed to print receipt");
			this.logic.getViewController().updateOnReceiptPrintingFail();
		}

		// Suspend station
		this.logic.setEnabled(false);
	}
	
	/**
	 * Sets the member number in Receipt Printing Controller when Customer enters valid membership
	 * @param memberNumber The membership number
	 */
	public void setMembershipNumberOnReceipt(String memberNumber) {
		this.memberNumber = memberNumber;
	}
	
	/**
	 * Predicts state of the paper level and updates the view accordingly
	 * Paper can be empty, low, stable, or full
	 * 
	 * @return the predicted status of the paper
	 */
	public Status predictPaper() {
		if (this.unitsOfPaperLeft == 0) {
			this.logic.getViewController().updateOnPaperEmpty();
			
			return Status.EMPTY;
		}
		else if (this.unitsOfPaperLeft <= PAPER_BUFFER) {
			this.logic.getViewController().updateOnPaperLow();
			
			return Status.FULL;
		}
		else if (this.unitsOfPaperLeft < FULL_PAPER) {
			this.logic.getViewController().updateOnPaperStable();
			
			return Status.LOW;
		}

		this.logic.getViewController().updateOnPaperFull();
		
		return Status.FULL;
	}
	
	/**
	 * Predicts state of the ink level and updates view accordingly
	 * Ink can be empty, low, stable, or full
	 * 
	 * @return the predicted status of the ink
	 */
	public Status predictInk() {
		if (this.unitsOfInkLeft == 0) {
			this.logic.getViewController().updateOnInkEmpty();
			
			return Status.EMPTY;
		}
		else if (this.unitsOfInkLeft <= INK_BUFFER) {
			this.logic.getViewController().updateOnInkLow();
			
			return Status.LOW;
		}
		else if (this.unitsOfInkLeft < FULL_INK) {
			this.logic.getViewController().updateOnInkStable();
			
			return Status.STABLE;
		}
		
		this.logic.getViewController().updateOnInkFull();
		
		return Status.FULL;
	}
	
	/**
	 * Creating a helper method to calculate the updated capacity of the paper remaining 
	 */
	private void calculatePaperCapacity(double initialCapacity, double linesPrinted) {
		this.unitsOfPaperLeft = this.unitsOfPaperLeft - (linesPrinted * 0.5);
	}
	
	/**
	 * Reset total lines to 4
	 */
	private void resetTotalLines() {
		this.totalLines = 4;
	}
	
	@Override
	public void thePrinterIsOutOfPaper() {
		this.predictPaper();
		this.onPrintingFail();
	}
	
	@Override
	public void thePrinterHasLowPaper() {
		this.predictPaper();
	}

	/**
	 * Once paper has been refilled, signal to be sent to attendant panel, that paper is full
	 * Paper capacity also resets
	 * 
	 * Assuming paper is filled to max capacity
	 */
	@Override
	public void paperHasBeenAddedToThePrinter() {
		this.unitsOfPaperLeft = FULL_PAPER;
		this.predictPaper();
	}
	
	@Override
	public void inkHasBeenAddedToThePrinter() {
		this.unitsOfInkLeft = FULL_INK;
		this.predictInk();
	}
	
	@Override
	public void thePrinterIsOutOfInk() {
		this.predictInk();
		this.onPrintingFail();
	}

	@Override
	public void thePrinterHasLowInk() {
		this.predictInk();
	}
	
	// ---- Unused ----

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
}
