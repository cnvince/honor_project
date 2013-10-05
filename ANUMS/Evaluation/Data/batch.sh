#!/bin/bash

rm Fold-*/regression.csv
cd Fold-0
R CMD BATCH regression.R
cd ../Fold-1
R CMD BATCH regression.R
cd ../Fold-2
R CMD BATCH regression.R
cd ../Fold-3
R CMD BATCH regression.R
echo "Done!"
