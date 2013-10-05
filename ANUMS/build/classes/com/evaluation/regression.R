data<-read.csv(file="train.csv",sep=",",head=TRUE)
fit<-lm(lm(rel~-1+length:sc+length:rs+cs:sc+cs:rs,data=data))
write.csv(coef(summary(fit)),file="regression.csv")