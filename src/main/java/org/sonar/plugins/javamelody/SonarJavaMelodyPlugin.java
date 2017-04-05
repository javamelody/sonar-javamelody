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
