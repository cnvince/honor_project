
R version 2.14.1 (2011-12-22)
Copyright (C) 2011 The R Foundation for Statistical Computing
ISBN 3-900051-07-0
Platform: x86_64-apple-darwin9.8.0/x86_64 (64-bit)

R is free software and comes with ABSOLUTELY NO WARRANTY.
You are welcome to redistribute it under certain conditions.
Type 'license()' or 'licence()' for distribution details.

  Natural language support but running in an English locale

R is a collaborative project with many contributors.
Type 'contributors()' for more information and
'citation()' on how to cite R or R packages in publications.

Type 'demo()' for some demos, 'help()' for on-line help, or
'help.start()' for an HTML browser interface to help.
Type 'q()' to quit R.

[Previously saved workspace restored]

> data<-read.csv(file="1.csv.csv",sep=",",head=TRUE)
> jpeg("1.jpg")
> boxplot(data,las=2,cex.axis=0.6)
> dev.off()
null device 
          1 
> data<-read.csv(file="2.csv.csv",sep=",",head=TRUE)
> jpeg("2.jpg")
> boxplot(data,las=2,cex.axis=0.6)
> dev.off()
null device 
          1 
> data<-read.csv(file="3.csv.csv",sep=",",head=TRUE)
> jpeg("3.jpg")
> boxplot(data,las=2,cex.axis=0.6)
> dev.off()
null device 
          1 
> data<-read.csv(file="0.csv.csv",sep=",",head=TRUE)
> jpeg("0.jpg")
> boxplot(data,las=2,cex.axis=0.6)
> dev.off()
null device 
          1 
> 
> proc.time()
   user  system elapsed 
  0.403   0.062   1.434 
