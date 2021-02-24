### 子模块

 * 主项目projectA, 子项目subOne
 
 * cd projectA
   > git submodule add git地址 （添加一个子模块）
      + 执行后会在projectA下生成一个subOne目录，但这是一个空目录
      + 生成.gitmodules文件
   > git add . 
   > git commit
   > git push 
      + 将绑定submodule内容提交
      
   > git submodule init
      + 需要初始化本地配置，可以执行cat .git/config看到配置中有了一个submodule
      + 如果不执行此命令，则git submodule update 不生效
      
 * git submodule update命令
   + 拉取主项目所依赖子项的版本内容，并将子项切换到这个关联的版本上
   + 主要是方便开发，对于projectA项目开发者不关心subOne的开启只要使用最新的就行。然后执行此命令就可以拿到最新依赖
      
 * subOne项目修改
   + 与普通git项目一样使用git pull, git push等操作
   + 主项目projectA发现subOne有修改后，projectA需要记录subOne的最新版本，所以projectA需要git commit; git push
   
 
 * 分支切换时同时更新子模块到相对应提交
   + git checkout branch_name --recurse-submodules
   > 如果projectA 所在分支：b1, 同时关联submodule的subOne的master分支最新提供
   > 如果projectA 所在分支：b2, 同时关联submodule的subOne的master2分支最新提供
   > 此时在master提交修改，同时b1提交对master的修改。
   > 此时并没有切subOne分支，直接切的是projectA到b2分支上后，也会有subOne未提交的提示
   > 如果使用此命令切分支时，就不出现未提交的提示了。（就是这个作用）