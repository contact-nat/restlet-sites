/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.Engine;
import org.restlet.engine.application.Encoder;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.ext.atom.Entry;
import org.restlet.ext.atom.Feed;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Directory;
import org.restlet.routing.Filter;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.util.Series;

import com.restlet.frontend.objects.framework.Distribution;
import com.restlet.frontend.objects.framework.DistributionsList;
import com.restlet.frontend.objects.framework.Edition;
import com.restlet.frontend.objects.framework.EditionsList;
import com.restlet.frontend.objects.framework.Qualifier;
import com.restlet.frontend.objects.framework.QualifiersList;
import com.restlet.frontend.objects.framework.Version;
import com.restlet.frontend.objects.framework.VersionsList;
import com.restlet.frontend.web.resources.DistributionsResource;
import com.restlet.frontend.web.resources.DownloadCurrentServerResource;
import com.restlet.frontend.web.resources.DownloadPastServerResource;
import com.restlet.frontend.web.resources.EditionsResource;
import com.restlet.frontend.web.resources.FeedGeneralResource;
import com.restlet.frontend.web.resources.FeedReleasesResource;
import com.restlet.frontend.web.resources.FeedSummaryResource;
import com.restlet.frontend.web.resources.QualifiersResource;
import com.restlet.frontend.web.resources.RestletComRefreshResource;
import com.restlet.frontend.web.resources.VersionsResource;
import com.restlet.frontend.web.services.RefreshStatusService;

import freemarker.template.Configuration;

/**
 * Application for the http://restlet.com site.
 * 
 * @author Jerome Louvel
 */
public class RestletCom extends BaseApplication implements RefreshApplication {

	/**
	 * {@link TemplateRoute} that scores URIs according to a regex pattern. Once
	 * chosen, the URI is transmitted to the next Restlet unchanged, whereas a
	 * classic {@link TemplateRoute} adjusts the base reference according to the
	 * matched par of the URI.
	 * 
	 * @author Thierry Boileau
	 * 
	 */
	private static class StartsWithRoute extends TemplateRoute {
		/** The pattern to use for formatting or parsing. */
		private volatile String pattern;

		/** The internal Regex pattern. */
		private volatile Pattern regexPattern;

		/**
		 * Constructor.
		 * 
		 * @param router
		 *            the router.
		 * @param next
		 *            the Restlet to transmit the Request to.
		 * @param pattern
		 *            The regex pattern to match.
		 */
		public StartsWithRoute(Router router, Restlet next, String pattern) {
			super(next);
			setRouter(router);
			this.pattern = pattern;
			this.regexPattern = Pattern.compile(this.pattern.toString());
		}

		@Override
		public float score(Request request, Response response) {
			float result = -1f;
			String remainingPart = request.getResourceRef().getRemainingPart(
					false, isMatchingQuery());
			if (remainingPart != null) {
				Matcher matcher = regexPattern.matcher(remainingPart);
				if (matcher.lookingAt()) {
					result = matcher.end();
				}
			}
			return result;
		}
	}

	/** The list of defined branches. */
	private Set<String> branches;

	/** The data file URI. */
	private String dataUri;

	/** List of current distributions. */
	private DistributionsList distributions;

	private Map<String, DistributionsList> distributionsByVersion;

	private Map<String, DistributionsList> distributionsByVersionEdition;

	/** List of current editions. */
	private EditionsList editions;

	/** List of current Restlet feeds. */
	private List<Entry> feedGeneral;

	/** URI of the general feed. */
	private final String feedGeneralAtomUri;

	/** List of current Restlet feeds. */
	private List<Entry> feedReleases;

	/** URI of the releases feed. */
	private final String feedReleasesAtomUri;

	/** List of current Restlet feeds. */
	private List<Entry> feedSummary;

	/** Freemarker configuration object */
	private Configuration fmc;

	/** Login for admin protected pages. */
	private String login;

	/** Password for admin protected pages. */
	private char[] password;

	/** List of current editions. */
	private QualifiersList qualifiers;

	/** List of current qualifiers. */
	private Map<String, Qualifier> qualifiersMap;

	/** The URI of the router properties file. */
	private String routerPropertiesFileReference;

	/** The root router. */
	private Router rootRouter;

	/** Login for global site authentication. */
	private String siteLogin;

	/** Password for global site authentication. */
	private char[] sitePassword;

	private Map<String, String> toBranch;

