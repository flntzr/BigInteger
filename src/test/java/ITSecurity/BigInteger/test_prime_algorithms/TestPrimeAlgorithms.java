package ITSecurity.BigInteger.test_prime_algorithms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ITSecurity.BigInteger.BigInt;
import ITSecurity.BigInteger.BigIntUtils;
import ITSecurity.BigInteger.ImportedTestCase;
import ITSecurity.BigInteger.TestFileReader;

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

	@Test
	public void evaluateDifferentPrimeTests() {
		System.out.println("Title; " + "Number; " + "Actual Prime?; " + "Divisible by primes < 100; "
				+ "MR with prime-bases <= 37; " + "Random number; " + "Random Fermat; " + "Random Euler; "
				+ "Random Miller-Rabin; " + "Comment");
		
		BigInt randomBase = BigIntUtils.getRandomOdd(10);
		this.evaluatePrimeFile(randomBase);
		this.evaluateFermatPseudoPrimeFile(randomBase);
		this.evaluateEulerPseudoPrimeFile(randomBase);
	}

	private void evaluateFermatPseudoPrimeFile(BigInt base) {
		TestFileReader reader = new TestFileReader(
				"src/test/java/ITSecurity/BigInteger/test_files/FermatPseudoPrime-Tests.txt");
		List<ImportedTestCase> testCases = reader.read();
		for (ImportedTestCase testCase : testCases) {
			BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			p.fromHexString(testCase.map.get("p"));
			boolean[] results = this.evaluatePrimeTest(p, base);
			this.printTestResults(testCase.title, p.toString(), results, false, base, testCase.map.get("c"));
		}
	}

	private void evaluateEulerPseudoPrimeFile(BigInt base) {
		TestFileReader reader = new TestFileReader(
				"src/test/java/ITSecurity/BigInteger/test_files/EulerPseudoPrime-Tests.txt");
		List<ImportedTestCase> testCases = reader.read();
		for (ImportedTestCase testCase : testCases) {
			BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			p.fromHexString(testCase.map.get("p"));
			boolean[] results = this.evaluatePrimeTest(p, base);
			this.printTestResults(testCase.title, p.toString(), results, false, base, testCase.map.get("c"));
		}
	}

	private void evaluatePrimeFile(BigInt base) {
		TestFileReader reader = new TestFileReader(
				"src/test/java/ITSecurity/BigInteger/test_files/primeNumber-Tests.txt");
		List<ImportedTestCase> testCases = reader.read();
		for (ImportedTestCase testCase : testCases) {
			BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			p.fromHexString(testCase.map.get("p"));
			boolean[] results = this.evaluatePrimeTest(p, base);
			this.printTestResults(testCase.title, p.toString(), results, true, base, "");
		}
	}

	private void printTestResults(String title, String prime, boolean[] results, boolean isActualPrime, BigInt random,
			String comment) {
		System.out.println(title + ";\t" + prime + ";\t" + isActualPrime + ";\t" + results[0] + ";\t" + results[1]
				+ ";\t" + random.toString() + ";\t" + results[2] + ";\t" + results[3] + ";\t" + results[4] + ";\t"
				+ comment);
	}

	private boolean[] evaluatePrimeTest(BigInt p, BigInt random) {
		boolean[] results = new boolean[5];
		// test-divide by all primes < 100 and check modulo
		results[0] = this.isDivisibleByPrimesUnder100(p);
		// do Miller-Rabin for all prime-bases <= 37
		results[1] = this.passesMillerRabinForBasesUpTo37(p);
		// test Fermat with random base
		results[2] = p.isPrimeFermat(random);
		// test Euler with random base
		results[3] = p.isPrimeEuler(random);
		// test Miller-Rabin with random base
		results[4] = p.isPrimeMillerRabin(random);
		return results;
	}

	private boolean isDivisibleByPrimesUnder100(BigInt p) {
		for (int prime : this.primesUpTo100) {
			BigInt clone = p.clone();
			BigInt bigPrime = new BigInt(true, prime, BigInt.DEFAULT_BIG_INT_SIZE);
			if (bigPrime.equals(p)) {
				return false;
			}
			BigInt mod = clone.divMod(bigPrime, true);
			if (mod.spart == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean passesMillerRabinForBasesUpTo37(BigInt p) {
		for (int base : this.primesUpTo37) {
			if (!p.isPrimeMillerRabin(new BigInt(true, base, BigInt.DEFAULT_BIG_INT_SIZE))) {
				return false;
			}
		}
		return true;
	}
}
