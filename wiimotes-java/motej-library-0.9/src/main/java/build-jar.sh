javac -cp "bluecove-2.1.0.jar:bluecove-gpl-2.1.0.jar:slf4j-api-1.6.1.jar:slf4j-simple-1.6.1.jar:." motej/event/*.java
javac -cp "bluecove-2.1.0.jar:bluecove-gpl-2.1.0.jar:slf4j-api-1.6.1.jar:slf4j-simple-1.6.1.jar:." motej/request/*.java
javac -cp "bluecove-2.1.0.jar:bluecove-gpl-2.1.0.jar:slf4j-api-1.6.1.jar:slf4j-simple-1.6.1.jar:." motej/*.java
jar cvf motej-library-0.9-disconnect-mod.jar motej
