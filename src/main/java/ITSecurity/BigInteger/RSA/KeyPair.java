package ITSecurity.BigInteger.RSA;

import ITSecurity.BigInteger.BigInt;

public class KeyPair {
	private BigInt privateKey;
	private BigInt publicKey;
	private BigInt p;
	private BigInt q;
	public BigInt getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(BigInt privateKey) {
		this.privateKey = privateKey;
	}
	public BigInt getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(BigInt publicKey) {
		this.publicKey = publicKey;
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
	
	
}
