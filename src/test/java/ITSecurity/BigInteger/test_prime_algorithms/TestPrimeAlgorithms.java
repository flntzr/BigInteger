package ITSecurity.BigInteger.test_prime_algorithms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ITSecurity.BigInteger.BigInt;
import ITSecurity.BigInteger.BigIntUtils;

/*
 * 	Es werden die drei Tests (Fermat, Euler, Miller-Rabin) mit zufälligen Proben 
 * (und den Primzahlen bis 37 (siehe oben)) sowie vorher die 
 * Probedivision mit allen Primzahlen unter 100 durchgeführt. Dies wird ca. 100 Mal mit 
	(a) echten Primzahlen, 
	(b) Pseudoprimzahlen und 
	(c) zusammengesetzten Zahlen 
	durchgeführt. 
	Bestimmen Sie in wie vielen Prozent der Fälle die jeweiligen Tests irren bzw. richtig liegen.
 */
public class TestPrimeAlgorithms {
	int[] primesUpTo37 = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37 };
	int[] primesUpTo100 = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73,
			79, 83, 89, 97 };

	public boolean isDivisibleByPrimesUnder100(BigInt p) {
		for (int prime : this.primesUpTo100) {
			BigInt clone = p.clone();
			BigInt mod = clone.divMod(new BigInt(true, prime, BigInt.DEFAULT_BIG_INT_SIZE), true);
			if (mod.spart == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean passesMillerRabinForBasesUpTo37(BigInt p) {
		for (int base : this.primesUpTo37) {
			if (!p.isPrimeMillerRabin(new BigInt(true, base, BigInt.DEFAULT_BIG_INT_SIZE))) {
				return false;
			}
		}
		return true;
	}
}
