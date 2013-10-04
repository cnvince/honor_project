data<-read.csv(file="train.csv",sep=",",head=TRUE)
fit<-lm(rel~-1+length+cs+sc+rs,data=data)
write.csv(coef(summary(fit)),file="regression.csv")
