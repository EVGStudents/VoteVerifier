package proxy;

import java.util.List;
import java.util.ArrayList;

public class RealSubject implements Subject {

	@Override
	public List<Object> getElectionData(int id) {
		//Here connection to the election board and download of data
		System.out.println("Inside the real subject: dowloading the data....");
		return new ArrayList<Object>();
	}

}
