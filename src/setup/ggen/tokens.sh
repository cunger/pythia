#!/bin/bash

echo "import <GRAMMAR>
pg-words" >> tokens.gfs
gf --run <tokens.gfs
