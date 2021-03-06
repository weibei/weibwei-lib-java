package org.weibei.bouncycastle.asn1.crmf;

import org.weibei.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.weibei.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CRMFObjectIdentifiers
{
    static final ASN1ObjectIdentifier id_pkix = new ASN1ObjectIdentifier("1.3.6.1.5.5.7");

    // arc for Internet X.509 PKI protocols and their components

    static final ASN1ObjectIdentifier id_pkip  = id_pkix.branch("5");

    static final ASN1ObjectIdentifier id_regCtrl = id_pkip.branch("1");
    static final ASN1ObjectIdentifier id_regCtrl_regToken = id_regCtrl.branch("1");
    static final ASN1ObjectIdentifier id_regCtrl_authenticator = id_regCtrl.branch("2");
    static final ASN1ObjectIdentifier id_regCtrl_pkiPublicationInfo = id_regCtrl.branch("3");
    static final ASN1ObjectIdentifier id_regCtrl_pkiArchiveOptions = id_regCtrl.branch("4");

    static final ASN1ObjectIdentifier id_ct_encKeyWithID = new ASN1ObjectIdentifier(PKCSObjectIdentifiers.id_ct + ".21");
}
