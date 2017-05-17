package net.floodlightcontroller.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;

public class FTGuardManager implements IFloodlightModule, IOFMessageListener {
	private static final Logger log = LoggerFactory.getLogger(FTGuardManager.class);
	protected IFloodlightProviderService floodlightProvider;
	
	private static final String ALPHA_STR = "alpha";
	private static final String HIGH_COUNT_STR = "highcount";
	public static double alpha = 0;
	public static int highCount = 0;
	private static final String PI_PARA1_STR = "pipara1";
	private static final String PI_PARA2_STR = "pipara2";
	public static int piPara1 = 0;
	public static int piPara2 = 0;
	private static final String COUNT_PARA1_STR = "countpara1";
	private static final String COUNT_PARA2_STR = "countpara2";
	public static int countPara1 = 0;
	public static int countPara2 = 0;
	
	
	public FTGuardManager() {}
	
	private void initScore() {
		StatisticsCollector.scoreImptList.add(countPara1);
		StatisticsCollector.scoreImptList.add(countPara2);
		
		ArrayList<Integer> array1 = new ArrayList<Integer>(); 
		array1.add(70); array1.add(20); array1.add(10);
		ArrayList<Integer> array2 = new ArrayList<Integer>(); 
		array2.add(10); array2.add(80); array2.add(10);
		StatisticsCollector.imptProbMap.put(1, array1);
		StatisticsCollector.imptProbMap.put(2, array2);
		log.info("######ALPHA = {}", alpha);
		log.info("######COUNT = {}", highCount);
	}
	
	private void addHost(IPv4Address ip, DatapathId id) {
		if (!StatisticsCollector.hostDpMap.containsKey(ip)) {
			StatisticsCollector.hostDpMap.put(ip, id);	
			log.debug("######ADD-IP-{}, {}", ip.toString(), id.toString());
		}
		if (!StatisticsCollector.hostFlowMap.containsKey(ip)) {
			StatisticsCollector.hostFlowMap.put(ip, new HostEntry(ip));
			log.debug("######ADD-IP-{}, {}", ip.toString(), id.toString());
		}
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// TODO Auto-generated method stub
	    Collection<Class<? extends IFloodlightService>> l =
	            new ArrayList<Class<? extends IFloodlightService>>();
	    l.add(IFloodlightProviderService.class);
	    return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		// TODO Auto-generated method stub
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		Map<String, String> config = context.getConfigParams(this);
		if (config.containsKey(ALPHA_STR)) {
			try {
				alpha = Double.parseDouble(config.get(ALPHA_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", ALPHA_STR, alpha);
			}
		}
		if (config.containsKey(HIGH_COUNT_STR)) {
			try {
				highCount = Integer.parseInt(config.get(HIGH_COUNT_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", HIGH_COUNT_STR, highCount);
			}
		}
		if (config.containsKey(PI_PARA1_STR)) {
			try {
				piPara1 = Integer.parseInt(config.get(PI_PARA1_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", PI_PARA1_STR, highCount);
			}
		}
		if (config.containsKey(PI_PARA2_STR)) {
			try {
				piPara2 = Integer.parseInt(config.get(PI_PARA2_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", PI_PARA2_STR, highCount);
			}
		}
		if (config.containsKey(COUNT_PARA1_STR)) {
			try {
				countPara1 = Integer.parseInt(config.get(COUNT_PARA1_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", COUNT_PARA1_STR, highCount);
			}
		}
		if (config.containsKey(COUNT_PARA2_STR)) {
			try {
				countPara2 = Integer.parseInt(config.get(COUNT_PARA2_STR).trim());
			} catch (Exception e) {
				log.error("Could not parse '{}'. Using default of {}", COUNT_PARA2_STR, highCount);
			}
		}
		initScore();
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		// TODO Auto-generated method stub
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return FTGuardManager.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return (name.equals("forwarding") || name.equals("routing"));
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg,
			FloodlightContext cntx) {
        Ethernet eth =
                IFloodlightProviderService.bcStore.get(cntx,
                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
 
        if (eth.getEtherType() == EthType.IPv4) {
        	IPv4 ip = (IPv4) eth.getPayload();
        	IPv4Address srcIp = ip.getSourceAddress();
        	if (srcIp.toString().equals("0.0.0.0")) return Command.CONTINUE;
        	addHost(srcIp, sw.getId());
        }
        return Command.CONTINUE;
	}
}
