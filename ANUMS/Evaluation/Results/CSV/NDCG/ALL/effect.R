data<-read.csv(file="NDCG@10.csv",sep=",",head=TRUE)
for(i in 1:10)
{
  for(j in 1:10)
	{
	t<-t.test(data[i],data[j],paired=TRUE)
	res<-pwr.t.test(n=44,power=t$p.value,sig.level=0.05,type="paired",alternative="greater              ")
	write(res$d,file="foo.values",sep=",",append=TRUE)
	}
}
