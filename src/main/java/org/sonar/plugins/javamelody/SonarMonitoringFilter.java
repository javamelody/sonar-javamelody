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

import org.sonar.api.web.ServletFilter;

import net.bull.javamelody.PluginMonitoringFilter;

public class SonarMonitoringFilter extends ServletFilter {
	private final MyPluginMonitoringFilter pluginMonitoringFilter = new MyPluginMonitoringFilter();

	private static class MyPluginMonitoringFilter extends PluginMonitoringFilter {
		/** {@inheritDoc} */
		@Override
		public String getApplicationType() {
			return "Sonar";
		}
	}

	/** {@inheritDoc} */
	@Override
	public void init(FilterConfig config) throws ServletException {
		pluginMonitoringFilter.init(config);
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
		pluginMonitoringFilter.doFilter(request, response, chain);
	}
}
