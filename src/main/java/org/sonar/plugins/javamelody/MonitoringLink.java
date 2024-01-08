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

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;
import org.sonar.api.web.UserRole;

@UserRole(UserRole.ADMIN)
public final class MonitoringLink implements PageDefinition {
	@Override
	public void define(Context context) {
		context
			.addPage(Page.builder("/monitoring")
			.setName("Monitoring")
			.setAdmin(true)
			.build());
	}
}
