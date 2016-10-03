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

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.SonarPlugin;

/**
 * Main class of JavaMelody Sonar Plugin
 *
 * @author Emeric Vernat
 */

public class SonarJavaMelodyPlugin extends SonarPlugin {
	public SonarJavaMelodyPlugin() {
		super();
		System.setProperty("javamelody.no-database", "true");
	}

	@Override
	public List<?> getExtensions() {
		final List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();
		try {
			list.add(SonarMonitoringFilter.class);
			list.add(MonitoringLink.class);
		} catch (final Throwable t) {
			// the plugin is installed when doing sonar analysis on a project !
			// but fails to load the class javax.servlet.Filter,
			// so ignoring the problem in a sonar analysis
		}
		return list;
	}
}
