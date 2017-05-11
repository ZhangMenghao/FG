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
	public static int FLOW_MATCH_HIGH_THRESHOLD = 1;
	public static int CONSTANT_T = 2;
	public int importance;
	public int pi;
	public int piCopy;
	
	public HostEntry(IPv4Address ipAddress) {
		number = 0;
		highFlowNumber = 0;
		ip = ipAddress;
		pi = 0;
		importance = 1;
		score = 0;
		piCopy = 0;
	}
	
	public void init() {
		number = 0;
		highFlowNumber = 0;
		importance = 1;
		score = 0;
		pi = piCopy;
		piCopy = 0;
	}
	
	public void setScore(double s) {
		score = s;
	}
	public String toString() {
		return String.valueOf(pi) + " - " + String.valueOf((int)score) + " - " + String.valueOf(importance);
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
	
	public int computeCount() {
		double s = 0;
		if (number == 0) s = 0.000001;
		else
			s = (double)highFlowNumber / number;
		int impt = 1;
		int temp = (int)score;
		for (int i : StatisticsCollector.scoreImptList) {
			if (i > temp) break;
			impt += 1;
		}
		return impt;
	}
	
	public void compute() {
		// count part
		if (number == 0) score = 0.000001;
		else
			score = (double)highFlowNumber / number;
		score *= 10;
		int countImpt = 1;
		int temp = (int)score;
		for (int i : StatisticsCollector.scoreImptList) {
			if (i > temp) break;
			countImpt += 1;
		}
		// pi part
		int piImpt = 0;
		if (pi >= 500)
			piImpt = -2;
		else if (pi >= 300)
			piImpt = -1;
		else if (pi >= 200)
			piImpt = 0;
		else
			piImpt = 1;
		importance = piImpt + countImpt;
		if (importance < 1)
			importance = 1;
		if (importance > 3)
			importance = 3;
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
