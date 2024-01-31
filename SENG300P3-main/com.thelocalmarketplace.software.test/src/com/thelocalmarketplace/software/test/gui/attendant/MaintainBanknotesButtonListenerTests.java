package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.MaintainBanknotesButtonListener;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class MaintainBanknotesButtonListenerTests {

	
	private MaintainBanknotesButtonListener listener;
	private JTextField field;

	private JButton button;
	private BigDecimal denom;

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	Banknote[] banknotes;
	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();

		AbstractSelfCheckoutStation.resetConfigurationToDefaults();

		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		logic = new SelfCheckoutStationSoftware(station);
		button = new JButton();
	}

	@Test 
	public void testLoadCoins() throws NoSuchFieldException, IllegalAccessException {
		field = new JTextField("1");
		denom = new BigDecimal(1.0);
		Currency c = Currency.getInstance("CAD");
		banknotes = new Banknote[] {new Banknote(c,denom)};
		listener = new MaintainBanknotesButtonListener(logic, field, denom);
		button.addActionListener(listener);
		IBanknoteDispenser dispenser = logic.getHardware().getBanknoteDispensers().get(denom);
		dispenser.unload();
		button.doClick();
		assertTrue("did not load coins properly", dispenser.size()==1);

	}@Test 
	public void testUnloadCoins() throws NoSuchFieldException, IllegalAccessException, SimulationException, CashOverloadException {
		field = new JTextField("-1");
		denom = new BigDecimal(1.0);
		Currency c = Currency.getInstance("CAD");
		banknotes = new Banknote[] {new Banknote(c,denom)};
		listener = new MaintainBanknotesButtonListener(logic, field, denom);
		button.addActionListener(listener);
		IBanknoteDispenser dispenser = logic.getHardware().getBanknoteDispensers().get(denom);
		dispenser.unload();
		dispenser.load(banknotes);
		button.doClick();
		assertTrue("did not load coins properly", dispenser.size()==0);

	}
	
}


