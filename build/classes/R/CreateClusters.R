#Library for multivariate normal distributions
library(MASS)
setwd('/home/victor/UNED/Trabajo Fin Master/ClusterAnalysis/src/R');

#Create the two dimensional covariance and means matrix for the three clusters
Sigma1 <- matrix(c(0.25,0,0,0.25),2,2);
Mu1 <- matrix( c(-2, 0.25), 1, 2);

Sigma2 <- matrix(c(0.25,0,0,0.25),2,2);
Mu2 <- matrix( c(1, -1), 1, 2);

Sigma3 <- matrix(c(0.15,0,0,0.15),2,2);
Mu3 <- matrix( c(0, 3), 1, 2);

#Num samples
n <- 1000;

#Sample for the distribution, add cluster identification
samples <- cbind(seq(1,n), 1, mvrnorm(n, Mu1, Sigma1) );
samples2 <- cbind(seq(n+1,2*n), 2, mvrnorm(n, Mu2, Sigma2) );
samples3 <- cbind(seq(2*n+1,3*n), 3, mvrnorm(n, Mu3, Sigma3) );

#Plot graph
plot(samples[,3:4], col='red', xlim=c(-4,3), ylim=c(-3,5));
points(samples2[,3:4], col='green');
points(samples3[,3:4], col='blue');

#Create data frame for samples
data <- data.frame( rbind( samples, samples2, samples3) );
colnames( data ) <- c('SampleId', 'ModeId', 'X', 'Y');

#Create data frame for modes
mode1 <- cbind(1, 0.5, Mu1);
mode2 <- cbind(2, 0.5, Mu2);
mode3 <- cbind(3, 0.5, Mu3);
modes <- data.frame( rbind( mode1, mode2, mode3 ));
colnames( modes ) <- c( 'ModeId', 'Bandwidth', 'X', 'Y');

#Save to CSV sample data
write.table(data, append<-FALSE, file="samples.csv", eol="\r", 
            quote<-FALSE, sep<-c(','), row.names=FALSE );

write.table(modes, append<-FALSE, file="modes.csv", eol="\r", 
            quote<-FALSE, sep<-c(','), row.names=FALSE );