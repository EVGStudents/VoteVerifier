/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package electionboardproxy;

import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snake
 */
public class ElectionBoardProxyTest {
	
	ElectionBoardProxy ebp;
	
	public ElectionBoardProxyTest() {
		ebp = new ElectionBoardProxy("sub-2013");
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	// @Test
	// public void hello() {}

	@Test
	public void testElectionData() throws ElectionBoardServiceFault{
		ElectionData ed = ebp.getElectionData();

		assertNotNull(ed);

	}

	@Test
	public void testElectionSystemInfo() throws ElectionBoardServiceFault{
		ElectionSystemInfo esi = ebp.getElectionSystemInfo();
		assertNotNull(esi);
	}

}