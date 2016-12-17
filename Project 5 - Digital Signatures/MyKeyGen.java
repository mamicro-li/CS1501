/** Sharon Gao
* CS 1501, Project 5
* MyKeyGen.java
* Generates a 1024 bit RSA keypair and stores the public and private keys in files 
* named pubkey.rsa and privkey.rsa respectively.
* BigInteger class is used for key generation.
**/

import java.math.BigInteger;
import java.util.Random;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class MyKeyGen {
	public static void main(String[] args) throws IOException {
		Random rnd = new Random();	// Random object for BigInteger
		BigInteger P = new BigInteger(1024, 256, rnd); // Random prime number P used to compute N
		BigInteger Q = new BigInteger(1024, 256, rnd); // Random prime number Q used to compute N
		BigInteger N = P.multiply(Q); // N = P * Q
		
		BigInteger P_minusOne = P.subtract(BigInteger.ONE); // P - 1 using BigInteger ONE constant
		BigInteger Q_minusOne = Q.subtract(BigInteger.ONE); // Q - 1 using BigInteger ONE constant
		BigInteger phiN = P_minusOne.multiply(Q_minusOne); // PHI(N) = (P - 1) * (N - 1)
		
		// Determine value for E where 1 < E < PHI(N) and GCD(E, PHI(N)) = 1
		BigInteger E = BigInteger.ZERO;
		BigInteger temp = BigInteger.ONE.add(BigInteger.ONE);
		while(!temp.gcd(phiN).equals(BigInteger.ONE)) {
			temp = temp.add(BigInteger.ONE);
		}
		E = temp;
		BigInteger D = E.modInverse(phiN); // D = E^-1 mod PHI(N)
		
		// RSA files to hold public and private keys
		FileOutputStream pubkey = new FileOutputStream("pubkey.rsa"); 
		FileOutputStream privkey = new FileOutputStream("privkey.rsa"); 
		ObjectOutputStream pub = new ObjectOutputStream(pubkey); 
		ObjectOutputStream priv = new ObjectOutputStream(privkey);
		// Save E and N to pubkey.rsa
		pub.writeObject(E);	
		pub.writeObject(N);
		// Save D and N to privkey.rsa
		priv.writeObject(D);
		priv.writeObject(N);
		// Close output streams
		pub.close();
		priv.close();	
		
	}
}
