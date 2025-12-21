sonar-javamelody
=========================

Extension plugin for JavaMelody to monitor performance in Sonar 3.1 or later.

**Sonarqube has migrated to jakarta servlet api which is not a problem for javamelody, but Sonarqube has also [dropped support of ServletFilter](https://github.com/SonarSource/sonarqube/commit/70f938bab6e565d8842a966d29104a046d469471) and so usage of the standard servlet api and of javamelody becomes impossible. So sonar-javamelody will not be compatible with Sonarqube v25 and later. No other release of sonar-javameloy is expected.**

See https://github.com/javamelody/javamelody/wiki

License ASL, http://www.apache.org/licenses/LICENSE-2.0

Please submit github pull requests and github issues.


Downloading and Installing the plugin:
---------------------------------------
 - download the latest sonar-javamelody-plugin jar file from [releases](../../releases)
 - copy the jar in \<path_to_your_sonar_install\>/extensions/plugins folder,
 - restart sonar
 - open http://localhost:9000/monitoring in a browser


Compiling and Installing the plugin:
---------------------------------------
 - Install maven
 - Clone the repository
 - Compile and test the code, then generate the jar:
	-> run "mvn clean install" command in your terminal
 - copy the jar (in the new generated target folder) in \<path_to_your_sonar_install\>/extensions/plugins folder,
 - restart sonar
 - open http://localhost:9000/monitoring in a browser
