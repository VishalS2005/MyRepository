library(rpart)
library(rpart.plot)
setwd("C:/Users/visha/Downloads/Data101")
ClothesTest <- read.csv("ClothesTest.csv", header=TRUE)
ClothesTrain <- read.csv("ClothesTrain.csv", header=TRUE)

View(ClothesTrain)
View(ClothesTest)

#Default error before decision tree predictions
ybar = mean(ClothesTrain$Price)
oldMSE <- mean((ClothesTrain$Price - ybar)^2)		 
oldMSE

decision <- rpart(Price ~ Category + Brand + Color + Size + Material, data = ClothesTrain, method="anova",
                 control=rpart.control(minsplit = 8, maxdepth = 8, cp=0.001))

rpart.plot(decision)

pred.results = predict(decision, ClothesTrain, type = "vector")
train_predicted = cbind(ClothesTrain, pred.results)

head(train_predicted)

MSE <- mean((train_predicted$Price - train_predicted$pred.results)^2)
MSE 

test.results = predict(decision, ClothesTest, type = "vector")
test_predicted = cbind(ClothesTest, test.results)
head(test_predicted)

MSEComparison <- mean((train_predicted$Price - test_predicted$test.results)^2)
MSEComparison

library(naivebayes)
