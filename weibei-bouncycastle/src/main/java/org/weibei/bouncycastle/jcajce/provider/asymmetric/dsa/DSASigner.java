package org.weibei.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.DSAKey;
import java.security.spec.AlgorithmParameterSpec;

import org.weibei.bouncycastle.asn1.ASN1Encoding;
import org.weibei.bouncycastle.asn1.ASN1Integer;
import org.weibei.bouncycastle.asn1.ASN1Primitive;
import org.weibei.bouncycastle.asn1.ASN1Sequence;
import org.weibei.bouncycastle.asn1.DERSequence;
import org.weibei.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.weibei.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.weibei.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.weibei.bouncycastle.crypto.CipherParameters;
import org.weibei.bouncycastle.crypto.DSA;
import org.weibei.bouncycastle.crypto.Digest;
import org.weibei.bouncycastle.crypto.digests.NullDigest;
import org.weibei.bouncycastle.crypto.digests.SHA1Digest;
import org.weibei.bouncycastle.crypto.digests.SHA224Digest;
import org.weibei.bouncycastle.crypto.digests.SHA256Digest;
import org.weibei.bouncycastle.crypto.digests.SHA384Digest;
import org.weibei.bouncycastle.crypto.digests.SHA512Digest;
import org.weibei.bouncycastle.crypto.params.ParametersWithRandom;

public class DSASigner
    extends SignatureSpi
    implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest                  digest;
    private DSA                     signer;
    private SecureRandom            random;

    protected DSASigner(
        Digest digest,
        DSA signer)
    {
        this.digest = digest;
        this.signer = signer;
    }

    protected void engineInitVerify(
        PublicKey   publicKey)
        throws InvalidKeyException
    {
        CipherParameters    param;

        if (publicKey instanceof DSAKey)
        {
            param = DSAUtil.generatePublicKeyParameter(publicKey);
        }
        else
        {
            try
            {
                byte[]  bytes = publicKey.getEncoded();

                publicKey = new BCDSAPublicKey(SubjectPublicKeyInfo.getInstance(bytes));

                if (publicKey instanceof DSAKey)
                {
                    param = DSAUtil.generatePublicKeyParameter(publicKey);
                }
                else
                {
                    throw new InvalidKeyException("can't recognise key type in DSA based signer");
                }
            }
            catch (Exception e)
            {
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
        }

        digest.reset();
        signer.init(false, param);
    }

    protected void engineInitSign(
        PrivateKey      privateKey,
        SecureRandom    random)
        throws InvalidKeyException
    {
        this.random = random;
        engineInitSign(privateKey);
    }

    protected void engineInitSign(
        PrivateKey  privateKey)
        throws InvalidKeyException
    {
        CipherParameters    param;

        param = DSAUtil.generatePrivateKeyParameter(privateKey);

        if (random != null)
        {
            param = new ParametersWithRandom(param, random);
        }

        digest.reset();
        signer.init(true, param);
    }

    protected void engineUpdate(
        byte    b)
        throws SignatureException
    {
        digest.update(b);
    }

    protected void engineUpdate(
        byte[]  b,
        int     off,
        int     len) 
        throws SignatureException
    {
        digest.update(b, off, len);
    }

    protected byte[] engineSign()
        throws SignatureException
    {
        byte[]  hash = new byte[digest.getDigestSize()];

        digest.doFinal(hash, 0);

        try
        {
            BigInteger[]    sig = signer.generateSignature(hash);

            return derEncode(sig[0], sig[1]);
        }
        catch (Exception e)
        {
            throw new SignatureException(e.toString());
        }
    }

    protected boolean engineVerify(
        byte[]  sigBytes) 
        throws SignatureException
    {
        byte[]  hash = new byte[digest.getDigestSize()];

        digest.doFinal(hash, 0);

        BigInteger[]    sig;

        try
        {
            sig = derDecode(sigBytes);
        }
        catch (Exception e)
        {
            throw new SignatureException("error decoding signature bytes.");
        }

        return signer.verifySignature(hash, sig[0], sig[1]);
    }

    protected void engineSetParameter(
        AlgorithmParameterSpec params)
    {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    /**
     * @deprecated replaced with <a href = "#engineSetParameter(java.security.spec.AlgorithmParameterSpec)">
     */
    protected void engineSetParameter(
        String  param,
        Object  value)
    {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    /**
     * @deprecated
     */
    protected Object engineGetParameter(
        String      param)
    {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    private byte[] derEncode(
        BigInteger  r,
        BigInteger  s)
        throws IOException
    {
        ASN1Integer[] rs = new ASN1Integer[]{ new ASN1Integer(r), new ASN1Integer(s) };
        return new DERSequence(rs).getEncoded(ASN1Encoding.DER);
    }

    private BigInteger[] derDecode(
        byte[]  encoding)
        throws IOException
    {
        ASN1Sequence s = (ASN1Sequence)ASN1Primitive.fromByteArray(encoding);
        return new BigInteger[]{
            ((ASN1Integer)s.getObjectAt(0)).getValue(),
            ((ASN1Integer)s.getObjectAt(1)).getValue()
        };
    }

    static public class stdDSA
        extends DSASigner
    {
        public stdDSA()
        {
            super(new SHA1Digest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }

    static public class dsa224
        extends DSASigner
    {
        public dsa224()
        {
            super(new SHA224Digest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }
    
    static public class dsa256
        extends DSASigner
    {
        public dsa256()
        {
            super(new SHA256Digest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }
    
    static public class dsa384
        extends DSASigner
    {
        public dsa384()
        {
            super(new SHA384Digest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }
    
    static public class dsa512
        extends DSASigner
    {
        public dsa512()
        {
            super(new SHA512Digest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }

    static public class noneDSA
        extends DSASigner
    {
        public noneDSA()
        {
            super(new NullDigest(), new org.weibei.bouncycastle.crypto.signers.DSASigner());
        }
    }
}
