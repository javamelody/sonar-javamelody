sonar-javamelody
=========================

See http://javamelody.googlecode.com

Extension plugin for JavaMelody plugin in Sonar.

I - Compiling and Installing the plugin:
---------------------------------------
 - Install maven
 - Clone the repository
 - Compile and test the code, then generate the jar:
	-> run "mvn clean install" command in your terminal
 - copy the jar (in the new generated target folder) in <path_to_your_sonar_install>/extensions/plugins folder,
 - restart sonar