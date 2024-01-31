package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.tdc.CashOverloadException;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.MaintainCoinsButtonListener;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class MaintainCoinsButtonListenerTests {

	
	private MaintainCoinsButtonListener listener;
	private JTextField field;

	private JButton button;
	private BigDecimal denom;

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	Coin[] coins;
	
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
		coins = new Coin[] {new Coin(c,denom)};
		listener = new MaintainCoinsButtonListener(logic, field, denom);
		button.addActionListener(listener);
		ICoinDispenser dispenser = logic.getHardware().getCoinDispensers().get(denom);
		dispenser.unload();
		button.doClick();
		assertTrue("did not load coins properly", dispenser.size()==1);

	}@Test 
	public void testUnloadCoins() throws NoSuchFieldException, IllegalAccessException, SimulationException, CashOverloadException {
		field = new JTextField("-1");
		denom = new BigDecimal(1.0);
		Currency c = Currency.getInstance("CAD");
		coins = new Coin[] {new Coin(c,denom)};
		listener = new MaintainCoinsButtonListener(logic, field, denom);
		button.addActionListener(listener);
		ICoinDispenser dispenser = logic.getHardware().getCoinDispensers().get(denom);
		dispenser.unload();
		dispenser.load(coins);
		button.doClick();
		assertTrue("did not load coins properly", dispenser.size()==0);

	}
	
}


