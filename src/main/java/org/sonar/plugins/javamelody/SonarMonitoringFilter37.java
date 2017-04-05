/*
 * Copyright 2008-2017 by Emeric Vernat
 *
 *     This file is part of Sonar JavaMelody plugin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import org.sonar.api.web.ServletFilter;

import net.bull.javamelody.PluginMonitoringFilter;

public class SonarMonitoringFilter37 extends ServletFilter {
	private static final boolean PLUGIN_AUTHENTICATION_DISABLED = Boolean
			.parseBoolean(System.getProperty("javamelody.plugin-authentication-disabled"));

	private final MyPluginMonitoringFilter pluginMonitoringFilter = new MyPluginMonitoringFilter();

	private Class<?> userSessionClass;
	private Class<?> permissionClass;

	private static class MyPluginMonitoringFilter extends PluginMonitoringFilter {
		/** {@inheritDoc} */
		@Override
		public String getApplicationType() {
			return "Sonar";
		}

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
			// Permission existe seulement depuis sonar 3.7 et UserSession etait
			// avant dans org.sonar.server.platform
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
				// TODO faire plutot un redirect vers login et return avant
				// doFilter ?
				e.printStackTrace();
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
				httpResponse.flushBuffer();
				return;
			}
		}

		pluginMonitoringFilter.doFilter(request, response, chain);
	}

	private void checkSystemAdmin() throws Exception {
		// cens� fonctionner en sonar 3.7 et +, mais pas en sonar 4.3.2
		if (!PLUGIN_AUTHENTICATION_DISABLED && userSessionClass != null && permissionClass != null) {
			final Object userSession = userSessionClass.getMethod("get").invoke(null);
			userSessionClass.getMethod("checkLoggedIn").invoke(userSession);
			// equivalent de
			// org.sonar.server.user.UserSession.get().checkLoggedIn();

			final Object systemAdminPermission = permissionClass.getField("SYSTEM_ADMIN").get(null);
			userSessionClass.getMethod("checkGlobalPermission", permissionClass).invoke(userSession,
					systemAdminPermission);
			// equivalent de
			// org.sonar.server.user.UserSession.get().checkGlobalPermission(Permission.SYSTEM_ADMIN);
		}
	}
}
