# 虚拟机环境配置

## GO

### 允许密码连接，默认密码为vagrant
```shell script
vi /etc/ssh/sshd_config

PermitRootLogin yes 
PasswordAuthentication yes

systemctl restart sshd
```

### 更新源
```shell script
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

