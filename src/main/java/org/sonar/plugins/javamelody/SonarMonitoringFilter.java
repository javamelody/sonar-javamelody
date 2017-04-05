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
