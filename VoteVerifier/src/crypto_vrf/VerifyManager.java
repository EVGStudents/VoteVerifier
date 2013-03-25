package crypto_vrf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;


import common.Config;
import common.Verification;

@SuppressWarnings("unused")
public class VerifyManager {

	private Map<Verification, Boolean> setupResults = new HashMap<Verification, Boolean>();
	private int electionID;
	
	public VerifyManager(int electionID){
		this.electionID = electionID;
	}
	
	/**
	 * Verify the setup 
	 * @throws InterruptedException 
	 */
	public void setupVerifier() throws InterruptedException, FileNotFoundException{
		PrimitivesVrf setup = new PrimitivesVrf(Config.p, Config.q, Config.g);
		
		setupResults.put(Verification.PRIMITIVE_P_IS_PRIME, setup.isPPrime());
		
		setupResults.put(Verification.PRIMITIVE_Q_IS_PRIME, setup.isQPrime());
		
		setupResults.put(Verification.PRIMITIVE_G_IS_GENERATOR, setup.isGenerator());
		
		setupResults.put(Verification.PRIMITIVE_LENGTH_OK, 
				setup.areParametersLength(Config.pLength, Config.qLength, Config.gLength));
		
		setupResults.put(Verification.PRIMITIVE_P_IS_SAFE_PRIME, setup.isPSafePrime());
		
		//--------- BEGIN TEST -----------
		//only for test, here probably we have to call the observer and send the data to the gui
		//pay attention to the order of the verification
		@SuppressWarnings("rawtypes")
		Iterator it = setupResults.entrySet().iterator();

		System.out.println("Checking setup......");
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("etc/messages.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(1000);
		
		while(it.hasNext()){
			
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();

			Verification v = (Verification) pairs.getKey();
			
			System.out.println(p.getProperty(Integer.toString(v.getID())) + " = " + pairs.getValue());
			Thread.sleep(1000);
			
		}
		//--------- END TEST -----------
	}
	
}
