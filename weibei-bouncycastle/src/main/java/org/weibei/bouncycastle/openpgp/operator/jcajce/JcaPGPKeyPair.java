package org.weibei.bouncycastle.openpgp.operator.jcajce;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.weibei.bouncycastle.openpgp.PGPException;
import org.weibei.bouncycastle.openpgp.PGPKeyPair;
import org.weibei.bouncycastle.openpgp.PGPPrivateKey;
import org.weibei.bouncycastle.openpgp.PGPPublicKey;

public class JcaPGPKeyPair
    extends PGPKeyPair
{
    private static PGPPublicKey getPublicKey(int algorithm, PublicKey pubKey, Date date)
        throws PGPException
    {
        return  new JcaPGPKeyConverter().getPGPPublicKey(algorithm, pubKey, date);
    }

    private static PGPPrivateKey getPrivateKey(PGPPublicKey pub, PrivateKey privKey)
        throws PGPException
    {
        return new JcaPGPKeyConverter().getPGPPrivateKey(pub, privKey);
    }

    public JcaPGPKeyPair(int algorithm, KeyPair keyPair, Date date)
        throws PGPException
    {
        this.pub = getPublicKey(algorithm, keyPair.getPublic(), date);
        this.priv = getPrivateKey(this.pub, keyPair.getPrivate());
    }
}
