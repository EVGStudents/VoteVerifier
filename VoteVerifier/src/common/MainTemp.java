package common;

import java.io.FileNotFoundException;

import proxy.Subject;
import proxy.VerificatorProxy;

import crypto_vrf.VerifyManager;



public class MainTemp {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//System.out.println(Config.p.toString());
		//System.out.println(Config.q.toString());
		//System.out.println(Config.g.toString());
		
		VerifyManager vm = new VerifyManager(0);
		
		try {
			vm.setupVerifier();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Test of the proxy
		Subject myProxy = new VerificatorProxy();
		
		myProxy.getElectionData(1);
		myProxy.getElectionData(1);
		
		
	}

}
