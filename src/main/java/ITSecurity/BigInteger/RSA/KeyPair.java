package ITSecurity.BigInteger.RSA;

import ITSecurity.BigInteger.BigInt;

public class KeyPair {
	private BigInt e;
	private BigInt d;
	private BigInt p;
	private BigInt q;
	private BigInt n;

	public BigInt getE() {
		return e;
	}

	public void setE(BigInt e) {
		this.e = e;
	}

	public BigInt getD() {
		return d;
	}

	public void setD(BigInt d) {
		this.d = d;
	}

	public BigInt getP() {
		return p;
	}

	public void setP(BigInt p) {
		this.p = p;
	}

	public BigInt getQ() {
		return q;
	}

	public void setQ(BigInt q) {
		this.q = q;
	}

	public BigInt getN() {
		return n;
	}

	public void setN(BigInt n) {
		this.n = n;
	}

	public BigInt getPhiN() {
		BigInt one = new BigInt(true, 1, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt phiN = p.clone();
		phiN.sub(one, true);
		BigInt qClone = q.clone();
		qClone.sub(one, true);
		phiN.mul(qClone, true);
		return phiN;
	}

}
