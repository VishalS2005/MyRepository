library(faraway)
head(pima)
summary(pima)
age_table <- table(pima$age) #creation of 1 way table using age category
head(age_table)
age_test_table<-table(pima$age,pima$test) #creation of 2 way table using age and test(diabetes) category
head(age_test_table)
age_less_40<-subset(pima,age<40) #only records pima data for women who are less than 40 years old
head(age_less_40)
less_40_ages <- table(age_less_40$age) #creation of 1 way table using age category, but only less than 40
head(less_40_ages)
less_40_ages_test <- table(age_less_40$age,age_less_40$test) #creation of 2 way table using age and test(diabetes) category
less_40_ages_test
