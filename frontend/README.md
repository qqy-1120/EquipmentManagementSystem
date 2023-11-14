# 拉取代码
1. 在 shell 里运行，后续命令也是
2. `git clone -b master xxx` (目前代码在master分支中)

# 配置环境
1. `node >= 16`
2. `npm >= 8.1.0`

# 安装依赖
## 安装 cnpm
`npm install -g cnpm@7.1.0`
## 安装依赖
`cnpm install`

# 开发
1. 确认后端 server 服务启动
2. `npm start`

# 部署
1.  `npm run build`
2.  将 build 产生的 dist 文件夹里的静态文件进行部署(ydroid 阿里云服务器 /var/www/userStudy 目录下)
3.  将代码更新同步到 gitlab 中：`git push origin master`