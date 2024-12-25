test1 <- prop.test(x = c(30, 40), n = c(50,50), conf.level = .95, correct = FALSE)
test1
test1$conf.int
test1$p.value
#The p value is 0.0291. There is statistically significant evidence to reject 
#the hypothesis at the alpha level of 5% because 0.0291 < 0.05 
set.seed(23415)
library(EnvStats)
test2 <- twoSamplePermutationTestProportion(c(rep(1, 30),rep(0, 20)), c(rep(1,40),rep(0,10)), alternative = "two.sided")
test2
#The p-value 0.0485 is less than the alpha level 5% so we reject the null hypothesis.