data<-read.csv(file="alldata.csv",sep=",",head=TRUE)
fit<-lm(rel~-1+length:sc+length:rs+cs:sc+cs:rs,data=data)
write.csv(coef(summary(fit)),file="regression.csv")
