package proxy;

import java.util.List;

public class VerificatorProxy implements Subject {

	RealSubject rs = new RealSubject();
	List<Object> list = null;
	
	@Override
	public List<Object> getElectionData(int id) {
		
		System.out.println("Inside the proxy");
		
		
		if(list == null){
			list = rs.getElectionData(id);
			System.out.println("Data is not chached.");
			return list;
			}
		else{ //if the data are already downloaded, return the object
			System.out.println("Data is chached.");
			return list;
		}
		
	}

}
