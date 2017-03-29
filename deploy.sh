#!/bin/sh
sbt fullOptJS
polymer build
cd build/default
sed -i -- 's|./target/scala-2.12/patcheditor-jsdeps.js|./target/scala-2.12/patcheditor-jsdeps.min.js|g' index.html
sed -i -- 's|./target/scala-2.12/patcheditor-fastopt.js|./target/scala-2.12/patcheditor-opt.js|g' index.html

cd ../..
rm -rf docs/*
cp -rv build/default/* docs