	/** List of current versions. */
	private VersionsList versions;

	/** List of current versions. */
	private Map<String, Version> versionsMap;

	/** The Web files root directory URI. */
	private String wwwUri;

	/**
	 * Constructor.
	 * 
	 * @param propertiesFileReference
	 *            The Reference to the application's properties file.
	 * @throws IOException
	 */
	public RestletCom(String propertiesFileReference) throws IOException {
		super(propertiesFileReference);

		// By default, check the classpath.
		this.routerPropertiesFileReference = getProperty("router.uri",
				"clap://class/router.properties");

		this.setStatusService(new RefreshStatusService(true, this));

		this.dataUri = getProperty("data.uri");
		this.wwwUri = getProperty("www.uri");
		this.login = getProperty("admin.login");

		String str = getProperty("admin.password");
		if (str != null) {
			this.password = str.toCharArray();
		}
		this.siteLogin = getProperty("site.login");
		str = getProperty("site.password");
		if (str != null) {
			sitePassword = str.toCharArray();
		}

		this.feedGeneralAtomUri = getProperty("feed.restlet.general.atom");
		this.feedReleasesAtomUri = getProperty("feed.restlet.releases.atom");

		// Turn off extension tunnelling because of redirections.
		this.getTunnelService().setExtensionsTunnel(false);

		// Override the default mediatype for XSD
		getMetadataService().addExtension("xsd", MediaType.APPLICATION_XML,
				true);

		this.fmc = new Configuration();
		try {
			this.fmc.setDirectoryForTemplateLoading(new File(
					new LocalReference(this.wwwUri).getFile(), ""));
		} catch (IOException e) {
			getLogger()
					.warning(
							"Cannot set Freemarker templates directory: "
									+ this.wwwUri);
		}

		qualifiers = new QualifiersList();
		versions = new VersionsList();
		distributions = new DistributionsList();
		editions = new EditionsList();
		branches = new HashSet<String>();
		toBranch = new ConcurrentHashMap<String, String>();
		qualifiersMap = new HashMap<String, Qualifier>();
		versionsMap = new HashMap<String, Version>();
		distributionsByVersion = new HashMap<String, DistributionsList>();
		distributionsByVersionEdition = new HashMap<String, DistributionsList>();
		getConnectorService().getClientProtocols().add(Protocol.CLAP);
		getConnectorService().getClientProtocols().add(Protocol.HTTP);
		getConnectorService().getClientProtocols().add(Protocol.FILE);
	}

    private static final class HttpRedirectFilter extends Filter {
        private HttpRedirectFilter(Context context) {
            super(context);
        }

