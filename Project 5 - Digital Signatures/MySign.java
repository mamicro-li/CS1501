/** Sharon Gao
* CS 1501, Project 5
* Signs files and verifies signatures.
* Takes two command-line arguments, a flag to specify signing or verifying ("s" or "v") and the file that should be signed or verified.
* If called to sign, program:
* 1. generates an SHA-256 hash of the file contents, 
* 2. "decrypts" this hash value using the private key stored in privkey.rsa, 
* 3. writes out a signed version of the file ("my.file.text.signed") that contains the contents of the file and the "decrypted" hash of the original file.
* If called to verify, program:
* 1. reads the contents of the original file,
* 2. generates an SHA-256 hash of the file contents
* 3. reads the "decrypted" hash of the original file
* 4. "encrypts" this hash value with the public key stored in pubkey.rsa
* 5. compares the two hash values and prints to console whether or not signature is valid (values are the same)
**/

import java.io.IOException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.math.BigInteger;

public class MySign {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		char flag = args[0].charAt(0);	// Sign 's' or verify 'v' flag read from command line
		String file = args[1];
		
		// SIGNED MODE
		if (flag == 's') { 
			String fileName = args[1] + ".signed"; 	
			FileInputStream fileInput;
			
			// Attempt to read private key
			// Exits and displays error message if privkey.rsa is not found in current directory
			try {
				fileInput = new FileInputStream("privkey.rsa");
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: \"privkey.rsa\" was not found in the current directory. Please generate a keypair by running MyKeyGen.");
				return;
			} // end try-catch
			
			// Read D and N from privkey.rsa
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			BigInteger D = (BigInteger)objectInput.readObject();
			BigInteger N = (BigInteger)objectInput.readObject();
			objectInput.close(); // Close input streams
			
			// Generate SHA-256 Hash
			try {
				// Read in the file to hash
				Path path = Paths.get(file);
				byte[] data = Files.readAllBytes(path);

				// Create class instance to create SHA-256 hash
				MessageDigest md = MessageDigest.getInstance("SHA-256");

				// Process the file
				md.update(data);
				// Generate a hash of the file
				byte[] digest = md.digest();

				BigInteger result = new BigInteger(1, digest);	// BigInteger hash value
				BigInteger decrypt = result.modPow(D, N); // Decrypt -> Hash value raised to Dth power mod N

				FileReader reader = new FileReader(file);
				BufferedReader bReader = new BufferedReader(reader);
				FileOutputStream fileOutput = new FileOutputStream(fileName);
				ObjectOutputStream output = new ObjectOutputStream(fileOutput);
				String text;
				String content = "";
				while ((text = bReader.readLine()) != null) {
					content = text + '\n';
				}
				output.writeObject(content);
				output.writeObject(decrypt);
				// Close streams
				bReader.close();
				output.close();
			} catch(Exception e) {
				System.out.println(e.toString());
			} // end try-catch
		} // end if
		// VERIFY MODE
		else if (flag == 'v') {
			// Exits and displays error message if file has not yet been signed
			if (!file.contains(".signed")) {
				System.err.println("ERROR: File has not yet been signed. Please sign before attempting to verify signatures.");
				return;
			}
			FileInputStream fileInput;
			
			// Attempts to read public key from pubkey.rsa
			// Exits and displays error message if pubkey.rsa is not found in current directory
			try {
				fileInput = new FileInputStream("pubkey.rsa");
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: \"pubkey.rsa\" was not found in the current directory. Please generate a keypair by running MyKeyGen.");
				return;
			} // end try-catch
			
			// Read E and N from privkey.rsa
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			BigInteger E = (BigInteger)objectInput.readObject();
			BigInteger N = (BigInteger)objectInput.readObject();
			objectInput.close(); // Close input streams
			
			// Read file contexts and decrypted hash value
			fileInput = new FileInputStream(file);
			objectInput = new ObjectInputStream(fileInput);
			String content = (String)objectInput.readObject();
			BigInteger hash = (BigInteger)objectInput.readObject();
			String originalFile = file.replace(".signed", "");
			
			// Generate SHA-256 Hash
			try {
				// Read in the file to hash
				Path path = Paths.get(originalFile);
				byte[] data = Files.readAllBytes(path);

				// Create class instance to create SHA-256 hash
				MessageDigest md = MessageDigest.getInstance("SHA-256");

				// Process the file
				md.update(data);
				// Generate a hash of the file
				byte[] digest = md.digest();

				BigInteger result = new BigInteger(1, digest);	// BigInteger hash value
				BigInteger encrypt = hash.modPow(E, N); // Encrypt -> Hash value raised to Eth power mod N
				
				if (encrypt.compareTo(result) == 0) 
					System.out.println("Signature is valid.");
				else 
					System.out.println("Invalid signature.");
			} catch(Exception e) {
				System.out.println(e.toString());
			} // end try-catch
		} // end else if
		else {
			System.err.println("ERROR: Invalid command line argument.");
			return;
		} // end else
		
	}
}