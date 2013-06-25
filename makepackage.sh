#!/usr/bin/env sh

mkdir -v pkg
cp -r doc icons src test data README.md sample.rsk .project .classpath pkg
tar -cvaf pkg.tar.gz pkg

rm -r pkg