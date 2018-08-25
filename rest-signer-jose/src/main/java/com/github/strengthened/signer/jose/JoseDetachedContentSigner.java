package com.github.strengthened.signer.jose;

import com.github.strengthened.signer.DetachedContentSigner;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.lang.JoseException;

@Specializes
public class JoseDetachedContentSigner extends DetachedContentSigner {

  @Inject
  SignInfoExporter signInfoExporter;

  @Inject
  JoseDetachedContentSignerHandler handler;

  @Override
  public String signEntity(byte[] entity) {
    try {
      SignInfo signInfo = signInfoExporter.produceSignInfo();
      JsonWebSignature signerJws = new JsonWebSignature();
      signerJws.setPayloadBytes(entity);
      signerJws.setAlgorithmHeaderValue(signInfo.algorithmHeaderValue());
      signerJws.setKey(signInfo.key());
      if (signInfo.keyIdHeaderValue() != null) {
        signerJws.setKeyIdHeaderValue(signInfo.keyIdHeaderValue());
      }
      signerJws.getHeaders().setObjectHeaderValue(HeaderParameterNames.BASE64URL_ENCODE_PAYLOAD,
          false);
      signerJws.setCriticalHeaderNames(HeaderParameterNames.BASE64URL_ENCODE_PAYLOAD);
      return handler.handleDetachedContentJws(signerJws.getDetachedContentCompactSerialization());
    } catch (JoseException ex) {
      return handler.handleJoseException(ex);
    }
  }
}
