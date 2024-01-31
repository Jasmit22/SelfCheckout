package com.thelocalmarketplace.software;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.SwingUtilities;

import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.software.gui.view.AttendantView;

/**
 * Class that allows attendant to select and control individual stations
 * Separated from the LocalViewController as this runs independently of all self checkout machines
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
public class AttendantStationSoftware implements IStationSoftware<AttendantStation> {
	
	/**
	 * Reference to the software installed on the selected station
	 */
	private SelfCheckoutStationSoftware supervisedStation;
	
	/**
	 * Physical attendant station
	 */
	private AttendantStation hardware;
	
	/**
	 * Map of software installed on various stations indexed by the station id
	 */
	private Map<UUID, SelfCheckoutStationSoftware> stations;
	
	/**
	 * Reference to viewable class that represents what the attendant can see
	 */
	public AttendantView attendantView;
	
	
	/**
	 * Base constructor
	 */
	public AttendantStationSoftware(AttendantStation station) {
		this.stations = new HashMap<>();
		this.attendantView = new AttendantView(this);
		this.hardware = station;
	}

	/**
	 * Start the attendant view
	 */
	public void start() {
		SwingUtilities.invokeLater(() -> {
			this.attendantView.start();
        });
	}
	
	/**
	 * Starts attendant view and all local station views
	 */
	public void startAll() {
		for (Entry<UUID, SelfCheckoutStationSoftware> e : this.stations.entrySet()) {
			e.getValue().getViewController().start();
		}
		
		this.start();
	}
	
	/**
	 * Hides the view of all but the desired station (for the purposes of demonstration)
	 * @param toShow The reference to the software installed on the station we want to show
	 */
	private void showSingleStation(SelfCheckoutStationSoftware toShow) {
		for (Entry<UUID, SelfCheckoutStationSoftware> e : this.stations.entrySet()) {
			e.getValue().getViewController().hide();
		}
		
		toShow.getViewController().show();
	}
	
	/**
	 * Selects station to supervise on the software end
	 * @param id The ID of the station to supervise
	 */
	public void setSupervised(UUID id) {
		SelfCheckoutStationSoftware nextStation = this.stations.get(id);
		
		if (!this.supervisedStation.equals(nextStation)) {			
			this.supervisedStation = nextStation;
			this.showSingleStation(this.supervisedStation);
		}
	}
	
	/**
	 * Registers the software of a given station to the list of supervisable stations
	 * @param stationSoftware The station to add
	 */
	public void registerStation(SelfCheckoutStationSoftware stationSoftware) {
		this.stations.put(stationSoftware.getID(), stationSoftware);
		this.attendantView.addStationOption(stationSoftware);
		this.hardware.add(stationSoftware.getHardware());
		
		if (this.stations.size() == 1) {
			this.supervisedStation = stationSoftware;
			this.showSingleStation(stationSoftware);
		}
	}
	
	public SelfCheckoutStationSoftware getSupervisedStation() {
		return this.supervisedStation;
	}
	
	public Map<UUID, SelfCheckoutStationSoftware> getStations() {
		return this.stations;
	}

	@Override
	public AttendantStation getHardware() {
		return this.hardware;
	}
}
