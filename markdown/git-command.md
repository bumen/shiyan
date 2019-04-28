## Git命令

### windows 下中文乱码配置
 * 配置
   + i18n.logoutputencoding=utf-8
     - 好像也可以不配置
   + i18n.commitencoding=utf-8
     - 好像也可以不配置
   + 同时在home/下创建.bashrc 加入export LESSCHARSET=utf-8
   + idea 中使用bash.exe
   + windows窗口中使用git-bash.exe
   
 * idea Terminal替换成Git bash
   + 修改C:\Program Files\Git\etc\bash.bashrc
     > export LANG="zh_CN.UTF-8"
     > export LC_ALL="zh_CN.UTF-8"
   + 重启窗口
 
 * 查看windows窗口编码
   + 进入cmd 
   + chcp
   + chcp 65001   #换成utf-8代码页
   + chcp 936       #换成默认的gbk
   + chcp 437       #美国英语

### 把多个commit合并成一个commit
 * git rebase -i 

### 撤销命令
 * git checkout [file]
   + 恢复暂存区文件到工作区
 * git checkout [commit] [file]
   + 恢复某个commit到暂存区和工作区
 * git checkout .
   + 恢复暂存区所有文件到工作区
 * git reset [file]
   + 重置暂存区文件与本地仓库一致，但工作不变
 * git reset --head [file]
   + 重置暂存区与工作区与本地仓库一致
 * git reset [commit]
   + 重置当前分支指针到指定commit，同时重置暂存区，但工作区不变
 *  git reset --head [commit]  
    + 重置当前分支指针到指定commit，暂存 区与工作区与commit一致
 * git reset --keep [commit]
   + 重置当前分支指针到指定commit，暂存区与工作不变
 * git revert [commit]
   + 新建一个commit，用来撤销指定commit
   + 后者的所有变化都将被前者抵消，并且应用到当前分支
 
### 分支
 * git branch -vv
   + 查看本地分支与远程分支关联
 * git branch 
   + 列出本地分支
 * git branch [branch-name]
   + 新建分支，不切换
 * git branch [branch-name] [commit]
   + 新建分支，指向指定commit
 * git branch --track [branch-name][remote-branch]
   + 新建分支，与指定远程分支关联
 * git branch --set-upstream [branch-name][remote-branch]
   + 将本地分支与远程分支关联
 * git checkout -b [barnch-name]
   + 新建分支，并切换分支
 * git checkout -b not_master_branch origin/not_master_branch
   + 新建分支，并切换分支，并关联远程分支
   + 首先远程分支要pull到本地，通过在master分支执行git pull
 * git checkout [branch-name]
   + 切换分支
 * git checkout -
   + 切换到上一分支
 * git merge [branch-name]
   + 合并分支
 * git cherry-pick [commit]
   + 选择一个commit，合并到当前分支
 * git branch --unset-upstream
   + 解除本地与远程分支关联
 * git branch --delete --remotes <remote>/<branch>
   + 解除本地与远程分支关联
 * git branch -d [branch-name]
   + 删除本地分支
 * git push origin --delete [branch-name]
   + 删除远程仓库的分支
 * git branch -dr [remote/branch]
   + 删除本地记录的远程分支
   
### Log
 * git log --graph --name-status --pretty=oneline --abbrev-commit
 
### diff
 * git diff 
   + 比较工作区与Index区差异
 * git diff --cached | --staged 
   + 比较Index区与Head commit 差异
 * git diff HEAD
   + 比较工作区与Head commit 差异
 * git diff dev master
   + 两个分支HEAD commit 差异
 * git diff --stat
   + --stat 参数，显示简单的差异
 * git diff dev
   + 当前分叉HEAD commit 与 dev分支差异
 * git diff HEAD-- path
   + 当前path目录下HEAD commit与上一次提交差异
 * git diff HEAD^ HEAD 
   + 当前与上上次差异
 * git diff c1 c2 
   + c1 commit 与 c2 commit 差异
 
### merge
 * git merge --no-ff master
 
### stash 
 * git stash 
   + 保存当前工作区与暂存区内容
   + git stash save 'message'
 * git stash list 
   + 查看列表
 * git stash pop 
   + 取出最近一次stash放回工作区与暂存区
 * git stash drop [id]
   + 删除stash
 * git stash clear
   + 清除stash
 * git stash pop --index [id]
   + 恢复指定id
   
### 忽略已经跟踪的文件更新
 * 说明：
   + 这个命令来将已经track的文件标记一下, 使其不出现在更新列表中(git status不会列出标记过的文件)
   + 只在本地的项目中生效
 * git update-index --assume-unchanged .gitignore
   + 忽略.gitignore文件
 * git update-index --assume-unchanged /path/to/*
   + 忽略目录下的所有文件。对子目录无效