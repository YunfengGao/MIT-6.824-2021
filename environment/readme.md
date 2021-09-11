# 虚拟机环境配置

### 允许密码连接，默认密码为vagrant
```shell script
vi /etc/ssh/sshd_config

PermitRootLogin yes 
PasswordAuthentication yes

systemctl restart sshd
```

### 更新源
```shell script
yum install wget

cd /etc/yum.repos.d

mv CentOS-Base.repo CentOS-Base.repo.bk

wget http://mirrors.163.com/.help/CentOS6-Base-163.repo

yum makecache

yum install git

yum install gcc
```

### 安装GO
```shell script
wget https://mirrors.ustc.edu.cn/golang/go1.17.linux-amd64.tar.gz   

tar -C /usr/local -zxvf go1.17.linux-amd64.tar.gz

# 添加/usr/loacl/go/bin目录到PATH变量中
vi /etc/profile
# 在最后一行添加
export GOROOT=/usr/local/go
export PATH=$PATH:$GOROOT/bin
# 保存退出后source一下，使配置立即生效
source /etc/profile

$ go env -w GO111MODULE=on
$ go env -w GOPROXY=https://goproxy.cn,direct
```

### 安装JDK17
```shell script
# 下载地址 https://jdk.java.net/17/

wget https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_linux-x64_bin.tar.gz

mkdir /usr/local/java/

tar -C /usr/local/java/ -zxvf openjdk-17*_bin.tar.gz

vi /etc/profile

# 在最后一行添加
export JAVA_HOME=/usr/local/java/jdk-17
export PATH=${JAVA_HOME}/bin:$PATH

source /etc/profile

java -version
```
