package BSEP.KT2.security.hash;

public interface IHasher {
    public String hash(String entry);
    public String hashSalted(String entry, String salt);
    public String hashHmac(String entry);
}
