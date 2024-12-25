vaccinated = c(200688,24,33)
notVaccinated = c(201087,27,155)
chisq.test(data.frame(vaccinated,notVaccinated))
#The data provide significant evidence to conclude that there 
#is not dependence between vaccination status and polio status 
#because the p-value 2.2e-16 < 0.05 where the significance level is 0.05
library(EnvStats)
twoSamplePermutationTestLocation(x=vaccinated,y=notVaccinated,n.permutations = 2000)
#The data provide significant evidence to conclude that there
#is dependence between vaccination status and polio status
#because the p-value 0.6125 < 0.05 where the significance level is 0.05