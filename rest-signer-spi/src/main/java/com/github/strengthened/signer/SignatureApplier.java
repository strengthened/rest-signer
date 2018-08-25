package com.github.strengthened.signer;

import javax.enterprise.inject.Specializes;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * This class is meant to be overridden and specialized {@link Specializes} to change the
 * {@link SignerInterceptor} configuration.
 */
public class SignatureApplier {

  public static final String SIGNATURE_HEADER = "Strength-Signature";

  public WriterInterceptorContext applySignature(final String signature,
      WriterInterceptorContext context) {
    if (signature != null && !signature.isEmpty()) {
      context.getHeaders().putSingle(SIGNATURE_HEADER, signature);
    }
    return context;
  }

}
