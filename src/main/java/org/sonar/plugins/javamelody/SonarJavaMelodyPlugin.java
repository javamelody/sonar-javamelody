/*
 * Copyright 2008-2019 by Emeric Vernat
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

import org.sonar.api.Plugin;

import net.bull.javamelody.Parameter;

/**
 * Main class of JavaMelody Sonar Plugin
 *
 * @author Emeric Vernat
 */

public class SonarJavaMelodyPlugin implements Plugin {
	public SonarJavaMelodyPlugin() {
		super();
		Parameter.NO_DATABASE.setValue("true");
	}

	@Override
	public void define(Plugin.Context context) {
		try {
			context.addExtension(SonarMonitoringFilter.class);
		} catch (final Throwable t) {
			// the plugin is installed when doing sonar analysis on a project !
			// but fails to load the class javax.servlet.Filter,
			// so ignoring the problem in a sonar analysis.
			// see also https://sonarsource.atlassian.net/browse/SONAR-19996
			// and https://sonarsource.atlassian.net/browse/SONAR-21197 since sonarqube 10.4
		}
	}
}
