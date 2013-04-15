/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ch.bfh.univoteverifier.common.StringConcatenator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
public class StringConcatenatorTest {
	
	StringConcatenator sc;
	
	public StringConcatenatorTest() {
		this.sc = new StringConcatenator();
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
	public  void pullConcatenation(){
		sc.pushObject("string");
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject("string");

		assertEquals("string|string", sc.pullAll());

		//now we must have an empty string
		assertEquals("", sc.pullAll());
		
	}

	
	@Test
	public void simpleConcatenation(){
		String one = "first";
		String two = "second";
	
		sc.pushObject(StringConcatenator.LEFT_DELIMITER);
		sc.pushObject(one);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(two);
		sc.pushObject(StringConcatenator.RIGHT_DELIMITER);
		
		assertEquals("(first|second)", sc.pullAll());


	}
	
	@Test
	public void simpleConcatenationTwo(){
		String first = "first";
		String array_first = "arr1";
		String array_second = "arr2";
		String array_third = "arr3";
		String second = "second";

		sc.pushObject(StringConcatenator.LEFT_DELIMITER);
		sc.pushObject(first);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(StringConcatenator.LEFT_DELIMITER);
		sc.pushObject(array_first);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(array_second);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(array_third);
		sc.pushObject(StringConcatenator.RIGHT_DELIMITER);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(second);
		sc.pushObject(StringConcatenator.RIGHT_DELIMITER);

		assertEquals("(first|(arr1|arr2|arr3)|second)", sc.pullAll());

		
	}

	@Test
	public void pushListObject(){
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("134536454634534"));
		bi.add(new BigInteger("454634534"));
		bi.add(new BigInteger("134536534"));
		bi.add(new BigInteger("634534"));
		bi.add(new BigInteger("579485769845"));
		bi.add(new BigInteger("678549"));
		bi.add(new BigInteger("13565565756134536454634534"));
		bi.add(new BigInteger("9879879134536454634534"));
	
		sc.pushList(bi, true);

		String res = sc.pullAll();

					
		assertEquals("(134536454634534|454634534|134536534|634534|579485769845|678549|13565565756134536454634534|9879879134536454634534)", res);
	}

	@Test
	public void pushListSimple(){
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("1"));

		sc.pushList(bi, true);

		assertEquals("1", sc.pullAll());

		
	}
	
	@Test
	public void pushListObjectTwo(){
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("1"));
		bi.add(new BigInteger("1"));

		sc.pushList(bi, true);

		assertEquals("(1|1)", sc.pullAll());

		
	}

	@Test
	public void pushListObjectEmpty(){
		List<BigInteger> bi = new ArrayList<>();

		sc.pushList(bi, true);

		assertEquals("", sc.pullAll());

		
	}

	@Test
	public void pushListWithoutSeparators(){
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("8458347593847978797879871"));
		bi.add(new BigInteger("445363641"));

		sc.pushList(bi, false);

		assertEquals("8458347593847978797879871445363641", sc.pullAll());
	}
}