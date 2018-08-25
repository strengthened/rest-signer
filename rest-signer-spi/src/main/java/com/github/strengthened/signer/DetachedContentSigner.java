package com.github.strengthened.signer;

import javax.enterprise.inject.Specializes;

/**
 * This class is meant to be overridden and specialized {@link Specializes} to change the
 * {@link SignerInterceptor} configuration.
 */
public class DetachedContentSigner {

  @SuppressWarnings("squid:S1172")
  public String signEntity(final byte[] entity) {
    return null;
  }

}
