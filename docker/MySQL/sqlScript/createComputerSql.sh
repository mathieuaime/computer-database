#!/bin/bash
echo "SET GLOBAL max_allowed_packet=83886080; INSERT IGNORE INTO computer (id,name,company_id) VALUES " > insertComputer.sql
for i in {1..1000000}
do
   echo "($i,CONCAT('test',$i),1)," >> insertComputer.sql
done
