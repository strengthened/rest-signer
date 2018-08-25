package com.github.strengthened.signer.jose;

import javax.enterprise.inject.Specializes;

/**
 * This class is meant to be overridden and specialized {@link Specializes} to change the
 * {@link JoseDetachedContentSigner} configuration.
 */
public class SignInfoExporter {

  public SignInfo produceSignInfo() {
    throw new IllegalStateException("You must @Specializes SignInfoExporter");
  }

}
