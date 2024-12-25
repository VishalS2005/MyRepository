library(faraway)
pimadf <- data.frame(pima)
test0pimadf <- subset(pimadf, test ==0)
head(test0pimadf)
diabetes0 <- mean(test0pimadf$diabetes)
diabetes0
test1pimadf <- subset(pimadf, test ==1)
head(test1pimadf)
diabetes1 <- mean(test1pimadf$diabetes)
diabetes1
