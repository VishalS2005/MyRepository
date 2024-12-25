set.seed(13245)
sample1 <- runif(9, min = 0, max = 1)
sample1
sample2 <- runif(9, min = 0, max = 1.3)
sample2
qqnorm(sample1)
qqnorm(sample2)
test1 <- t.test(sample1, sample2, var.equal = TRUE)
test1
library(EnvStats)
twoSamplePermutationTestLocation(x=sample1, y=sample2, fcn = "mean")
