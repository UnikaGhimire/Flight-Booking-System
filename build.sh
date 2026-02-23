#!/bin/bash
echo "========================================"
echo "Advanced Flight Booking System - BUILD"
echo "========================================"
echo ""

mkdir -p bin

echo "Compiling all Java files..."
find src -name "*.java" > sources.txt
javac -d bin @sources.txt

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ BUILD SUCCESSFUL!"
    echo ""
    echo "To run: java -cp bin bcu.cmp5332.bookingsystem.main.Main"
    echo ""
    echo "Default Admin Login:"
    echo "  Username: admin"
    echo "  Password: admin123"
    echo ""
else
    echo ""
    echo "✗ BUILD FAILED"
    exit 1
fi

echo ""
echo "----------------------------------------"
echo "TESTING (JUnit 5)"
echo "----------------------------------------"
if [ -f lib/junit-platform-console-standalone.jar ]; then
    mkdir -p bin_test
    find test -name "*.java" > test_sources.txt
    javac -d bin_test -cp "bin:lib/junit-platform-console-standalone.jar" @test_sources.txt

    if [ $? -eq 0 ]; then
        echo ""
        echo "Running tests..."
        java -jar lib/junit-platform-console-standalone.jar --class-path "bin:bin_test" --scan-classpath
    else
        echo ""
        echo "✗ TEST BUILD FAILED"
        exit 1
    fi
else
    echo "JUnit 5 jar not found. Place it at lib/junit-platform-console-standalone.jar"
fi
