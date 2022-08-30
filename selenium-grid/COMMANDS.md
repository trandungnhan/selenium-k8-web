selenium-grid/COMMANDCommand to start Hub
java -jar selenium-server-4.4.0.jar hub
Command to start Node
This node will register:

Chrome
Firefox
Safari
Note: IF you are on Windows OS, please remove part related to safari and add for Edge

java -jar -Dwebdriver.<type>.<name>s path/to/selenium/server.jar node --config /path/to/nodeConfig.json
java -jar -Dwebdriver.gecko.driver=geckodriver.exe -Dwebdriver.chrome.driver=chromedriver.exe selenium-server-4.4.0.jar node --config node_config.json
java -jar -Dwebdriver.chrome.driver="F:/SDET Pro/SOURCE_CODE/selenium-k8-web/selenium-grid/chromedriver.exe" -Dwebdriver.gecko.driver="F:/SDET Pro/SOURCE_CODE/selenium-k8-web/selenium-grid/geckodriver.exe" "F:/SDET Pro/SOURCE_CODE/selenium-k8-web/selenium-grid/selenium-server-4.4.0.jar" node --config "F:/SDET Pro/SOURCE_CODE/selenium-k8-web/selenium-grid/node_config.json"


java -Dwebdriver.gecko.driver=./geckodriver.exe -jar ./selenium-server-4.4.0.jar

NOTE: on Windows need to specify extension like gecko.exe, chrome.exe

Next
In second stage, we will learn about how to set up using DockerS.md




