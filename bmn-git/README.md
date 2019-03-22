## gitignore_global
### Git ignore file for global settings

### 使用方法
 * 将.gitignore_global放到相应的目录下，例如~/.gitnore_global
 * 执行：git config --global core.excludesfile ~/.gitignore_global 
 * 其他注意事项
 * gitignore的优先级

 * 同目录或者父目录（最高至顶层目录）中的.gitignore
 * 全局变量core.excludefiles所指定的文件
   为此，如果需要为某个工程添加额外的gitignore，请在该工程的根目录下添加针对该工程的.gitignore，将该工程的特殊忽略文件加进去即可