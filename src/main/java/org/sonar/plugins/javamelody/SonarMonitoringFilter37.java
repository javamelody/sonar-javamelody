/*
 * Sonar JavaMelody Plugin
 * Copyright (C) 2013 Emeric Vernat
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.javamelody;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.PluginMonitoringFilter;

import org.sonar.api.web.ServletFilter;

public class SonarMonitoringFilter37 extends ServletFilter {
	private static final boolean PLUGIN_AUTHENTICATION_DISABLED = Boolean.parseBoolean(System
			.getProperty("javamelody.plugin-authentication-disabled"));

	private final MyPluginMonitoringFilter pluginMonitoringFilter = new MyPluginMonitoringFilter();
	
	private Class<?> userSessionClass;
	private Class<?> permissionClass;
	
	private static class MyPluginMonitoringFilter extends PluginMonitoringFilter {
		protected final String getMyMonitoringUrl(HttpServletRequest httpRequest) {
			return super.getMonitoringUrl(httpRequest);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void init(FilterConfig config) throws ServletException {
		pluginMonitoringFilter.init(config);

		try {
			// https://github.com/SonarSource/sonar/blob/master/sonar-server/src/main/java/org/sonar/server/user/UserSession.java
			userSessionClass = Class.forName("org.sonar.server.user.UserSession");
			// https://github.com/SonarSource/sonar/blob/master/sonar-core/src/main/java/org/sonar/core/permission/Permission.java
			permissionClass = Class.forName("org.sonar.core.permission.Permission");
		} catch (final ClassNotFoundException e) {
			userSessionClass = null;
			permissionClass = null;
			// Permission existe seulement depuis sonar 3.7 et UserSession etait avant dans org.sonar.server.platform
			// TODO Permission n'existe plus en sonar 4.3.2
		}
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		pluginMonitoringFilter.destroy();
	}

	/** {@inheritDoc} */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)) {
			pluginMonitoringFilter.doFilter(request, response, chain);
			return;
		}
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (httpRequest.getRequestURI().equals(pluginMonitoringFilter.getMyMonitoringUrl(httpRequest))) {
			try {
				checkSystemAdmin();
			} catch (final Exception e) {
				// TODO faire plutot un redirect vers login et return avant doFilter ?
				e.printStackTrace();
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
				httpResponse.flushBuffer();
				return;
			}
		}

		pluginMonitoringFilter.doFilter(request, response, chain);
	}
	
	private void checkSystemAdmin() throws Exception {
		// censé fonctionner en sonar 3.7 et +, mais pas en sonar 4.3.2
		if (!PLUGIN_AUTHENTICATION_DISABLED && userSessionClass != null && permissionClass != null) {
			final Object userSession = userSessionClass.getMethod("get").invoke(null);
			userSessionClass.getMethod("checkLoggedIn").invoke(userSession);
			// equivalent de org.sonar.server.user.UserSession.get().checkLoggedIn();

			final Object systemAdminPermission = permissionClass.getField("SYSTEM_ADMIN").get(null);
			userSessionClass.getMethod("checkGlobalPermission", permissionClass).invoke(userSession, systemAdminPermission);
			// equivalent de org.sonar.server.user.UserSession.get().checkGlobalPermission(Permission.SYSTEM_ADMIN);
		}
	}
}
