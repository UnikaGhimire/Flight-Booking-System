#!/bin/bash
set -e

echo "========================================"
echo "Advanced Flight Booking System - JAVADOC"
echo "========================================"
echo ""

mkdir -p docs
javadoc -d docs -sourcepath src -subpackages bcu.cmp5332.bookingsystem

echo ""
echo "Javadoc generated in: docs/index.html"
