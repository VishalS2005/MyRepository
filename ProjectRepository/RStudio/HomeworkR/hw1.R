c1 <- c('a','b','c','d','x','y','z')
v1 <- c(1,3,5,7,9,11,13)
v2 <- c(2,4,6,8,10,12,14)
hw1 <- data.frame(cbind(c1,v1,v2))
v3 <- v1 + v2
min_v3 <- min(v3)
max_v3 <- max(v3)
hw1 <- cbind(hw1, v3)
hw1
min_v3
max_v3