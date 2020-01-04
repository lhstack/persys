FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER lhstack@foxmail.com
RUN echo "Asia/Shanghai" > /etc/timezone
RUN mkdir -p /PerSystem
WORKDIR /PerSystem
ADD ./jar/per-sys.jar ./app.jar
EXPOSE 80
CMD java -Dserver.port=${SERVER_PORT:-80} \
-Dspring.freemarker.cache=${FREEMARKER_CACHE:-true} \
-Dspringboot.jackson.time-zone=GMT+8 \
-Dspring.profiles.active=${ACTIVE_ENV:-dev} \
-Dmongodb.username=${MONGO_USERNAME:-admin} \
-Dmongodb.password=${MONGO_PASSWORD:-123456} \
-Djava.security.egd=file:/dev/./urandom \
-Dmongodb.url=${MONGO_URL:-www.lhstack.xyz} \
-Dmongodb.port=${MONGO_PORT:-27017} \
-Dmongodb.database=${MONGO_DATABASE:-PermissionSys} \
-Dmysql.url=${MYSQL_URL:-localhost} \
-Dmysql.port=${MYSQL_PORT:-3306} \
-Dmysql.database=${MYSQL_DATABASE:-permission} \
-Dmysql.username=${MYSQL_USERNAME:-root} \
-Dmysql.password=${MYSQL_PASSWORD:-123456} \
-Dredis.host=${REDIS_HOST:-localhost} \
-Dredis.port=${REDIS_PORT:-6379} \
-Dredis.password=${REDIS_PASSWORD:-123456} \
-Dadmin.url=${ADMIN_URL:-http://localhost:9876} \
-Dspring.log.path=${LOG_PATH:-/logback} \
-Dspring.log.external-file=${LOG_EXT_PATH:-/logback/all.log} \
-Dscheduler.url=${SCHEDULER_URL:-localhost} \ 
-Dscheduler.port=${SCHEDULER_PORT:-3306} \ 
-Dscheduler.tb=${SCHEDULER_TB:-quartz} \ 
-Dscheduler.scheduler_mysql_password=${SCHEDULER_PASSWORD:-123456} \ 
-Dscheduler.scheduler_mysql_username=${SCHEDULER_USERNAME:-root} \ 
-Dfastdfs.http.prefix=${FASTDFS_PREFIX:-http://www.lhstack.xyz:8888} \
-Dpersys.version=${PERSYS_VERSION:-0.0.1} \
-Dpersys.client.authority.singingKey=${PERSYS_CLIENT_AUTHORITY_SINGINGKEY:-sdfgasghhsdfghws} \
-Dremote.sql.monitory.aes.singingKey=${REMOTE_SQL_MONITORY_AES_SINGINGKEY:-asdf153zdg5212sg} \
-Xms128m -Xmx512m -jar app.jar
