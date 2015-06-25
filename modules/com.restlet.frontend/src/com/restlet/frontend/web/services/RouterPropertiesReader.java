package com.restlet.frontend.web.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Redirector;

public abstract class RouterPropertiesReader {

	private String routerPropertiesRef;

	public RouterPropertiesReader(String routerPropertiesRef) {
		this.routerPropertiesRef = routerPropertiesRef;
	}

	public void read(Logger logger) {
		Map<String, Integer> modes = new HashMap<>();
		modes.put("CLIENT_PERMANENT", Redirector.MODE_CLIENT_PERMANENT);
		modes.put("CLIENT_FOUND", Redirector.MODE_CLIENT_FOUND);
		modes.put("CLIENT_SEE_OTHER", Redirector.MODE_CLIENT_SEE_OTHER);
		modes.put("CLIENT_TEMPORARY", Redirector.MODE_CLIENT_TEMPORARY);
		modes.put("REVERSE_PROXY", Redirector.MODE_SERVER_OUTBOUND);
		modes.put("ROUTER", -1);

		ClientResource resource = new ClientResource(routerPropertiesRef);
		BufferedReader br = null;
		try {
			Representation rep = resource.get();
			br = new BufferedReader(new InputStreamReader(rep.getStream()));
			String line = null;
			int currentMode = Redirector.MODE_CLIENT_SEE_OTHER;
			while ((line = br.readLine()) != null) {
				logger.fine("add router instruction: " + line);
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
						currentMode = modes.get(strMode);
					} else {
						handle(source.toString(), target.toString(),
								currentMode, bStartsWith);
					}
				}
			}
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Cannot read properties: "
					+ routerPropertiesRef, t);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Cannot close properties: "
							+ routerPropertiesRef, e);
				}
			}
		}
	};

	public abstract void handle(String source, String target, int currentMode,
			boolean bStartsWith);

}
