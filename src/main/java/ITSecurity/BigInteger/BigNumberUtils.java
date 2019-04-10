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

	public static String padWithZeros(String str, int length) {
		int padCount = length - str.length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < padCount; i++) {
			builder.append('0');
		}
		builder.append(str);
		return builder.toString();
	}

}
