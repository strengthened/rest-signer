package com.github.strengthened.signer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.jboss.resteasy.core.interception.jaxrs.ServerWriterInterceptorContext;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class SignerInterceptorTest {

  @Mock
  DetachedContentSigner detachedContentSigner;

  @TestSubject
  SignerInterceptor underTest = new SignerInterceptor();

  @Before
  public void before() {
    underTest.signatureApplier = new SignatureApplier();
  }

  @Test
  public void shouldSignBody() throws Exception {
    WriterInterceptorContext context =
        new WriterInterceptorContextTest(null, null, TO_BE_SIGNED, String.class, null, null, null,
            new MultivaluedMapImpl<String, Object>(), new ByteArrayOutputStream(), null);

    String signature = "signature";
    expect(detachedContentSigner.signEntity(TO_BE_SIGNED.getBytes())).andReturn(signature);

    replay(detachedContentSigner);
    underTest.aroundWriteTo(context);
    verify(detachedContentSigner);

    assertThat(context.getHeaders().get(SignatureApplier.SIGNATURE_HEADER)).contains(signature);
  }

  @Test
  public void shouldNotSignBody() throws Exception {
    WriterInterceptorContext context =
        new WriterInterceptorContextTest(null, null, TO_BE_SIGNED, String.class, null, null, null,
            new MultivaluedMapImpl<String, Object>(), new ByteArrayOutputStream(), null);

    expect(detachedContentSigner.signEntity(TO_BE_SIGNED.getBytes())).andReturn(null);

    replay(detachedContentSigner);
    underTest.aroundWriteTo(context);
    verify(detachedContentSigner);

    assertThat(context.getHeaders().get(SignatureApplier.SIGNATURE_HEADER)).isNull();
  }

  @Test
  public void shouldHandleSignException() throws Exception {
    WriterInterceptorContext context =
        new WriterInterceptorContextTest(null, null, TO_BE_SIGNED, String.class, null, null, null,
            new MultivaluedMapImpl<String, Object>(), new ByteArrayOutputStream(), null);

    expect(detachedContentSigner.signEntity(TO_BE_SIGNED.getBytes()))
        .andThrow(new IllegalStateException());

    replay(detachedContentSigner);
    try {
      underTest.aroundWriteTo(context);
      fail("Should have thrown an exception");
    } catch (Exception ex) {
      verify(detachedContentSigner);
    }
  }

  static class WriterInterceptorContextTest extends ServerWriterInterceptorContext {
    @SuppressWarnings("rawtypes")
    public WriterInterceptorContextTest(WriterInterceptor[] interceptors,
        ResteasyProviderFactory providerFactory, Object entity, Class type, Type genericType,
        Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> headers,
        OutputStream outputStream, HttpRequest request) {
      super(interceptors, providerFactory, entity, type, genericType, annotations, mediaType,
          headers, outputStream, request);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected MessageBodyWriter resolveWriter() {
      return new MessageBodyWriter<String>() {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
          return false;
        }

        @Override
        public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
          return 0;
        }

        @Override
        public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException {
          entityStream.write(t.getBytes());
        }
      };
    }
  }

  static final String TO_BE_SIGNED =
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
          + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, "
          + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
          + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore "
          + "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, "
          + "sunt in culpa qui officia deserunt mollit anim id est laborum.";
  static final String KEY_ID = "lorem-ipsum";
}
