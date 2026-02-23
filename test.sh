#!/bin/bash
set -e

echo "========================================"
echo "Advanced Flight Booking System - TEST"
echo "========================================"
echo ""

if [ ! -f lib/junit-platform-console-standalone.jar ]; then
    echo "JUnit 5 jar not found. Place it at lib/junit-platform-console-standalone.jar"
    exit 1
fi

mkdir -p bin
mkdir -p bin_test

# Compile main sources
find src -name "*.java" > sources.txt
javac -d bin @sources.txt

# Compile tests
find test -name "*.java" > test_sources.txt
javac -d bin_test -cp "bin:lib/junit-platform-console-standalone.jar" @test_sources.txt

echo ""
echo "Running tests..."
java -jar lib/junit-platform-console-standalone.jar --class-path "bin:bin_test" --scan-classpath
