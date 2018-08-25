package com.github.strengthened.signer.jose;

import javax.enterprise.inject.Specializes;
import org.jose4j.lang.JoseException;

/**
 * This class is meant to be overridden and specialized {@link Specializes} to change the
 * {@link JoseDetachedContentSigner} configuration.
 */
public class JoseDetachedContentSignerHandler {

  public String handleDetachedContentJws(String detachedContentJws) {
    return detachedContentJws;
  }

  @SuppressWarnings("squid:S1172")
  public String handleJoseException(JoseException ex) {
    return null;
  }
}
