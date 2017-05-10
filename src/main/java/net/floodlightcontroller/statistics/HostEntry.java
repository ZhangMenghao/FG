package net.floodlightcontroller.statistics;

import org.projectfloodlight.openflow.protocol.OFFlowStatsEntry;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostEntry {
	private static final Logger log = LoggerFactory.getLogger(HostEntry.class);
	private int number; // Ni
	private double score; // Wi
	private int highFlowNumber;
	private IPv4Address ip;
	public static int HOST_INIT_FI = 2;
	public static int FLOW_MATCH_HIGH_THRESHOLD = 2;
	public static int CONSTANT_T = 2;
	public int importance;
	
	public HostEntry(IPv4Address ipAddress) {
		number = 0;
		highFlowNumber = 0;
		ip = ipAddress;		
		importance = 2;
		score = 0;
	}
	
	public void init() {
		number = 0;
		highFlowNumber = 0;
		importance = 2;
		score = 0;
	}
	
	public void setScore(double s) {
		score = s;
	}
	public String toString() {
		return String.valueOf((int)score) + " - " + String.valueOf(importance);
	}
	public int getHighFlowNumber() {
		return highFlowNumber;
	}
	public int getNumber() {
		return number;
	}
	public double getScore() {
		return score;
	}
	
	public void compute() {
		if (number == 0) score = 0.000001;
		else
			score = (double)highFlowNumber / number;
		score *= 10;
		int impt = 1;
		int temp = (int)score;
		for (int i : StatisticsCollector.scoreImptList) {
			if (i > temp) break;
			impt += 1;
		}
		importance = impt;
	}
	
//	public void compute() {
//		if (number == 0) score = 0.000001;
//		else
//			score = (double)highFlowNumber / number;
//		score *= 10;
//		int impt = 1;
//		int temp = (int)score;
//		for (int i : StatisticsCollector.scoreImptList) {
//			if (i > temp) break; 
//			impt += 1;
//		}
//		int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
//		if (impt == StatisticsCollector.IMPORTANCE_NUM) importance = impt;
//		else {
//			int rank = 1;
//			for (int i : StatisticsCollector.imptProbMap.get(impt)) {
//				randomNum -= i;
//				if (randomNum < 0) {
//					importance = rank;
//					break;
//				}
//				rank += 1;
//			}
//		}
//	}
	
	public void udpateByReply(OFFlowStatsEntry pse) {
		int packetCount = (int)pse.getPacketCount().getValue();
        number += 1;
        if (packetCount > FLOW_MATCH_HIGH_THRESHOLD) {
            this.highFlowNumber += 1;
        }
	}
	
	public void addEntry(Match match) {
		number += 1;
	}
}
