
#Open MySQL Library
library("RMySQL");

#Open a Connection

#CAMBIAR ESTA LINEA PARA CONECTARTE A TU BASE DE DATOS, POSIBLEMENTE HAY QUE AÑADIR
#EL PARAMETRO PASSWORD, CAMBIAR el user, y cambiar el host
con <- dbConnect(MySQL(), user="victor", 
                 dbname="Astrostatistics_pm_sh", host="127.0.0.1");

#Get modes for bandwidth 160 whose bandwidth id is 138
bw_id <- 138;
rs <- dbSendQuery(con, paste("SELECT id, muAcosD, muD FROM Mode WHERE bandwidth=", bw_id));
modes <- fetch(rs, n=-1);
dbClearResult(rs);

#Get samples id for this modes and bandwidths
rs <- dbSendQuery(con, paste("SELECT id_mode, id_source FROM Cluster_membership WHERE bandwidth=", bw_id ) );
membership <- fetch(rs, n=-1);
dbClearResult(rs);

#Get sources for each source id
rs <- dbSendQuery(con, paste("SELECT id, muAcosD, muD FROM Astrosources.Dataset_pleiades 
                  WHERE id IN (SELECT id_source FROM Cluster_membership WHERE bandwidth=", bw_id, ")" ) ) ;
sources <- fetch(rs, n=-1);
dbClearResult(rs);

#Luckily membership and sources have same order we can just merge tables
sources_and_modes <- cbind( membership$id_mode, sources);
colnames(sources_and_modes) <- c('id_mode', 'id', 'muAcosD', 'muD');

#First plot modes
plot( modes$muAcosD, modes$muD, pch=8, xlab="muAcosD", ylab="muD");

#Get an array of different colors for each mode
colors <- cbind( modes$id, rainbow( dim( modes )[1] ));
colnames(colors) <- c('id','color');

#Now for each mode print each cluster in a different color
for (mode_id in modes$id ) {
  #Get associated points for this cluster and color to display
  lines <- which( sources_and_modes$id_mode == mode_id);
  color <- colors[ which(colors[,1] == mode_id) ,2];
  #Plot points for this cluster
  points( sources_and_modes[lines,]$muAcosD, sources_and_modes[lines,]$muD, col = color);  
}

dbDisconnect(con);