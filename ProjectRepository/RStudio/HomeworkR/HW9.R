observed = c(6,29,38,22,5)
expected = c(0.05, 0.3, 0.4, 0.2, 0.05)
chisq.test(x=observed, p=expected)
#The data do not provide sufficient evidence to conclude that the number of 
#absences are different than those specified with the professor because the
#p-value is 0.9702 > 0.05 where 0.05 represents a significance level of 0.05.
