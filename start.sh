#!/bin/bash
nohup /app/jdk1.8.0_231/bin/java \
-server -Xms128m -Xmx256m \
-jar \
/app/cfp/cfp.jar \
--mysql.host=127.0.0.1 \
--mysql.db=cfp \
--mysql.username=root \
--mysql.password=root \
--spring.jpa.show-sql=false \
--server.port=80 \
--cfp.key=53cc83eba6f1eebachfsbkd4b74e87fb \
> /app/cfp/cfp.log 2>&1 & echo $! > /var/run/cfpartner_52465.pid