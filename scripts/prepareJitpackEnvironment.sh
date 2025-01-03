#!/bin/bash

# Fix for https://jitpack.io build error
mkdir -p ~/.gradle
echo "org.gradle.jvmargs=-Xmx2g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError" > ~/.gradle/gradle.properties
