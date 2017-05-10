package net.floodlightcontroller.statistics;

import java.util.ArrayList;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTGuardManager {
	private static final Logger log = LoggerFactory.getLogger(FTGuardManager.class);
	public boolean flag;
	
	public FTGuardManager() {
		this.flag = false;
	}
	
	public void init() {
		// add switch to ft-guard
		DatapathId id = DatapathId.of("00:00:00:00:00:00:00:01");
		this.addDatapath(id);
		this.addHost(IPv4Address.of("10.0.0.1"), id);
		this.addHost(IPv4Address.of("10.0.0.2"), id);
		this.addHost(IPv4Address.of("10.0.0.3"), id);
		this.addHost(IPv4Address.of("10.0.0.10"), id);
		initScore();
		this.flag = true;
	}
	
	private void initScore() {
		if (flag) return;
		StatisticsCollector.scoreImptList.add(2);
		StatisticsCollector.scoreImptList.add(6);
		StatisticsCollector.scoreImptList.add(9);
		
		ArrayList<Integer> array1 = new ArrayList<Integer>(); 
		array1.add(70); array1.add(20); array1.add(10);
		ArrayList<Integer> array2 = new ArrayList<Integer>(); 
		array2.add(10); array2.add(80); array2.add(10);
		StatisticsCollector.imptProbMap.put(1, array1);
		StatisticsCollector.imptProbMap.put(2, array2);
	}
	
	private void addDatapath(DatapathId id) {
		if (flag) return;
		log.debug("######ADD-SW-{}", id.toString());
	}
	
	private void addHost(IPv4Address ip, DatapathId id) {
		if (flag) return;
		log.debug("######ADD-IP-{}, {}", ip.toString(), id.toString());
		StatisticsCollector.hostDpMap.put(ip, id);
		StatisticsCollector.hostFlowMap.put(ip, new HostEntry(ip));
	}
}
