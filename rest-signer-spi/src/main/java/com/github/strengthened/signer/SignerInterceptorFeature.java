package com.github.strengthened.signer;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class SignerInterceptorFeature implements DynamicFeature {

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    if (!resourceInfo.getResourceClass().isAnnotationPresent(DisableSigner.class)
        && !resourceInfo.getResourceMethod().isAnnotationPresent(DisableSigner.class)) {
      context.register(SignerInterceptor.class);
    }
  }

}