        @Override
        protected int beforeHandle(Request request, Response response) {
            if (request.getResourceRef().getPath().startsWith("/blog")) {		
                 // issue #134 : routes all proxied HTTP urls to HTTPS.		
                 Series<Header> headers = (Series<Header>) request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);		
                 if (headers ==  null) {		
                 	headers = new Series<>(Header.class);		
                 }		
                 Protocol protocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));		
                 if (protocol != null) {		
                     if (Protocol.HTTPS.equals(protocol)) {		
                         response.redirectTemporary(request.getResourceRef());		
                         response.getLocationRef().setProtocol(Protocol.HTTP);		
                        return Filter.STOP;		
                    }		
 		
                 }		
                     return super.beforeHandle(request, response);		
             }

            // issue #134 : routes all proxied HTTP urls to HTTPS.
            Series<Header> headers = (Series<Header>) request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            if (headers ==  null) {
            	headers = new Series<>(Header.class);
            }
            Protocol protocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));
            if (protocol != null) {
                request.getHostRef().setProtocol(Protocol.HTTPS);
                request.getResourceRef().setProtocol(Protocol.HTTPS);
                if (request.getResourceRef().getBaseRef() != null) {
                    request.getResourceRef().getBaseRef()
                            .setProtocol(Protocol.HTTPS);
                }
                request.getRootRef().setProtocol(Protocol.HTTPS);
                if (Protocol.HTTP.equals(protocol)) {
                    response.redirectPermanent(request.getResourceRef());
                    response.getLocationRef().setProtocol(Protocol.HTTPS);
                    return Filter.STOP;
                }
            }
            return super.beforeHandle(request, response);
        }
    }
    
	@Override
	public Restlet createInboundRoot() {
		Engine.setLogLevel(Level.FINEST);
		// Create a root router
		rootRouter = new Router(getContext());
		rootRouter.setDefaultMatchingMode(Router.MODE_FIRST_MATCH);
		updateRootRouter();

		HttpRedirectFilter redirectFilter = new HttpRedirectFilter(getContext());

		if (siteLogin != null && sitePassword != null) {
			ChallengeAuthenticator ca = new ChallengeAuthenticator(
					getContext(), ChallengeScheme.HTTP_BASIC, "realm");
			MapVerifier mv = new MapVerifier();
			mv.getLocalSecrets().put(siteLogin, sitePassword);
			mv.getLocalSecrets().put(login, password);
			ca.setVerifier(mv);
			ca.setNext(rootRouter);
			redirectFilter.setNext(ca);
		} else {
			redirectFilter.setNext(rootRouter);
		}

		Encoder encoder = new Encoder(getContext(), false, true,
				getEncoderService());
		encoder.setNext(redirectFilter);

		return encoder;
	}

	public String getDataUri() {
		return this.dataUri;
	}

	public Distribution getDistribution(Form query, Series<Cookie> cookies,
			Version version, Edition edition) {
		DistributionsList dl = distributionsByVersionEdition.get(version
				.getId() + "|" + edition.getId());
		// looking for the distribution
		Distribution distribution = getDistribution(
				query.getFirstValue("distribution"), dl);
		if (distribution == null) {
			distribution = getDistribution(
					cookies.getFirstValue("distribution"), dl);
		}
		if (distribution == null) {
			// Or set default value
			for (Distribution d : dl) {
				if ("zip".equals(d.getFileType())) {
					distribution = d;
					break;
				}
			}
		}
		return distribution;
	}

	private Distribution getDistribution(String id, DistributionsList dl) {
		Distribution result = null;
		if (id != null) {
			for (Distribution d : dl) {
				if (id.equals(d.getFileType())) {
					result = d;
					break;
				}
			}
		}

		return result;
	}

	public Edition getEdition(Form query, Series<Cookie> cookies,
			Version version) {
		// looking for the edition
		DistributionsList dl = distributionsByVersion.get(version.getId());
		Edition edition = getEdition(query.getFirstValue("edition"), dl);
		if (edition == null) {
			edition = getEdition(cookies.getFirstValue("edition"), dl);
		}

		if (edition == null) {
			// Or set default value
			for (Distribution d : dl) {
				if ("jse".equals(d.getEdition())
						|| "all".equals(d.getEdition())) {
					for (Edition e : editions) {
						if (e.getId().equals(d.getEdition())) {
							edition = e;
							break;
						}
					}
					break;
				}
			}
		}
		return edition;
	}

	private Edition getEdition(String id, DistributionsList dl) {
		Edition result = null;
		if (id != null) {
			for (Edition e : editions) {
				if (id.equals(e.getId())) {
					// check also if this edition exists for the given version
					for (Distribution d : dl) {
						if (d.getEdition().equals(e.getId())) {
							result = e;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	public List<Entry> getFeedGeneral() {
		return feedGeneral;
	}

	public List<Entry> getFeedReleases() {
		return feedReleases;
	}

	public List<Entry> getFeedSummary() {
		return feedSummary;
	}

	public Configuration getFmc() {
		return this.fmc;
	}

	@Override
	public String getName() {
		return "Application for restlet.com";
	}

	public Version getVersion(Form query, Series<Cookie> cookies) {
		Version version = getVersion(query.getFirstValue("release"));
		if (version == null) {
			// check the cookies
			version = getVersion(cookies.getFirstValue("release"));
		}
		if (version == null) {
			// or set the default value
			version = versionsMap.get(qualifiersMap.get("stable").getVersion());
		}
		return version;
	}

	private Version getVersion(String id) {
		// looking for the qualified version, if any
		if (qualifiersMap.containsKey(id)) {
			id = qualifiersMap.get(id).getVersion();
		}
		return versionsMap.get(id);
	}

	public String getWwwUri() {
		return this.wwwUri;
	}

	/**
	 * Helps to define redirections assuming that the router defines route by
	 * using the {@link Template.MODE_STARTS_WITH} mode.
	 * 
	 * @param router
	 *            The router where to define the redirection.
	 * @param from
	 *            The source template.
	 * @param to
	 *            The target template.
	 * @return The defined route.
	 */
	private TemplateRoute redirectBranch(Router router, String from, String to,
			final String qualifier) {
		// temporary redirection
		TemplateRoute route = router.attach(from, new Redirector(getContext(),
				to, Redirector.MODE_CLIENT_TEMPORARY) {
			@Override
			protected Reference getTargetRef(Request request, Response response) {
				String b = (qualifier != null) ? toBranch.get(qualifier) : null;
				if (b == null) {
					// induce it from the request's cookies
					b = request.getCookies().getFirstValue("branch", "");
				}
				if (b == null || b.isEmpty() || !branches.contains(b)) {
					if (branches.contains(qualifier)) {
						b = qualifier;
					} else {
						b = toBranch.get("stable");
					}
				}
				request.getAttributes().put("branch", b);
				return super.getTargetRef(request, response);
			}
		});
		if (to.contains("{rr}")) {
			route.setMatchingMode(Template.MODE_STARTS_WITH);
		}
		wrapCookie(route, "qualifierToBranch", qualifier);
		return route;
	}

	/**
	 * Refreshes the list of distributions, versions, etc.
	 */
	public void refresh() {
		qualifiersMap = new HashMap<String, Qualifier>();
		versionsMap = new HashMap<String, Version>();
		distributionsByVersion = new HashMap<String, DistributionsList>();
		distributionsByVersionEdition = new HashMap<String, DistributionsList>();
		try {
			// Read the available editions, versions, distributions
			ClientResource cr;

			cr = new ClientResource(this.wwwUri + "/data/editions.json");
			cr.accept(MediaType.APPLICATION_JSON);
			synchronized (editions) {
				editions.clear();
				try {
					editions.addAll(cr.wrap(EditionsResource.class).list());
				} catch (Exception e) {
					getLogger().warning(
							"Cannot get the list of editions: "
									+ cr.getReference());
				}

			}

			cr = new ClientResource(this.wwwUri + "/data/versions.json");
			cr.accept(MediaType.APPLICATION_JSON);
			synchronized (versions) {
				versions.clear();
				try {
					versions.addAll(cr.wrap(VersionsResource.class).list());
				} catch (Exception e) {
					getLogger().warning(
							"Cannot get the list of versions: "
									+ cr.getReference());
				}
			}

			cr = new ClientResource(this.wwwUri + "/data/distributions.json");
			cr.accept(MediaType.APPLICATION_JSON);
			synchronized (distributions) {
				distributions.clear();
				try {
					distributions.addAll(cr.wrap(DistributionsResource.class)
							.list());
				} catch (Exception e) {
					getLogger().warning(
							"Cannot get the list of distributions: "
									+ cr.getReference());
				}
			}

			cr = new ClientResource(this.wwwUri + "/data/qualifiers.json");
			cr.accept(MediaType.APPLICATION_JSON);
			synchronized (qualifiers) {
				qualifiers.clear();
				try {
					qualifiers.addAll(cr.wrap(QualifiersResource.class).list());
				} catch (Exception e) {
					getLogger().warning(
							"Cannot get the list of qualifiers: "
									+ cr.getReference());
				}
			}
			for (Version version : versions) {
				for (Edition ve : version.getEditions()) {
					for (Edition e : editions) {
						if (e.getId().equals(ve.getId())) {
							ve.setLongname(e.getLongname());
							ve.setShortname(e.getShortname());
							break;
						}
					}
				}
			}
			synchronized (branches) {
				branches.clear();
				for (Version version : versions) {
					branches.add(version.getMinorVersion());
					versionsMap.put(version.getId(), version);
					for (Qualifier q : qualifiers) {
						if (q.getVersion().equals(version.getId())) {
							version.setQualifier(q.getId());
							break;
						} else {
							version.setQualifier(version.getId());
						}
					}
					DistributionsList dlv = new DistributionsList();
					for (Distribution distribution : distributions) {
						if (distribution.getVersion().equals(version.getId())) {
							dlv.add(distribution);

							String keyVe = distribution.getVersion() + "|"
									+ distribution.getEdition();
							DistributionsList dlve = distributionsByVersionEdition
									.get(keyVe);
							if (dlve == null) {
								dlve = new DistributionsList();
							}
							dlve.add(distribution);
							distributionsByVersionEdition.put(keyVe, dlve);
						}
					}
					distributionsByVersion.put(version.getId(), dlv);
				}
				for (Qualifier qualifier : qualifiers) {
					String branch = qualifier.getVersion().substring(0, 3);
					toBranch.put(qualifier.getId(), branch);
					qualifiersMap.put(qualifier.getId(), qualifier);
				}
			}
			updateRootRouter();

			// Get the feed
			cr = new ClientResource(this.feedGeneralAtomUri);
			Representation rep = cr.get(MediaType.APPLICATION_ATOM);
			Feed restletFeed = null;
			if (rep != null && rep.isAvailable()) {
				try {
					restletFeed = new Feed(rep);
				} catch (IOException e) {
					getLogger().warning(
							"Cannot parse the general feed." + e.getMessage());
				}
			}

			if (restletFeed != null) {
				ArrayList<Entry> digestEntries = new ArrayList<Entry>();
				for (Entry nEntry : restletFeed.getEntries()) {
					digestEntries.add(nEntry);
				}
				setFeedSummary(digestEntries);
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warning("Cannot load distributions and load feeds.");
		}
	}

	/**
	 * Shortcut method that add a {@link CookieSetting} to the response.
	 * 
	 * @param response
	 *            The response to complete.
	 * @param name
	 *            The name of the coookie.
	 * @param value
	 *            The value of the cookie.
	 */
	private void setCookie(Response response, String name, String value) {
		response.getCookieSettings().add(
				new CookieSetting(0, name, value, "/", null));
	}

	public void setFeedGeneral(List<Entry> feedGeneral) {
		this.feedGeneral = feedGeneral;
	}

	public void setFeedReleases(List<Entry> feedReleases) {
		this.feedReleases = feedReleases;
	}

	public void setFeedSummary(List<Entry> feedRestlet) {
		this.feedSummary = feedRestlet;
	}

	/**
	 * Sets up the redirections.
	 * 
	 * @param router
	 *            The router to complete.
	 */
	private void setRedirections(Router router) {

		redirectBranch(router, "/learn/javadocs", "/learn/javadocs/{branch}",
				null);

		// Issue #36: always route undefined user guide to 2.2, which is not the
		// stable release at this time.
		if (qualifiersMap.get("stable") != null) {
			redirect(router, "/learn/guide/stable", "/learn/guide/"
					+ qualifiersMap.get("stable").getVersion().substring(0, 3)
					+ "{rr}");
		} else {
			redirect(router, "/learn/guide/stable", "/learn/guide/2.2{rr}");
		}

		redirectBranch(router, "/learn/guide/testing",
				"/learn/guide/{branch}/", "testing");
		redirectBranch(router, "/learn/tutorial", "/learn/tutorial/{branch}/",
				null);
		redirectBranch(router, "/learn/guide", "/learn/guide/{branch}/", null);

		redirect(router, "/learn/2.0/tutorial", "/learn/tutorial/2.0");
	}

	/**
	 * Sets up the redirections.
	 * 
	 * @param router
	 *            The router to complete.
	 * @param redirectionsFileUri
	 *            The URI of the redirections file.
	 */
	private void readRouter(Router router, String redirectionsFileUri) {
		ClientResource resource = new ClientResource(redirectionsFileUri);
		try {
			Representation rep = resource.get();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					rep.getStream()));
			String line = null;
			int currentMode = Redirector.MODE_CLIENT_SEE_OTHER;
			while ((line = br.readLine()) != null) {
				getLogger().fine("add router instruction: " + line);
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				StringBuilder source = new StringBuilder();
				StringBuilder target = new StringBuilder();
				StringBuilder current = source;
				boolean bSource = true;
				boolean bTarget = false;
				boolean bStartsWith = false;
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if (Character.isWhitespace(c)) {
						if (bSource) {
							bSource = false;
						} else if (bTarget) {
							break;
						}
					} else if (c == '*' && bSource) {
						bStartsWith = true;
					} else if (!bSource && !bTarget) {
						bTarget = true;
						current = target;
						current.append(c);
					} else {
						current.append(c);
					}
				}
				if (source.length() > 0 && target.length() > 0) {
					if ("setMode".equals(source.toString())) {
						// Update the current redirection mode
						String strMode = target.toString();
						if ("CLIENT_PERMANENT".equals(strMode)) {
							currentMode = Redirector.MODE_CLIENT_PERMANENT;
						} else if ("CLIENT_FOUND".equals(strMode)) {
							currentMode = Redirector.MODE_CLIENT_FOUND;
						} else if ("CLIENT_SEE_OTHER".equals(strMode)) {
							currentMode = Redirector.MODE_CLIENT_SEE_OTHER;
						} else if ("CLIENT_TEMPORARY".equals(strMode)) {
							currentMode = Redirector.MODE_CLIENT_TEMPORARY;
						} else if ("REVERSE_PROXY".equals(strMode)) {
							currentMode = Redirector.MODE_SERVER_OUTBOUND;
						} else if ("ROUTER".equals(strMode)) {
							currentMode = -1;
						}
					} else {
						if (currentMode == -1) {
							Directory dir = new Directory(getContext(),
									"file://" + target.toString());
							rootRouter.attach(source.toString(), dir);
							getLogger().fine(
									"  attach directory: from "
											+ dir.getRootRef() + " to "
											+ source.toString());
						} else {
							if (!bStartsWith) {
								redirect(router, source.toString(),
										target.toString(), currentMode);
							} else {
								redirect(router, source.toString(),
										target.toString(), currentMode)
										.setMatchingMode(
												Template.MODE_STARTS_WITH);
							}
						}
					}
				}
			}
			br.close();
		} catch (Throwable t) {

		}
	}

	/**
	 * Helps to define redirections assuming that the router defines route by
	 * using the {@link Template.MODE_STARTS_WITH} mode. Redirection is made by
	 * default using the {@link Redirector#MODE_CLIENT_PERMANENT} mode.
	 * 
	 * @param router
	 *            The router where to define the redirection.
	 * @param from
	 *            The source template.
	 * @param to
	 *            The target template.
	 * @return The defined route.
	 */
	private TemplateRoute redirect(Router router, String from, String to) {
		return redirect(router, from, to, Redirector.MODE_CLIENT_PERMANENT);
	}

	/**
	 * Helps to define redirections assuming that the router defines route by
	 * using the {@link Template.MODE_STARTS_WITH} mode.
	 * 
	 * @param router
	 *            The router where to define the redirection.
	 * @param from
	 *            The source template.
	 * @param to
	 *            The target template.
	 * @param mode
	 *            The redirection mode (cf {@link Redirector}.
	 * @return The defined route.
	 */
	private TemplateRoute redirect(Router router, String from, String to,
			int mode) {
		TemplateRoute route = router.attach(from, new Redirector(getContext(),
				to, mode) {
			protected void serverRedirect(Restlet next, Reference targetRef,
					Request request, Response response) {
				if (next == null) {
		            getLogger().warning(
		                    "No next Restlet provided for server redirection to "
		                            + targetRef);
		        } else {
		            // Save the base URI if it exists as we might need it for
		            // redirections
		            Reference resourceRef = request.getResourceRef();
		            Reference baseRef = resourceRef.getBaseRef();

		            // Reset the protocol and let the dispatcher handle the protocol
		            request.setProtocol(null);
		            
		            // Update the request to cleanly go to the target URI
		            request.setResourceRef(targetRef);
		            request.getAttributes().remove(HeaderConstants.ATTRIBUTE_HEADERS);
		            next.handle(request, response);

		            // Memorize Access-Control-Allow-* headers to reinject in the response
		            Series<Header> resHeaders = (Series<Header>) response.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		            Series<Header> newHeaders = new Series<Header>(Header.class);
		            if (resHeaders != null) {
			            for (Header h : resHeaders) {
			            	if (h.getName().startsWith("Access-Control-Allow")) {
			            		newHeaders.add(h.getName(), h.getValue());
							}
						}
					}
					
		            // Allow for response rewriting and clean the headers
		            response.setEntity(rewrite(response.getEntity()));
		            response.getAttributes().remove(HeaderConstants.ATTRIBUTE_HEADERS);
		            request.setResourceRef(resourceRef);
					
					// Reinject Access-Control-Allow-* headers
		            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, newHeaders);
		            
		            // In case of redirection, we may have to rewrite the redirect URI
		            if (response.getLocationRef() != null) {
		                Template rt = new Template(this.targetTemplate);
		                rt.setLogger(getLogger());
		                int matched = rt.parse(response.getLocationRef().toString(),
		                        request);
		
		                if (matched > 0) {
		                    String remainingPart = (String) request.getAttributes()
		                            .get("rr");
		
		                    if (remainingPart != null) {
		                        response.setLocationRef(baseRef.toString()
		                                + remainingPart);
		                    }
		                }
		            }
				}

			}
		});
		if (to.contains("{rr}")) {
			route.setMatchingMode(Template.MODE_STARTS_WITH);
		}
		return route;
	}

	@Override
	public synchronized void start() throws Exception {
		super.start();
		refresh();
	}

	/**
	 * Maintains coherency of the cookies
	 * 
	 * @param version
	 *            The current version.
	 * @param edition
	 *            The current edition.
	 * @param distribution
	 *            The current distribution.
	 * @param response
	 *            The current response to update.
	 */

	public void updateCookies(Version version, Edition edition,
			Distribution distribution, Response response) {
		setCookie(response, "branch", version.getMinorVersion());
		setCookie(response, "distribution", distribution.getFileType());
		setCookie(response, "edition", edition.getId());
		setCookie(response, "release", version.getQualifier());
		setCookie(response, "version", version.getId());
	}

	private void updateRootRouter() {
		rootRouter.getRoutes().clear();

		rootRouter.attach("/feeds/summary", FeedSummaryResource.class);
		rootRouter.attach("/feeds/general", FeedGeneralResource.class);
		rootRouter.attach("/feeds/releases", FeedReleasesResource.class);

		// Guarding access to sensitive services
		ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(),
				ChallengeScheme.HTTP_BASIC, "Admin section");
		MapVerifier verifier = new MapVerifier();
		verifier.getLocalSecrets().put(this.login, this.password);
		guard.setVerifier(verifier);
		guard.setNext(RestletComRefreshResource.class);
		rootRouter.attach("/rf-refresh", guard);

		// Set up routes and redirections.
		readRouter(rootRouter, routerPropertiesFileReference);

		// "download" routing
		Router downloadRouter = new Router(getContext());
		downloadRouter.getRoutes().clear();
		// redirect stable, testing, unstable uris to the "current" page.
		for (Qualifier qualifier : qualifiers) {
			wrapCookie(
					redirect(downloadRouter, "/" + qualifier.getId(),
							"/download/current"), "qualifier",
					qualifier.getId());
		}
		// Serve Web pages
		downloadRouter.attach("/current", DownloadCurrentServerResource.class);
		downloadRouter.attach("/past", DownloadPastServerResource.class);
		downloadRouter.getRoutes().add(
				new StartsWithRoute(downloadRouter, new Directory(getContext(),
						this.wwwUri + "/download"), "\\/[a-zA-Z]+"));
		// Redirect "branches" uris (ie "/download/2.x"), to the "past" url.
		for (String branch : branches) {
			wrapCookie(
					redirect(downloadRouter, "/" + branch, "/download/past"),
					"branch", branch);
		}
		// Serve archives
		downloadRouter.attachDefault(new Directory(getContext(), this.dataUri
				+ "/archive/restlet"));

		rootRouter.attach("/download", downloadRouter);
	}

	/**
	 * Wraps the given route in order to set cookie.
	 * 
	 * @param route
	 *            the route to wrap.
	 * @param cookie
	 *            The name of the cookie.
	 * @param value
	 *            The value of the cookie.
	 * @return
	 */
	private TemplateRoute wrapCookie(TemplateRoute route, final String cookie,
			final String value) {
		if ("branch".equals(cookie)) {
			Filter filter = new Filter(getContext(), route.getNext()) {
				@Override
				protected void afterHandle(Request request, Response response) {
					if (value != null) {
						setCookie(response, cookie, value);
					} else {
						setCookie(response, cookie, (String) request
								.getAttributes().get("branch"));
					}
				}
			};
			route.setNext(filter);
		} else if ("qualifierToBranch".equals(cookie)) {
			Filter filter = new Filter(getContext(), route.getNext()) {
				@Override
				protected void afterHandle(Request request, Response response) {
					String b = (value != null) ? toBranch.get(value) : null;
					if (b == null) {
						// induce it from the request's cookies
						b = request.getCookies().getFirstValue("branch", "");
					}
					if (b == null || b.isEmpty() || !branches.contains(b)) {
						b = toBranch.get("stable");
					}
					setCookie(response, "branch", b);
				}
			};
			route.setNext(filter);
		} else {
			Filter filter = new Filter(getContext(), route.getNext()) {
				@Override
				protected void afterHandle(Request request, Response response) {
					super.afterHandle(request, response);
					setCookie(response, cookie, value);
				}
			};
			route.setNext(filter);
		}
		return route;
	}
}
