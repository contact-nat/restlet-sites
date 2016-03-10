/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web;

import java.io.IOException;
import java.util.Properties;

import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Redirector;
import org.restlet.routing.VirtualHost;

import com.restlet.frontend.web.applications.MavenRestletOrg;
import com.restlet.frontend.web.applications.P2RestletOrg;
import com.restlet.frontend.web.applications.RestletCom;
import com.restlet.frontend.web.applications.StudioRestletCom;

/**
 * The web component managing the Restlet web servers.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class WebComponent extends Component {
	/**
	 * Returns a Properties instance loaded from the given URI.
	 * 
	 * @param propertiesUri
	 *            The URI of the properties file.
	 * @return A Properties instance loaded from the given URI.
	 * @throws IOException
	 */
	public static Properties getProperties(String propertiesUri)
			throws IOException {
		ClientResource resource = new ClientResource(propertiesUri);
		try {
			Representation rep = resource.get();

			Properties properties = new Properties();
			properties.load(rep.getStream());
			return properties;
		} catch (Exception e) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Cannot access to the configuration file: \"");
			stringBuilder.append(propertiesUri);
			stringBuilder.append("\"");
			throw new IllegalArgumentException(stringBuilder.toString());
		}

	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            Program arguments.
	 */
	public static void main(String[] args) {
		try {
			// Create and start the server
			new WebComponent().start();
		} catch (Exception e) {
			System.err
					.println("Can't launch the web server.\nAn unexpected exception occurred:");
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Constructor.
	 */
	public WebComponent() throws Exception {
		super();
		
		getLogService().setLoggerName("com.noelios.web.WebComponent.www");
		// getLogService().setIdentityCheck(true);

		final Properties properties = getProperties("clap://class/webComponent.properties");

		// IP address to listen on
		String ipAddress = properties.getProperty("server.address");
		// Port to listen on
		int port = Integer.parseInt(properties.getProperty("server.http.port"));

		// Path to the truststore.
		String truststorePath = properties.getProperty("truststore.path");
		if (truststorePath != null) {
			System.setProperty("javax.net.ssl.trustStore", truststorePath);
		}

		// ------------------
		// Add the connectors
		// ------------------
		Server server = getServers().add(Protocol.HTTP, ipAddress, port);
		server.getContext().getParameters().add("useForwardedForHeader", "true");
		
		getClients().add(Protocol.CLAP);
		getClients().add(Protocol.FILE);
		getClients().add(Protocol.RIAP);
		getClients().add(Protocol.HTTP);
		getClients().add(Protocol.HTTPS);

		// -----------
		// restlet.org
		// -----------
		// handle redirection to restlet.com site.
		VirtualHost host = addHost("restlet.org", port, new Redirector(
				getContext().createChildContext(), null,
				getFrameworkSiteRedirectionMode(properties)) {
			@Override
			protected Reference getTargetRef(Request request, Response response) {
				Reference ref = new Reference(request.getResourceRef());
				ref.setHostDomain(getHostDomain("restlet.com", properties));
				return ref;
			}
		}, properties);
		getHosts().add(host);
		// ---------------
		// restlet.com
		// ---------------
		host = addHost("restlet.com", port, new RestletCom(
				"clap://class/restletCom.properties"), properties);
		getHosts().add(host);
		
		// -----------------
		// stage.restlet.com
		// -----------------
		host = addHost("stage.restlet.com", port, new RestletCom(
				"clap://class/stageRestletCom.properties"), properties);
		getHosts().add(host);
		
		// -----------------
		// studio.restlet.com
		// -----------------
		host = addHost("studio.restlet.com", port, new StudioRestletCom(
				"clap://class/studioRestletCom.properties"), properties);
		getHosts().add(host);

		// -----------------
		// maven.restlet.org
		// -----------------
		host = addHost("maven.restlet.org", port, new Redirector(getContext()
				.createChildContext(), null,
				getFrameworkSiteRedirectionMode(properties)) {
			@Override
			protected Reference getTargetRef(Request request, Response response) {
				Reference ref = new Reference(request.getResourceRef());
				ref.setHostDomain(getHostDomain("maven.restlet.com", properties));
				return ref;
			}
		}, properties);
		getHosts().add(host);
		host = addHost("maven.restlet.com", port, new MavenRestletOrg(
				"clap://class/mavenRestletOrg.properties"), properties);
		getHosts().add(host);
		// -----------------
		// p2.restlet.org
		// -----------------
		host = addHost("p2.restlet.org", port, new Redirector(getContext()
				.createChildContext(), null,
				getFrameworkSiteRedirectionMode(properties)) {
			@Override
			protected Reference getTargetRef(Request request, Response response) {
				Reference ref = new Reference(request.getResourceRef());
				ref.setHostDomain(getHostDomain("p2.restlet.com", properties));
				return ref;
			}
		}, properties);
		getHosts().add(host);
		host = addHost("p2.restlet.com", port, new P2RestletOrg(
				"clap://class/p2RestletOrg.properties"), properties);
		getHosts().add(host);

		// -----------------------
		// Redirect to restlet.org
		// -----------------------
		host = addRedirection("www.restlet.org", port,
				"http://restlet.org{rr}", properties);
		getHosts().add(host);
		// -----------------------
		// Redirect to restlet.com
		// -----------------------
		host = addRedirection("www.restlet.com", port,
				"http://restlet.com{rr}", properties);
		getHosts().add(host);
		// -----------------------
		// Redirect to restlet.org
		// -----------------------
		host = addRedirection(
				"search.restlet.org|search.onrest.org|book.restlet.org|onrest.org|www.onrest.org",
				port, "http://restlet.org/", properties);
		getHosts().add(host);
		// ------------------------------
		// Redirect to restlet.tigris.org
		// ------------------------------
		host = addRedirection("restlet.net", port,
				"http://restlet.tigris.org{rr}",
				Redirector.MODE_CLIENT_TEMPORARY, properties);
		getHosts().add(host);
		host.attach("/fisheye/", new Redirector(null,
				"http://restlet.net/source/browse/restlet/",
				Redirector.MODE_CLIENT_PERMANENT));
		getLogger()
				.info(host.getHostDomain()
						+ "/fisheye/ redirected to \"http://restlet.tigris.org{rr}\" on port "
						+ host.getHostPort());
		// -----------------------
		// Redirect to restlet.net
		// -----------------------
		host = addRedirection("www.restlet.net", port,
				"http://restlet.net{rr}", properties);
		getHosts().add(host);
		// ----------------------------
		// Redirect to blog.restlet.com
		// ----------------------------
		host = addRedirection("blog.noelios.com", port,
				"http://blog.restlet.com{rr}", properties);
		getHosts().add(host);
		// ---------------------------
		// Redirect to restlet.com
		// ---------------------------
		host = addRedirection("noelios.com|noelios.net|noelios.org|"
				+ "www.noelios.com|www.noelios.net|www.noelios.org", port,
				"http://restlet.com{rr}", properties);

		// ----------------------------
		// Redirect to restlet.com/blog
		// ----------------------------
		host = addRedirection("blog.restlet.com", port,
				"http://restlet.com/blog{rr}", properties);

		getHosts().add(host);

        // ----------------------------
        // Redirect summer-of-apis.restlet.com to restlet.com/summer-of-apis
        // ----------------------------
        host = addRedirection("summer-of-apis.restlet.com", port,
                "http://restlet.com/summer-of-apis{rr}", properties);

        getHosts().add(host);
        // ----------------------------
        // Redirect summerofapis.restlet.com to restlet.com/summer-of-apis
        // ----------------------------
        host = addRedirection("summerofapis.restlet.com", port,
                "http://restlet.com/summer-of-apis{rr}", properties);

        getHosts().add(host);
	}

	/**
	 * Defines a new virtual host.
	 * 
	 * @param host
	 *            The host domain.
	 * @param port
	 *            The port to listen to.
	 * @param application
	 *            The application.
	 * @param properties
	 *            The component's set of properties.
	 * @return A new virtual host.
	 */
	private VirtualHost addHost(String host, int port, Restlet restlet,
			Properties properties) {
		VirtualHost result = new VirtualHost(getContext().createChildContext());
		setHostDomain(result, host, properties);
		result.setHostPort("80|" + Integer.toString(port));
		result.attach(restlet);
		result.setName(host);
		getLogger().info(
				result.getHostDomain() + " listens to port "
						+ result.getHostPort());
		return result;
	}

	/**
	 * Defines a new host for a redirection.
	 * 
	 * @param host
	 *            The host domain.
	 * @param port
	 *            The port to listen to.
	 * @param redirection
	 *            The redirection.
	 * @param mode
	 *            The redirection mode.
	 * @param properties
	 *            The component's set of properties.
	 * @return A new virtual host.
	 */
	private VirtualHost addRedirection(String host, int port,
			String redirection, int mode, Properties properties) {
		VirtualHost result = new VirtualHost(getContext().createChildContext());
		setHostDomain(result, host, properties);
		result.setName(host);
		result.setHostPort("80|" + Integer.toString(port));
		result.attach(new Redirector(null, redirection, mode));
		getLogger().info(
				result.getHostDomain() + " redirected to \"" + redirection
						+ "\" on port " + result.getHostPort());

		return result;
	}

	/**
	 * Defines a new host for a redirection (in mode
	 * {@link Redirector.MODE_CLIENT_PERMANENT}).
	 * 
	 * @param host
	 *            The host domain.
	 * @param port
	 *            The port to listen to.
	 * @param redirection
	 *            The redirection.
	 * @param properties
	 *            The component's set of properties.
	 * @return A new virtual host.
	 */
	private VirtualHost addRedirection(String host, int port,
			String redirection, Properties properties) {
		return addRedirection(host, port, redirection,
				Redirector.MODE_CLIENT_PERMANENT, properties);
	}

	/**
	 * Returns the redirection mode for the framework site.
	 * 
	 * @param properties
	 *            The current properties.
	 * @return The redirection mode for the framework site.
	 */
	private int getFrameworkSiteRedirectionMode(Properties properties) {
		String r = properties.getProperty("framework.site.redirection",
				"MODE_CLIENT_TEMPORARY");

		if ("MODE_CLIENT_FOUND".equals(r)) {
			return Redirector.MODE_CLIENT_FOUND;
		} else if ("MODE_CLIENT_PERMANENT".equals(r)) {
			return Redirector.MODE_CLIENT_PERMANENT;
		} else if ("MODE_CLIENT_SEE_OTHER".equals(r)) {
			return Redirector.MODE_CLIENT_SEE_OTHER;
		}

		return Redirector.MODE_CLIENT_TEMPORARY;

	}

	/**
	 * Returns the host's domain. Could be customized by the "domain.host"
	 * property.
	 * 
	 * @param host
	 *            The {@link VirtualHost} to update.
	 * @param domain
	 *            The domain name.
	 * @param properties
	 *            The properties where to find the facultative customized domain
	 *            name.
	 */
	private String getHostDomain(String domain, Properties properties) {
		return properties.getProperty(domain + ".host", domain);
	}

	/**
	 * Sets the host's domain. Could be customized by the "domain.host"
	 * property.
	 * 
	 * @param host
	 *            The {@link VirtualHost} to update.
	 * @param domain
	 *            The domain name.
	 * @param properties
	 *            The properties where to find the facultative customized domain
	 *            name.
	 */
	private void setHostDomain(VirtualHost host, String domain,
			Properties properties) {
		host.setHostDomain(getHostDomain(domain, properties));
		getLogger().info(domain + " swapped to " + host.getHostDomain());
	}

}