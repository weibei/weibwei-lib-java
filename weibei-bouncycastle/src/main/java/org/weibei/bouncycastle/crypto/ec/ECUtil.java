package org.weibei.bouncycastle.crypto.ec;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.weibei.bouncycastle.math.ec.ECConstants;

class ECUtil
{
    static BigInteger generateK(BigInteger n, SecureRandom random)
    {
        int                    nBitLength = n.bitLength();
        BigInteger             k = new BigInteger(nBitLength, random);

        while (k.equals(ECConstants.ZERO) || (k.compareTo(n) >= 0))
        {
            k = new BigInteger(nBitLength, random);
        }

        return k;
    }
}
