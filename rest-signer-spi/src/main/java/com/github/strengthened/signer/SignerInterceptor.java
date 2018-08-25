package com.github.strengthened.signer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
public class SignerInterceptor implements WriterInterceptor {

  @Inject
  DetachedContentSigner detachedContentSigner;

  @Inject
  SignatureApplier signatureApplier;

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException {
    final OutputStream old = context.getOutputStream();
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    try {
      context.setOutputStream(buffer);
      context.proceed();
      final byte[] entity = buffer.toByteArray();

      final String signature = detachedContentSigner.signEntity(entity);
      signatureApplier.applySignature(signature, context);

      old.write(entity);
    } finally {
      context.setOutputStream(old);
    }
  }
}
