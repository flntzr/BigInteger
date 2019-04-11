package ITSecurity.BigInteger;

public final class BigIntUtils {
	public static BigInt add(BigInt a, BigInt b) {
		BigInt c;
		if (a.positive && b.positive) {
			c = a.clone();
			c.add(b, true);
		} else if (!a.positive && b.positive) {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, true);
			} else {
				c = a.clone();
				c.sub(b, false);
			}
		} else if (a.positive && !b.positive) {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, false);
			} else {
				c = a.clone();
				c.sub(b, true);
			}
		} else {
			c = a.clone();
			c.add(b, false);
		}
		return c;
	}
}
