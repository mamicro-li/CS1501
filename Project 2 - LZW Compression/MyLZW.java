/*************************************************************************
 *  Sharon Gao CS 1501 Project 2
 *  Custom LZW class
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - < input.txt > output.lzw   (compress)
 *  Execution:    java MyLZW + < input.lzw > output.txt  (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
   	private static final int R = 256;        // number of input chars
	private static final int MIN_CW = 512;
	private static final int MAX_CW = 65536;
    	private static int L = MIN_CW;       // number of codewords = 2^W
	private static final int MIN_WIDTH = 9;
	private static final int MAX_WIDTH = 16;
    	private static int W = MIN_WIDTH;         // codeword width
	private static String mode = "n";		// mode (n - do nothing, r - reset, m - monitor) default n
	private static char compression_type;				// mode identifying char 
	private static int size_uncompressed = 0;
	private static int size_compressed = 0;
	private static double compression_ratio = 0;
	private static double old_ratio = 0;
	private static boolean hasRatio = false;

    public static void compress() { 
	if (mode.equals("n"))				// if n mode, write 'n' char to beginning of file
		BinaryStdOut.write('n', 8);
	else if (mode.equals("r"))
		BinaryStdOut.write('r', 8);		// else if r mode, write 'r' char to beginning of file
	else if (mode.equals("m"))
		BinaryStdOut.write('m', 8);		// else if m mode, write 'm' char to beginning of file
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);
	}
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
		L = (int)Math.pow(2, W);	// 2^W
            	String s = st.longestPrefixOf(input);  // Find max prefix match s.
		size_uncompressed += s.length() * 8;				// 
            	BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
		size_compressed += W;
		compression_ratio = size_uncompressed/size_compressed;
            	int t = s.length();
            	if (t < input.length() && code < L)    // Add s to symbol table.
                	st.put(input.substring(0, t + 1), code++);
		// DO NOTHING MODE
		// Already implemented through LZW.java
		// Increase codeword size from 9 to 16 bits as previous sizes fill up
		if ((W < MAX_WIDTH) && (code == (int)Math.pow(2, W))) {
			W++;
			L = (int)Math.pow(2, W);
			st.put(input.substring(0, t + 1), code++);
		}
		// RESET MODE
		// When codewords are all used (current codeword = 2^16), reset the dictionary back to its initial state to make room for new codewords
		if (mode.equals("r") && code == MAX_CW) {
			st = new TST<Integer>();	// reset dictionary
			for (int i = 0; i < R; i++) {
				st.put("" + (char)i, i);
			}
			code = R + 1;
			W = MIN_WIDTH;
			L = MIN_CW;
		}
		// MONITOR MODE
		// Monitor compression ratio when codeword dictionary is full but continue to use full codebook
		// When ratio of old compression/new compression exceeds 1.1, reset dictionary to initial state 
		if (mode.equals("m") && code == MAX_CW) {
			if (hasRatio == false) {
				old_ratio = compression_ratio;
				hasRatio = true;
			}
			if ((old_ratio/compression_ratio) > 1.1) {
				st = new TST<Integer>();
				for (int i = 0; i < R; i++) {
					st.put("" + (char)i, i);
				}
				code = R + 1;
				W = MIN_WIDTH;
				L = MIN_CW;
				old_ratio = 0;
				compression_ratio = 0;
				hasRatio = false;
			}
		}
		input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 
	

    public static void expand() {
	compression_type = BinaryStdIn.readChar(8);		// read first char of file to determine mode
        String[] st = new String[MAX_CW];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++) {
        	st[i] = "" + (char) i;
	}
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            	BinaryStdOut.write(val);
		size_uncompressed = val.length() * 8;
            	codeword = BinaryStdIn.readInt(W);
		size_compressed += W;
		compression_ratio = size_uncompressed/size_compressed;
            	if (codeword == R) break;
            	String s = st[codeword];
            	if (i == codeword) s = val + val.charAt(0);   // special case hack
            	if (i < (L - 1)) st[i++] = val + s.charAt(0);
		// DO NOTHING MODE
		// Default, Already implemented by LZW.java
		if ((W < MAX_WIDTH) && (i == (L - 1))) {
			st[i++] = val + s.charAt(0);
			W++;
			L = (int)Math.pow(2, W);
		}
		// RESET
		// When dictionary is completely read, reset and reinitialize with all 1-character strings
		// Reset codeword size and number of codewords
		if (compression_type == 'r' && i == MAX_CW) {
			W = MIN_WIDTH;
			L = MIN_CW;
			st = new String[MAX_CW];
			for (i = 0; i < R; i++) {
				st[i] = "" + (char)i;
			}
			st[i++] = "";						// (unused) lookahead for EOF
			codeword = BinaryStdIn.readInt(W);
			if (codeword == R) break;			// expanded message is empty string
			val = st[codeword];
		}
		// MONITOR MODE
		// Monitor compression ratio when codeword dictionary is full but continue to use full codebook
		// When ratio of old compression/new compression exceeds 1.1, reset dictionary to initial state 
		if (compression_type == 'm' && i == MAX_CW) {
			if (hasRatio == false) {
				old_ratio = compression_ratio;
				hasRatio = true;
			}
			if ((old_ratio/compression_ratio) > 1.1) {
				W = MIN_WIDTH;
				L = MIN_CW;
				st = new String[MAX_CW];
				for (i = 0; i < R; i++) {
					st[i] = "" + (char)i;
				}
				st[i++] = "";
				codeword = BinaryStdIn.readInt(W);
				if (codeword == R) break;
				val = st[codeword];
					
				old_ratio = 0;
				compression_ratio = 0;
				hasRatio = false;
			}
		}
		val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if (args[0].equals("-")) {
			if (args[1].equals("n")) {
				mode = "n";
				compress();	// n mode: Do nothing
			}
			else if (args[1].equals("r")) {			// r mode: Reset
				mode = "r";
				compress();
			}
			else if (args[1].equals("m")) {			// m mode: Monitor
				mode = "m";
				compress();
			}
			else {
				System.err.println("There was a problem with the input compression method.");
				System.exit(1);
			}
		}
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
