#!/bin/sh
cp -v index-fastopt.html docs/index.html
cp -v target/scala-2.12/patcheditor-jsdeps.min.js docs
cp -v target/scala-2.12/patcheditor-opt.js docs
cp -v droidpatches.json docs

cd docs
sed -i -- 's|./target/scala-2.12/patcheditor-jsdeps.js|./patcheditor-jsdeps.min.js|g' index.html
sed -i -- 's|./target/scala-2.12/patcheditor-fastopt.js|./patcheditor-opt.js|g' index.html

