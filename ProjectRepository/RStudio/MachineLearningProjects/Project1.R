library(rpart)
library(rpart.plot)

setwd("C:/Users/visha/Downloads/Data101")
training <- read.csv("EarningsTrain.csv", header=TRUE)
testing <- read.csv("EarningsTest.csv", header = TRUE)

#Avg total variability for salary in training set
ybar=mean(training$Earnings)
avgSSTO <- mean ((training$Earnings - ybar)^2)
avgSSTO #MSE resulting from ybar as the prediction of all obs: 4436460

#Regression tree using rpart() with all predictors and with all defaults
rpart_train <- rpart(Earnings ~  Number_Of_Professional_Connections + GPA + Graduation_Year + Major,
                     data=training, method="anova",
                     control=rpart.control(minsplit = 15, maxdepth = 5, cp=0.00001)) 
#

rpart.plot(rpart_train)
pred.Earnings = predict(rpart_train, training, type = "vector")
train_predicted = cbind(training,pred.Earnings)
MSE<- mean((train_predicted$Earnings-train_predicted$pred.Earnings)^2)
MSE #9784.588

test_predicted <- predict(rpart_train, testing)
write.csv(test_predicted, "EarningsTestPREDICTIONS.csv", row.names = FALSE)
