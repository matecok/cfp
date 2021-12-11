> **说明：**
> 这是一个免费开源的CLOUDFLARE PARATNERS!不分商业或者个人！所以无授权可言

>**对于java开发者：**
您可以下载并进行二次开发，本功能不包含找回密码，但可以自由实现。

>**对于不懂java者：**
可进行定制开发， 现有用户可以直接对接您项目中的用户

>**对于小白：**
您可以直接下载根目录的jar进行直接启动，参数在 start.sh中，自行修改，如果这些参数您都看不懂，建议忽略本软件

>**使用方法**
本软件只列举在RedHat系部署流程，其他自行操作

>安装目录:  /app
```
mkdir /app
cd /app
wget -O jdk-8u231-linux-x64.tar.gz https://github.com/matecok/tools/blob/main/jdk-8u231-linux-x64.tar.gz?raw=true
tar -zxvf jdk-8u231-linux-x64.tar.gz && rm -rf jdk-8u231-linux-x64.tar.gz

echo 'export JAVA_HOME=/app/jdk1.8.0_231' >> /etc/profile
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> /etc/profile
source /etc/profile
mkdir /app/cfp/ && cd /app/cfp
wget -O cfp.jar https://github.com/matecok/cfp/blob/main/cfp.jar?raw=true
wget -O start.sh https://github.com/matecok/cfp/blob/main/start.sh?raw=true
wget -O stop.sh https://github.com/matecok/cfp/blob/main/stop.sh?raw=true
chmod +x /app/cfp/start.sh /app/cfp/stop.sh

touch /usr/lib/systemd/system/cfp.service
cat << EOF > /usr/lib/systemd/system/cfp.service
[Unit]
Description=cfpartnerService
After=syslog.target network.target remote-fs.target nss-lookup.target
[Service]
Type=forking
ExecStart=/app/cfp/start.sh
ExecStop=/app/cfp/stop.sh
PrivateTmp=true
[Install]
WantedBy=multi-user.target
EOF
```
>在此之后你可以使用系统管理服务命令进行管理cfp
> 例如
```
#设置开机自启，并立即启动
systemctl enable --now cfp
#仅设置开机自启
systemctl enable cfp
#仅启动
systemctl start cfp
#仅停止
systemctl stop cfp
#重启cfp
systemctl restart cfp
```

