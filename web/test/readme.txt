This directory is meant for test files only and should not be included in production war files.

If you need to view this files in your development environment you have two options:

1) The "deploy-web" target will copy all jsp and css files in /web to the currently configured
tomcathome/webapps/openmrs/* directory.  You can do this after creating and deploying a war
file into your tomcat instance.  The deploy-web task is set to not ignore /test.

2) The "package-web" task is what creates the war file.  This task is set to ignore /test.  To
create a war file with the /test included (for non-production environments hopefully), edit the
package-web target in the build.xml file.  Change the "excludes" attribute on the "war" element 
to not contain the string ", **/test/". 