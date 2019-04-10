package ITSecurity.BigInteger;

public final class BigNumberUtils {
	
	// expands the big numbers so they match
	public static void sameSize(BigNumber a, BigNumber b) {
		if (a.spart > b.spart) {
			b.resize(a.spart);
		} else if (a.spart < b.spart) {
			a.resize(b.spart);
		}
	}
	
}
