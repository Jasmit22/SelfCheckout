package com.thelocalmarketplace.software.demo;

import java.util.Map.Entry;
import java.util.UUID;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.ReusableBag;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.AttendantStationSoftware;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import powerutility.PowerGrid;

/**
 * Main entry point
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
public class Main {
	
	static ReusableBag bag1 = new ReusableBag();
	static ReusableBag bag2 = new ReusableBag();
	static ReusableBag bag3 = new ReusableBag();
	static ReusableBag bag4 = new ReusableBag();
	static ReusableBag bag5 = new ReusableBag();
	static ReusableBag bag6 = new ReusableBag();
	static ReusableBag bag7 = new ReusableBag();
	
	private static ReusableBag[] bags = new ReusableBag[] { bag1, bag2, bag3, bag4, bag5, bag6, bag7 };
	
	private static final int STATION_COUNTS = 2;
	
	
	public static void main(String[] args) throws OverloadedDevice {
		
		// Setup values for demo
		DemoSetup.setup();
		
		// Configure station
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		AbstractSelfCheckoutStation.configureCurrency(DemoSetup.CURRENCY);
		AbstractSelfCheckoutStation.configureCoinDenominations(DemoSetup.COIN_DENOMS_CAD);
		AbstractSelfCheckoutStation.configureBanknoteDenominations(DemoSetup.BANKNOTE_DENOMS_CAD);
		
		// Plug into power grid and turn on stations
		PowerGrid.engageUninterruptiblePowerSource();	

		// Create attendant station
		AttendantStation attendantStation = new AttendantStation();
		
		// Setup attendant controls
		AttendantStationSoftware globalController = new AttendantStationSoftware(attendantStation);
		
		// Instantiate station hardware
		for (int i = 0; i < STATION_COUNTS; i++) {
			SelfCheckoutStationBronze b = new SelfCheckoutStationBronze();
			SelfCheckoutStationSilver s = new SelfCheckoutStationSilver();
			SelfCheckoutStationGold g = new SelfCheckoutStationGold();
			
			b.plugIn(PowerGrid.instance());
			g.plugIn(PowerGrid.instance());
			s.plugIn(PowerGrid.instance());
			
			b.turnOn();
			g.turnOn();
			s.turnOn();
			
			b.getReusableBagDispenser().load(bags);
			g.getReusableBagDispenser().load(bags);
			s.getReusableBagDispenser().load(bags);
			
			SelfCheckoutStationSoftware bs = new SelfCheckoutStationSoftware(b, "Bronze Station " + (i + 1));
			SelfCheckoutStationSoftware ss = new SelfCheckoutStationSoftware(g, "Silver Station " + (i + 1));
			SelfCheckoutStationSoftware gs = new SelfCheckoutStationSoftware(s, "Gold Station " + (i + 1));
			
			bs.getViewController().getCustomerSimulationView().configureCustomerSimulation(DemoSetup.CURRENCY, DemoSetup.DEMO_BANK_CARDS, DemoSetup.DEMO_MEMBERSHIP_CARDS, DemoSetup.DEMO_ITEMS, DemoSetup.DEMO_BAGS, DemoSetup.DEMO_COIN_DENOMS, DemoSetup.DEMO_BANKNOTE_DENOMS);
			ss.getViewController().getCustomerSimulationView().configureCustomerSimulation(DemoSetup.CURRENCY, DemoSetup.DEMO_BANK_CARDS, DemoSetup.DEMO_MEMBERSHIP_CARDS, DemoSetup.DEMO_ITEMS, DemoSetup.DEMO_BAGS, DemoSetup.DEMO_COIN_DENOMS, DemoSetup.DEMO_BANKNOTE_DENOMS);
			gs.getViewController().getCustomerSimulationView().configureCustomerSimulation(DemoSetup.CURRENCY, DemoSetup.DEMO_BANK_CARDS, DemoSetup.DEMO_MEMBERSHIP_CARDS, DemoSetup.DEMO_ITEMS, DemoSetup.DEMO_BAGS, DemoSetup.DEMO_COIN_DENOMS, DemoSetup.DEMO_BANKNOTE_DENOMS);
			
			bs.getCardPaymentLogic().configureCardIssuer(DemoSetup.BANK);
			ss.getCardPaymentLogic().configureCardIssuer(DemoSetup.BANK);
			gs.getCardPaymentLogic().configureCardIssuer(DemoSetup.BANK);
			
			bs.getMembershipLogic().registerMembershipToLocalDatabase("23788");
			ss.getMembershipLogic().registerMembershipToLocalDatabase("23788");
			gs.getMembershipLogic().registerMembershipToLocalDatabase("23788");
			
			globalController.registerStation(bs);
			globalController.registerStation(ss);
			globalController.registerStation(gs);
		}

		// Start all views
		globalController.startAll();
		
		// Load station dispensables
		for (Entry<UUID, SelfCheckoutStationSoftware> e : globalController.getStations().entrySet()) {
			DemoSetup.preloadStationDispensables(e.getValue().getHardware());
		}
	}
}
