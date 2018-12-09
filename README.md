sonar-javamelody
=========================

Extension plugin for JavaMelody to monitor performance in Sonar 3.1 or later.

See https://github.com/javamelody/javamelody/wiki

Continuous integration: https://javamelody.ci.cloudbees.com/job/sonar-javamelody/

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
