setwd("C:/Users/visha/Downloads/Data101")
library(rpart)
library(rpart.plot)
loanTest <- read.csv("bankloanTest.csv", header=TRUE)
loanTrain <- read.csv("bankloanTrain.csv", header=TRUE)


head(loanTest)
head(loanTrain)


hist(loanTrain$Age, main="Age", col="green")
hist(loanTrain$Experience, main="Experience", col="red")
hist(loanTrain$Income, main="Income", col="yellow")
barplot(table(loanTrain$Education), main="Education", col="blue")
hist(loanTrain$CCAvg, main="CCAvg")
hist(loanTrain$Mortgage, main="Mortgage")
plot(loanTrain$Age, loanTrain$Experience, main="Age vs. Experience", xlab="Age", ylab="Experience", col="blue")


#Default error before decision tree predictions
ybar = mean(loanTrain$Personal.Loan)
oldMSE <- mean((loanTrain$Personal.Loan - ybar)^2)		 
oldMSE


decision <- rpart(Personal.Loan ~ CCAvg + Family + Income + Education + Age + Experience, data = loanTrain, method="anova",
                  control=rpart.control(minsplit = 50, maxdepth = 4, cp=0.01))
rpart.plot(decision)


pred.results = predict(decision, loanTrain, type = "vector")
train_predicted = cbind(loanTrain, pred.results)


newMSE <- mean((train_predicted$Personal.Loan - train_predicted$pred.results)^2)
newMSE 


pred.loanTree = predict(decision, loanTest, type = "vector")

# Assuming loanTrain is your existing dataframe


loanTrain$ID <- as.factor(loanTrain$ID)
loanTrain$Age <- as.factor(loanTrain$Age)
loanTrain$Experience <- as.factor(loanTrain$Experience)
loanTrain$Income <- as.factor(loanTrain$Income)
loanTrain$ZIP.Code <- as.factor(loanTrain$ZIP.Code)
loanTrain$Family <- as.factor(loanTrain$Family)
loanTrain$CCAvg <- as.factor(loanTrain$CCAvg)
loanTrain$Education <- as.factor(loanTrain$Education)
loanTrain$Mortgage <- as.factor(loanTrain$Mortgage)
loanTrain$Securities.Account <- as.factor(loanTrain$Securities.Account)
loanTrain$CD.Account <- as.factor(loanTrain$CD.Account)
loanTrain$Online <- as.factor(loanTrain$Online)
loanTrain$CreditCard <- as.factor(loanTrain$CreditCard)
loanTrain$Personal.Loan <- as.factor(loanTrain$Personal.Loan)


#nb
library(klaR)
?confusionMatrix
nb_mod <- NaiveBayes(Personal.Loan ~ CCAvg + Family + Income + Education + Age + Experience, data=loanTrain)
pred.loan <- predict(nb_mod, loanTest)

# For the naiveBayes model
confusionMatrix(table(loanTest$Personal.Loan, pred.loan$class))

#For the rPart model
# Convert the actual classes and predicted classes to factors
actual.classes <- as.factor(loanTest$Personal.Loan)
predicted.classes <- as.factor(ifelse(pred.loanTree > 0.5, 1, 0)) # Convert probabilities to class labels

# Create a table
confusion.table <- table(actual.classes, predicted.classes)

# Print the confusion matrix
print(confusionMatrix(confusion.table))

#rpart accuracy: 0.98 and naiveBayes accuracy: 0.82
