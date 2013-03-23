package common;

import java.math.BigInteger;

import crypto_vrf.VerifyManager;



public class MainTemp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(Config.p.toString());
		//System.out.println(Config.q.toString());
		//System.out.println(Config.g.toString());
		
		VerifyManager vm = new VerifyManager();
		
		vm.setupVerifier();
		
	}

}
