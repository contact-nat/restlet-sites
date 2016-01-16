import java.io.IOException;

import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.auth.NTLMEngine;
import org.apache.http.impl.auth.NTLMEngineException;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.restlet.Client;
import org.restlet.ext.httpclient.HttpClientHelper;

/**
 * Extends the client helper of the Apache httpclient extension, provides
 * support of NTLM authentication using the jcif library.
 */
public class MyNtlmHttpClientHelper extends HttpClientHelper {

	/**
	 * Constructor.
	 * 
	 * @param client
	 *            The client connector.
	 */
	public MyNtlmHttpClientHelper(Client client) {
		super(client);
	}

	@Override
	protected void configure(DefaultHttpClient httpClient) {
		super.configure(httpClient);
		// Register the NTLM scheme.
		httpClient.getAuthSchemes().register("ntlm", new AuthSchemeFactory() {
			public AuthScheme newInstance(final HttpParams params) {
				return new NTLMScheme(new NTLMEngine() {
					public String generateType1Msg(String domain,
							String workstation) throws NTLMEngineException {

						Type1Message t1m = new Type1Message(Type1Message
								.getDefaultFlags(), domain, workstation);
						return Base64.encode(t1m.toByteArray());
					}

					public String generateType3Msg(String username,
							String password, String domain, String workstation,
							String challenge) throws NTLMEngineException {
						Type2Message t2m;
						try {
							t2m = new Type2Message(Base64.decode(challenge));
						} catch (IOException ex) {
							throw new NTLMEngineException(
									"Invalid Type2 message", ex);
						}
						Type3Message t3m = new Type3Message(t2m, password,
								domain, username, workstation, 0);
						return Base64.encode(t3m.toByteArray());
					}
				});
			}
		});

		// Provide the credentials
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope("<remote server>", -1),
				new NTCredentials("<login>", "<password>", "<remote server>",
						"<Windows domain>"));
	}

	@Override
	protected void configure(org.apache.http.params.HttpParams params) {
		super.configure(params);
		// Use authentication
		HttpClientParams.setAuthenticating(params, true);
	};

}