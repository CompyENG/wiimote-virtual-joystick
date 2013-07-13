javac UInput.java
javah -jni UInput
gcc --shared -I /usr/include/ -I /usr/lib/jvm/java-6-sun/include/ -I /usr/lib/jvm/java-6-sun/include/linux/ -lsuinput -fPIC UInput.c -o libUInput.so
javac -cp "bluecove-2.1.0.jar:bluecove-gpl-2.1.0.jar:motej-library-0.9-bobby2.jar:motej-extras-0.9.jar:slf4j-simple-1.6.1.jar:slf4j-api-1.6.1.jar:." AdvancedDiscovery.java
