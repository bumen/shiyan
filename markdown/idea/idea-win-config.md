## IDEA 配置
 * version: 2019.1 Ultimate Edition
 
### IDEA System,Config文件位置调整
 * 默认位置：c:\Documents and Settings\${User}\.IntelliJIdea2019.1）
 * 作用：存放IDEA的运行时必要的数据文件（配置、index等）
   + 项目中的文件通常会比较多，IDEA会为每个文件建立索引的，所以会占用不少的磁盘空间。我们可以将其转移到其它磁盘。
 * 调整位置
   + 在${IDEA_HOME}/bin目录下有一个idea.properties文件，假设你要将目录 调整到D：/idea_data目录下
   ``` 
   1） 关闭IDEA
   2） 去掉idea.config.path、idea.system.path前的注释。
   
   3）将idea.config.path的值调整为d:/idea_data/config，将idea.system.path调整为d:/idea_data/system
   
   4）从c:\Documents and Settings${User}\.IntelliJIdea2019.1目录下，将.IntelliJIdea2017.3目录下的内容全部拷贝到d:/idea_data目录下。
   
   5）重启idea，在C盘并不会重新生成索引文件（这说明你的配置生效了）。
   ```

### Appearance
 * Theme
   + Darcula: 一部美剧中的一个吸血鬼的名字，中文叫德古拉
   
 * Use custom font
   + Courier New 
   + 14 size
 * System Settings
   + Updates = false
 
### Keymap
 * Default copy
 
### Editor
 * General
   + Auto Import:
     - Java: 
     1. [x] Add unambiguous imports on the fly: 导到包时打开模糊推段
     2. [x] Optimize imports on the fly(for current project): 自动删除没用的包
 * Font
   + font: Courier New
   + size: 15
 * Color Scheme
   + Scheme : Darcula
 * Code Style
   + Scheme : GoogleStyle
   + General:
     - Line separator: Unix and macOS
     - Hard Wrap at : 100
   
   + Java
     - [ ] Use tab character
     - Tab size: 4
     - Indent: 4
       
   
 * File and Code Templates
   + Includes
     - File Header
     ``` 
        /**  
         * @author baw@qq.com
         * @date ${DATE}
        */
     ```
 * File Encodings
   + Global Encoding: utf-8
   + Project Encoding: utf-8
   + Default encoding for properties files: utf-8
   + Create Utf-8 Files: with no BOM
 
 * Live Templates
 
 * File Types
   + Ignore files and folders
     - 添加要隐藏的文件：如*.iml
 

   
 

### Plugins
* Alibaba Java Coding Guidelines
* BashSupport
* EmmyLua
* google-java-format
* jclasslib Bytecode viewer
* Protobuf Support
* Python 


### Version Controls
 * Git
   + Path to Git executable
     - D:\Program Files\Git\bin\git.exe
     
### Build, Execution, Deployment
 * Build Tools
   + Maven
     - home directory: E:/data/home/user00/playcrab/usr/apache-maven-3.6.1
     - User settings file: C:\Users\playcrab\.m2\settings.xml (默认配置)
     - Local repository: C:\Users\playcrab\.m2\repository (默认配置)
   
### Languages & Frameworks
 
 
### Tools
 * Terminal
   + 配置Git bash
        - Shell path= D:\Program Files\Git\bin\bash.exe
   + 配置窗口
        - View Mode: Undock
        - Move to: Right Bottom
   + 快捷键
        - 打开与关闭窗口  
        `alt + F12` 
        - 打开窗口并新建一个命令窗口  
        `ctrl + alt + n` 
        - 在打开的窗口中左右移动命令窗口  
        `alt + 左右箭头` 
        
### Other Settings

### Experimental


### 问题

#### java文件过大导致不能编译
 * 场景：
   + 更新代码后，突然发现代码中出现很多红色错误提示。提示找不到类引用
   + 此时文件图标也变成了显示java文件图标J. 正常图标为^C
   + 但是查看发现被引用的类，编译都很正常

 * 查看被引用的类，打开发现idea提示
   + The File size (2.58 MB) exceeds configured limit (2.56 MB). Code insight features are not available.

 * 解决
   + 关闭idea
   + 修改idea.properties
    ```
    # idea.max.intellisense.filesize=2500
    idea.max.intellisense.filesize=5000
    ```

    

