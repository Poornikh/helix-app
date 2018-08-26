Software Pre-Request:
       1. JDK\JRE 8.
       2. Maven 3+.
       
Package Structure:
		After Maven build under target directory helix-app-rev001.zip will be created. On Unzipping the 
zip file below will be the directory structure and file contents.	
                    helix-app
                       |
                       |--bin
                       |   |--startAPIServer.bat
                       |   |--curl-save.bat
                       |   |--curl-get.bat
                       |   |--open-prompt.bat
                       |   |--post-event.json
                       |   |--env.properties
                       |
                       |
                       |--lib
                       |   |--helix-app-rev001.jar
                       |
                       |--ReadMe.txt

Steps to Start Server:
	1. Set the JAVA_HOME path to /bin/env.properties file.
	2. Double click startAPIServer.bat file.
	3. Server will be started in a cmd window at port 8081.
Test the Application:
	Pre-request:
		Server should be up and running. Follow above steps for starting the server.
	Steps:
	  Save Event:
		1.Sample json is avialable in post-event.json file. Edit the input as requried and save in same file. Default will save a event with event id 1.
		2.Double click open-prompt.bat.
		3. New cmd window will be opened.
		4. In cmd window type command curl-save.bat.
		5.Output response will be displayed in same cmd window
	  Get Event:
		1.Double click open-prompt.bat.
		2. New cmd window will be opened.
		3. In cmd window type command curl-get.bat <EVENT_ID> example: curl-get.bat 1
		4.Output response will be displayed in same cmd window.

Code can be pulled from Github URL

   
			
 