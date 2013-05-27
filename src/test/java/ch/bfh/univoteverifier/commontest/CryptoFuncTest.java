/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.commontest;

import ch.bfh.univoteverifier.common.CryptoFunc;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the behavior of the CryptoFunc.
 *
 * @author Scalzi Giuseppe
 */
public class CryptoFuncTest {

	/**
	 * Test if the sha1 function produces the correct results.
	 *
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testSHA1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//precomputed hash values
		String n1 = "9035e137ab7dfd59db43a60fb84488f52053d646"; //12345575776
		String n2 = "f0509c8f1ca1d3657340318b61543adc8bf2753e"; //86765545332
		String n3 = "55fcc964fd8134e89b8b139accb7eba95eedf452"; //12356454444476
		String n4 = "dd8cf2abfacc3baa4d647514bd4e856fdb331cdd"; //1457676
		String n5 = "1a4633b0de3e190c24500109c6cb2dffb22a710b"; //930923808408098098502438242
		String n6 = "455959ce65fb1b98cc270e6c196189fab7ed31a9"; //3897394798739487498759875987983798
		String n7 = "3827c8665923262a283efd554fc3e5ee31d3df8f"; //1423453576845
		String n8 = "91706a501a7d62d6fa8487b5c5ad46bdb710b874"; //349685900024545
		String n9 = "d5575e0f1b1c475dba26d1b8ca3378c0daf7a6db"; //5487598349837984
		String n10 = "b2fff679347a5f1f96694e2c86d24499df54e588"; //1457596735960394569374698576985776

		//call the sha1 function and test if the results are equals
		assertEquals(n1, CryptoFunc.sha1("12345575776").toString(16));
		assertEquals(n2, CryptoFunc.sha1("86765545332").toString(16));
		assertEquals(n3, CryptoFunc.sha1("12356454444476").toString(16));
		assertEquals(n4, CryptoFunc.sha1("1457676").toString(16));
		assertEquals(n5, CryptoFunc.sha1("930923808408098098502438242").toString(16));
		assertEquals(n6, CryptoFunc.sha1("3897394798739487498759875987983798").toString(16));
		assertEquals(n7, CryptoFunc.sha1("1423453576845").toString(16));
		assertEquals(n8, CryptoFunc.sha1("349685900024545").toString(16));
		assertEquals(n9, CryptoFunc.sha1("5487598349837984").toString(16));
		assertEquals(n10, CryptoFunc.sha1("1457596735960394569374698576985776").toString(16));
	}

	/**
	 * Test if the sha256 function produces the correct results.
	 *
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testSHA256() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//precomputed hash values
		String n1 = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e"; //Hello World
		String n2 = "34210dcdba2a4fbbea8799ce84a14e8af1be3dd6a10796f32e8c2d008cb38c6c"; //BernerFachHochSchule
		String n3 = "9f16e8de08a13d2fddb93cb45b43314221a9d8a98bb0b0bf7524b3b0f18349b1"; //67576576565765765675097986
		String n4 = "c75fdf57262d80d3eec1b8c605c4af4810d1e7d8f8d1b8c38e9e9b1749a08e73"; //7564359658765750894707
		String n5 = "819b29504c990a5376f9e64bbbf4247cda9301f6d161511dc945573064033e55"; //www.bfh.ch
		String n6 = "d95f41a3878f45ca23ead082a1d9891b889cf9b68e92ae2fed2f29f332ad0355"; //(vsbfh-2013|(t1|t2|t3)|2013-13-13|electionmanager)
		String n7 = "c0550a2437ff54ec637af0d0082eff1a7e9a14cc134e9ae22debda93367f1dc5"; //(sub-2013|(t1|t2|t3)|(m1|m2|m3)|2013-13-13|certificateauthority)
		String n8 = "1f5296513f009997b7735ffbe5ca1c0231dc5b558fd76e989b2631bff3718eb9"; //345876986795598674987063598763975084576093709857609876890743980787
		String n9 = "29b21b5cd967b2fcec20d7a915d23342978e06c60a9c176e4efc4b386ab535aa"; //2124764530520406897685760759847598274985790857698759856709387659087987
		String n10 = "ed563fc634233228f3fe40411234e487cd5d7591af5f40a9bac4b4d985b746a6"; //(sub-2013|1024|2013-4-12-12:00:12)

		//call the sha256 function and test if the results are equals
		assertEquals(n1, CryptoFunc.sha256("Hello World").toString(16));
		assertEquals(n2, CryptoFunc.sha256("BernerFachHochSchule").toString(16));
		assertEquals(n3, CryptoFunc.sha256("67576576565765765675097986").toString(16));
		assertEquals(n4, CryptoFunc.sha256("7564359658765750894707").toString(16));
		assertEquals(n5, CryptoFunc.sha256("www.bfh.ch").toString(16));
		assertEquals(n6, CryptoFunc.sha256("(vsbfh-2013|(t1|t2|t3)|2013-13-13|electionmanager)").toString(16));
		assertEquals(n7, CryptoFunc.sha256("(sub-2013|(t1|t2|t3)|(m1|m2|m3)|2013-13-13|certificateauthority)").toString(16));
		assertEquals(n8, CryptoFunc.sha256("345876986795598674987063598763975084576093709857609876890743980787").toString(16));
		assertEquals(n9, CryptoFunc.sha256("2124764530520406897685760759847598274985790857698759856709387659087987").toString(16));
		assertEquals(n10, CryptoFunc.sha256("(sub-2013|1024|2013-4-12-12:00:12)").toString(16));
	}

	/**
	 * Test if the decode function for base64 works.
	 */
	@Test
	public void testBase64() throws UnsupportedEncodingException {
		//precomputed base64 encoded string
		String b1 = "KHN1Yi0yMDEzfDEwMjR8MjAxMy00LTEyLTEyOjAwOjEyKQ=="; //(sub-2013|1024|2013-4-12-12:00:12)
		String b2 = "KHN1Yi0yMDEzfCh0MXx0Mnx0Myl8KG0xfG0yfG0zKXwyMDEzLTEzLTEzfGNlcnRpZmljYXRlYXV0aG9yaXR5KQ==";	//(sub-2013|(t1|t2|t3)|(m1|m2|m3)|2013-13-13|certificateauthority)
		String b3 = "OTA3NTg3OTg5ODc2NzU="; //90758798987675
		String b4 = "MzQ1ODc2OTg2Nzk1NTk4Njc0OTg3MDYzNTk4NzYzOTc1MDg0NTc2MDkzNzA5ODU3NjA5ODc2ODkwNzQzOTgwNzg3"; //345876986795598674987063598763975084576093709857609876890743980787
		String b5 = "MjEyNDc2NDUzMDUyMDQwNjg5NzY4NTc2MDc1OTg0NzU5ODI3NDk4NTc5MDg1NzY5ODc1OTg1NjcwOTM4NzY1OTA4Nzk4Nw=="; //2124764530520406897685760759847598274985790857698759856709387659087987

		//check that the decode works
		assertEquals("(sub-2013|1024|2013-4-12-12:00:12)", new String(CryptoFunc.decodeBase64("KHN1Yi0yMDEzfDEwMjR8MjAxMy00LTEyLTEyOjAwOjEyKQ==")));
		assertEquals("(sub-2013|(t1|t2|t3)|(m1|m2|m3)|2013-13-13|certificateauthority)", new String(CryptoFunc.decodeBase64(b2)));
		assertEquals("90758798987675", new String(CryptoFunc.decodeBase64(b3)));
		assertEquals("345876986795598674987063598763975084576093709857609876890743980787", new String(CryptoFunc.decodeBase64(b4)));
		assertEquals("2124764530520406897685760759847598274985790857698759856709387659087987", new String(CryptoFunc.decodeBase64(b5)));

	}
}
