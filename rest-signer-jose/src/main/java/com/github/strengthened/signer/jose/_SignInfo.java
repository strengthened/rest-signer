package com.github.strengthened.signer.jose;

import java.security.Key;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Value.Immutable
@Value.Style(visibility = ImplementationVisibility.PUBLIC, typeAbstract = "_*", typeImmutable = "*")
@SuppressWarnings("squid:S00114")
interface _SignInfo {

  String algorithmHeaderValue();

  Key key();

  @Nullable
  String keyIdHeaderValue();

}
