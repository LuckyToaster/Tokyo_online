#!/bin/bash

cd /mnt/c/Users/sench/eclipse-workspace/Tokyo/src/refactor4
javac *.java
cd ..
fg 
command java refactor4.Server
