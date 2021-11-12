Git回滚某个文件

1、获取文件commit id （假设文件叫做config.properties ） eg：git log config.properties  

2、回退到对应版本  git reset 9808585efb08765a566bec1e406d7098fca83383 config.properties 

3、提交 git commit -m "back config"

4、选中该文件 git checkout config.properties 

5、push到远程目录  git push origin hotfix_1.7.x
