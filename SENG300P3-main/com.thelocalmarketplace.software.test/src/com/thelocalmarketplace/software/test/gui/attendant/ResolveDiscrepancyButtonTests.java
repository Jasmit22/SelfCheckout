package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AttendantStationSoftware;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.customer.CallAttendantButtonListener;
import com.thelocalmarketplace.software.test.general.AddOwnBagsTests.Bag;

import powerutility.PowerGrid;

/**

@author Connell Reffo        (10186960)
@author Tara Strickland         (10105877)
@author Julian Fan            (30235289)
@author Samyog Dahal            (30194624)
@author Phuong Le            (30175125)
@author Daniel Yakimenka        (10185055)
@author Merick Parkinson        (30196225)
@author Julie Kim            (10123567)
@author Ajaypal Sallh        (30023811)
@author Nathaniel Dafoe        (30181948)
@author Anmol Ratol            (30231177)
@author Chantal del Carmen    (30129615)
@author Dana Al Bastrami        (30170494)
@author Maria Munoz            (30175339)
@author Ernest Shukla        (30156303)
@author Hillary Nguyen        (30161137)
@author Robin Bowering        (30123373)
@author Anne Lumumba            (30171346)
@author Jasmit Saroya        (30170401)
@author Fion Lei                (30134327)
@author Royce Knoepfli        (30172598)
*/

public class ResolveDiscrepancyButtonTests {

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	CallAttendantButtonListener listener;
	AttendantStation attendantHW;
	AttendantStationSoftware attendantSW;

	JButton overrideButton;

	Mass bag1mass;
	Mass bag2mass;
	Mass invalidBagMass;
	Mass m_product;

	public BarcodedProduct product;
	public Barcode barcode;
	public Numeral digits;
	public Numeral[] barcode_numeral;

	// Although bags arent barcoded this will allow me to test logic
	Bag bag1;
	Bag bag2;
	Bag bag3;
	BarcodedItem b;

//    private CustomerView mockView= ;

	private <T extends Component> T getComponent(String fieldName, Class<T> fieldType, Object instance)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return fieldType.cast(field.get(instance));
	}

	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();

		AbstractSelfCheckoutStation.resetConfigurationToDefaults();

		// d1 = new dummyProductDatabaseWithOneItem();
		// d2 = new dummyProductDatabaseWithNoItemsInInventory();

		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		session = new SelfCheckoutStationSoftware(station);
		listener = new CallAttendantButtonListener(session);
		
		attendantHW = new AttendantStation();
		attendantSW = new AttendantStationSoftware(attendantHW);

		attendantSW.registerStation(session);
		attendantSW.start();
		overrideButton = getComponent("overrideDiscrepancyButton",JButton.class,
		attendantSW.attendantView.getCorrespondingControlPanel(session).startPanel);
		

		bag1mass = new Mass((double) 8);
		bag2mass = new Mass((double) 10);
		invalidBagMass = new Mass((double) 30);
		
		bag1 = new Bag(bag1mass);
		bag2 = new Bag(bag2mass);
		bag3 = new Bag(invalidBagMass);

		barcode_numeral = new Numeral[] { Numeral.one, Numeral.two, Numeral.three };
		barcode = new Barcode(barcode_numeral);

		product = new BarcodedProduct(barcode, "some item", 5, (double) 300.0);
		m_product = new Mass((double) 300.0);
		b = new BarcodedItem(barcode, m_product);

		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);

		session.setEnabled(true);

	}
	
	
	@Test
	public void testWhileAddingInvalidBags() {
		session.startSession();
		
		session.getMassLogic().startAddBags();
		
		station.getBaggingArea().addAnItem(bag3);
		
		session.getMassLogic().endAddBags();
		
		overrideButton.doClick();
		
		assertFalse(session.getMassLogic().getBaggingDiscrepency());
		
	}
	

	
}
