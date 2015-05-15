APARAPI_HOME=/curr/cody/temp/aparapi
java \
 -Djava.library.path=$APARAPI_HOME/com.amd.aparapi.jni/dist \
 -Dcom.amd.aparapi.executionMode=$1 \
 -Dcom.amd.aparapi.enableVerboseJNI=true \
 -Dcom.amd.aparapi.enableExecutionModeReporting=true \
 -classpath $APARAPI_HOME/com.amd.aparapi/dist/aparapi.jar:classes/ \
 Main

