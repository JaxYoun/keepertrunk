---
title: Oppenssl 生成证书文档
tag: 前端开发
publishDate: 2018-03-13
---

## 1. 首先下载 openssl(windows 环境)。

[下载地址](http://slproweb.com/products/Win32OpenSSL.html),本篇文档用的 32 位。

## 2. 下载完毕后，安装。

## 3. 解压压缩包后，进入 bin 目录,建立一个 ca 的空文件夹

## 4. 下面根据命令进行操作

---

> 1. openssl req -new -out ca/ca-req.csr -key ca/ca-key.pem -config ./openssl.cnf

      注释：这里注意看你的openssl.cnf的文件位置

> 2. 根据提示进行信息的录入：

      Country Name (2 letter code) [AU]:CN
      State or Province Name (full name) [Some-State]:SC
      Locality Name (eg, city) []:Chengdu
      Organization Name (eg, company) [Internet Widgits PtyLtd]:troy
      Organizational Unit Name (eg, section) []:troy
      Common Name (eg, YOUR name) []:localhost
       注释：一定要写服务器所在的ip地址//这里是本机测试，所以我写localhost
      Email Address []:
      Please enter the following 'extra' attributes
      to be sent with your certificate request
      A challenge password []:
      An optional company name []

> 3. openssl x509 -req -in ca/ca-req.csr -out ca/ca-cert.pem -signkey ca/ca-key.pem -days 3650
> 4. keytool -genkey -alias troy-security -validity 365 -keyalg RSA -keysize 1024 -keypass Troy1q2w3e%-storepass Troy1q2w3e% -keystore troy-security.jks

        这里的troy-security可自行更改

> 5. 继续信息录入：

        您的名字与姓氏是什么：
        您的组织单位是什么：
        您所在的城市或区域名称是什么：
        您所在的省/市/自治区名称是什么：
        该单位的双字母国家/地区代码是什么：

> 6. keytool -certreq -alias troy-security -sigalg MD5withRSA -file troy-security.csr -keypass Troy1q2w3e% -keystore troy-security.jks -storepass Troy1q2w3e%
> 7. openssl x509 -req -in troy-security.csr -out troy-security.pem -CA ca/ca-cert.pem -CAkey ca/ca-key.pem -days 3650 -set_serial 1
> 8. keytool -import -v -trustcacerts -keypass Troy1q2w3e% -storepass Troy1q2w3e% -alias root -file ca/ca-cert.pem -keystore troy-security.jks
> 9. keytool -import -v -trustcacerts -storepass Troy1q2w3e% -alias troy-security -file troy-security.pem -keystore troy-security.jks
> 10. keytool -import -alias troy-security-ca -trustcacerts -file ca/ca-cert.pem -keystore troy-security-trust.jks

    *****
