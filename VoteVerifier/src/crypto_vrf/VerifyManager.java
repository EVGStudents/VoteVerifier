package crypto_vrf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import utils.Verification;

import common.Config;

@SuppressWarnings("unused")
public class VerifyManager {

	private Map<Verification, Boolean> setupResults = new HashMap<Verification, Boolean>();
	
	/**
	 * Verify the setup 
	 */
	public void setupVerifier(){
		PrimitivesVrf setup = new PrimitivesVrf(Config.p, Config.q, Config.g);
		
		setupResults.put(Verification.PRIMITIVE_P_IS_PRIME, setup.isPPrime());
		
		setupResults.put(Verification.PRIMITIVE_Q_IS_PRIME, setup.isQPrime());
		
		setupResults.put(Verification.PRIMITIVE_G_IS_GENERATOR, setup.isGenerator());
		
		setupResults.put(Verification.PRIMITIVE_LENGTH_OK, 
				setup.areParametersLength(Config.pLength, Config.qLength, Config.gLength));
		
		//only for test, here probably we have to call the observer and send the data to the gui
		//pay attention to the order of the verification
		@SuppressWarnings("rawtypes")
		Iterator it = setupResults.entrySet().iterator();
		
		System.out.println("Length of the map: " + setupResults.size());
		
		while(it.hasNext()){
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			
		}
	}
	
}
