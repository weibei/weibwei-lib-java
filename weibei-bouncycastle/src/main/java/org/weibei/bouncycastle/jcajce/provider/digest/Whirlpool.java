package org.weibei.bouncycastle.jcajce.provider.digest;

import org.weibei.bouncycastle.crypto.CipherKeyGenerator;
import org.weibei.bouncycastle.crypto.digests.WhirlpoolDigest;
import org.weibei.bouncycastle.crypto.macs.HMac;
import org.weibei.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.weibei.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.weibei.bouncycastle.jcajce.provider.symmetric.util.BaseMac;

public class Whirlpool
{
    private Whirlpool()
    {

    }

    static public class Digest
        extends BCMessageDigest
        implements Cloneable
    {
        public Digest()
        {
            super(new WhirlpoolDigest());
        }

        public Object clone()
            throws CloneNotSupportedException
        {
            Digest d = (Digest)super.clone();
            d.digest = new WhirlpoolDigest((WhirlpoolDigest)digest);

            return d;
        }
    }

    /**
     * Tiger HMac
     */
    public static class HashMac
        extends BaseMac
    {
        public HashMac()
        {
            super(new HMac(new WhirlpoolDigest()));
        }
    }

    public static class KeyGenerator
        extends BaseKeyGenerator
    {
        public KeyGenerator()
        {
            super("HMACWHIRLPOOL", 512, new CipherKeyGenerator());
        }
    }

    public static class Mappings
        extends DigestAlgorithmProvider
    {
        private static final String PREFIX = Whirlpool.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            provider.addAlgorithm("MessageDigest.WHIRLPOOL", PREFIX + "$Digest");

            addHMACAlgorithm(provider, "WHIRLPOOL", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }
}
