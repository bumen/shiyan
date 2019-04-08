## eclipse 配置

### Mac下使用安装版eclipse的配置
#### 配置Package Explorer字体大小
 * 查看eclipse.ini文件，查看配置主题目录 
   ``` 
    --launcher.library
    /Users/playcrab/.p2/pool/plugins/org.eclipse.equinox.launcher.cocoa.macosx.x86_64_1.1.1000.v20190125-2016
   ```
 * 进入auncher.library配置的目录  
   `/Users/playcrab/.p2/pool/plugins/`
 * 进入配置主题目录  
   `org.eclipse.ui.themes_1.2.400.v20190223-1254/css`
   + 不同版本名称不同
 * 编辑e4_default_mac.css
   + 如果eclipse使用的是default主题，则修改这个文件。其它主题则修改对应文件 
   + 在文档最后一行添加 
   ``` 
    #org-eclipse-jdt-ui-PackageExplorer Tree,
    #org-eclipse-ui-navigator-ProjectExplorer Tree,
    #org-eclipse-ui-views-ContentOutline Tree,
    #PerspectiveSwitcher ToolBar {
      font-size: 17px;
    }
   ```
 * 重启eclipse生效