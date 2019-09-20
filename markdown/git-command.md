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
   
### Git Bash配置
``` 
$ git config --global --list
core.quotepath=false
core.excludesfile=C:\Users\playcrab\Documents\gitignore_global.txt
core.autocrlf=false
core.safecrlf=false
difftool.sourcetree.cmd='' "$LOCAL" "$REMOTE"
mergetool.sourcetree.cmd=''
mergetool.sourcetree.trustexitcode=true

```

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
   
 * git reset --mixed 与 git reset 一样
   + 不删除工作空间改动代码，撤销commit，并且撤销git add . 操作
 
 * git reset --soft
   + 不删除工作空间改动代码，撤销commit，不撤销git add . 
   
 * git reset --hard
   + 删除工作空间改动代码，撤销commit，撤销git add . 
   
 * git reset --soft HEAD^
   + 也可以写成HEAD~1
   + HEAD~2，回上两个版本
   + 则回退到上一个版本
   
### revert
 * 它会产生一个新的提交，虽然代码回退了，但是版本依然是向前的
 
 * revert 常规 commit (git commit生成的commit)
   + 使用 git revert <commit id> 即可，git 会生成一个新的 commit，将指定的 commit 内容从当前分支上撤除。
   
 
 * revert merge commit
   + 这时需要添加 -m 选项以代表这次 revert 的是一个 merge commit
   + 因为merge的commit是由两个commit合并得到的，所以revert时需要指定要恢复到哪个commit
      - Merge: 856ad95e3 e631ad02e
      - 1 表示856ad95e3
      - 2 表示e631ad02e
   + 如果要回复1， 丢掉2
      - git revert -m 1 856ad95e3
      
 * 注意
   + 使用revert HEAD是撤销最近的一次提交。如果执行了一次revert后版本为G, 如果再revert即G1, 则表示G1 revert G
   相当于没有改变
   + 如果revert的不是最近一次提交，那么一定有代码冲突。需要你合并代码。只需要把当前代码全部去掉。保留之前版本即可。
 
   
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
 * git branch -m old new
   + 重命名
 * git reflog show --date=iso [branch-name]
   + 查看分支创建时间
   
### Log
 * git log --graph --name-status --pretty=oneline --abbrev-commit
 
 * git reflog
  
 * git log  -p -2 --date=iso -- demo-pig/src/main/java/com/bmn/test/Test.java
   + 查看某路径最近2次修改
   ``` 
    /e/project/bmn/haitang (master)
    $ git log  -p -2 --date=iso -- demo-pig/src/main/java/com/bmn/test/Test.java
  
   ```
   
 * git log --pretty=format:"%h - %an, %ar : %s"
   + 占位符
   ``` 
    %H	提交对象（commit）的完整哈希字串
    %h	提交对象的简短哈希字串
    %T	树对象（tree）的完整哈希字串
    %t	树对象的简短哈希字串
    %P	父对象（parent）的完整哈希字串
    %p	父对象的简短哈希字串
    %an	作者（author）的名字
    %ae	作者的电子邮件地址
    %ad	作者修订日期（可以用 -date= 选项定制格式）
    %ar	作者修订日期，按多久以前的方式显示
    %cn	提交者(committer)的名字
    %ce	提交者的电子邮件地址
    %cd	提交日期
    %cr	提交日期，按多久以前的方式显示
    %s	提交说明
   ```
 * git log 参数
   ``` 
    -p	按补丁格式显示每个更新之间的差异。
    --word-diff	按 word diff 格式显示差异。
    --stat	显示每次更新的文件修改统计信息。
    --shortstat	只显示 --stat 中最后的行数修改添加移除统计。
    --name-only	仅在提交信息后显示已修改的文件清单。
    --name-status	显示新增、修改、删除的文件清单。
    --abbrev-commit	仅显示 SHA-1 的前几个字符，而非所有的 40 个字符。
    --relative-date	使用较短的相对时间显示（比如，“2 weeks ago”）。
    --graph	显示 ASCII 图形表示的分支合并历史。
    --pretty	使用其他格式显示历史提交信息。可用的选项包括 oneline，short，full，fuller 和 format（后跟指定格式）。
    --oneline	--pretty=oneline --abbrev-commit 的简化用法。
    
    -(n)	仅显示最近的 n 条提交
    --since, --after	仅显示指定时间之后的提交。
    --until, --before	仅显示指定时间之前的提交。
    --author	仅显示指定作者相关的提交。
    --committer	仅显示指定提交者相关的提交。
    
   ```
   + 还可以给出若干搜索条件，列出符合的提交。用 --author 选项显示指定作者的提交，用 --grep 选项搜索提交说明中的关键字。
     （请注意，如果要得到同时满足这两个选项搜索条件的提交，就必须用 --all-match 选项。否则，满足任意一个条件的提交都会被匹配出来）
   + 另一个真正实用的git log选项是路径(path)，如果只关心某些文件或者目录的历史提交，可以在 git log 选项的最后指定它们的路径。
   因为是放在最后位置上的选项，所以用两个短划线（--）隔开之前的选项和后面限定的路径名。
    
   
 
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
 
### commit 
 * git commit -- amend
   + 修改当前提交的注释
   
 * commit有两种
   + 一种是git commit生成的
   ``` 
        commit 856ad95e37bbb8a327f6909168e4c1dfc749b893
        Author: zyq <zyq@qq.com>
        Date:   Thu Jun 27 18:08:36 2019 +0800
        
            优化

   ```
   + 一种是git merge后，自动生成的commit
   ``` 
        Merge: 856ad95e3 e631ad02e
        Author: zyq <zyq@qq.com>
        Date:   Thu Jun 27 18:55:34 2019 +0800
        
            Merge branch 'master' of gitlab.xx-inc.com:sv/server/sr

   ```
   
 
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
   
### git tag
 * git tag -a tag_name -m "common"
 * git push origin tag_name 
 * git tag -d tag_name
   + 删除本地tag
 * git push origin :refs/tags/tag_name
   + 删除远程tag
   
### 忽略已经跟踪的文件更新
 * 说明：
   + 这个命令来将已经track的文件标记一下, 使其不出现在更新列表中(git status不会列出标记过的文件)
   + 只在本地的项目中生效
 * git update-index --assume-unchanged .gitignore
   + 忽略.gitignore文件
 * git update-index --assume-unchanged /path/to/*
   + 忽略目录下的所有文件。对子目录无效
   
### git pull之后冲突
 * git pull其实是两步操作
   + git fetch: 先把代码拉下来
   + git merge：再与本地merge
   
 * 正常情况下会自动merge成功。生成commit
 
 * 当本地代码与远程冲突后，merge失败
   + 此时fetch下来的所有文件都会放到暂存区
     - 不管是与本地有冲突的文件还是没有冲突的别人修改过的文件都会放到暂存区
     因为fetch之后，相当于有了与我本地不同的文件。
     因为本地不同的文件会放到暂存区
   + 此时需要手动解决冲突
   + 解决完冲突后，将所有fetch下的暂存区的文件，继续add后
   + 此时相当于merge完成了。会自动生成commit
   + 然后再push到远程
   
 * 问题
   + 在merge冲突后，再解决完冲突后没有将暂存区中其它文件add后
   + 则不会生成commit. 导致本地少了之前远程提交的版本
   + 如果此时Push, 则会全本地commit，覆盖远程commit。导致代码丢失
   
 * 解决问题
   + 如果真出现这个问题。则需要其它人reset
   + git reset （一个比较新的版本）
   + git checkout -- . 回滚所有提交错的commit
   + git push origin -f 强制推送到远程
   > 注意，此时别人在git pull时，还是有问题。可能需要自己再reset到远程最新版本。
   > 然后再将不是自己修改的文件，从stage空间还原最新
   > 最后在重新提交自己的修改
   > 再git push 就可以了
   
 * 正确流程
   1. 
     + 可以本地commit后
     + 再git pull
     + 再解决冲突， 
     + 再将所有fetch下来的文件add
     + 再git push 
   2. 
     + 可以将本地git stash 
     + 再git pull。不会有冲突
     + 再git stash pop
     + 可能会有冲突
     + 解决冲突（肯定是自己与别人冲突，不会出现1情况里自己未修改的文件）
     + 再git add
     + 再git push
     
### reset注意（远程仓库版本回退方法）
   + 两个人本地都与远程同步到最新commit-10
   + 如果其中一个人reset了一个之前的commit5(即回滚了)
   + 然后git push origin -f 强制推送到远程
   + 然后第2个人执行git pull 拉取远程最新commit(引时已经是一个回滚的版本)
   + 此时第2个人的本地也会回滚到commit5
   + 然后第2个人的本地会多出commit5到commit10之间未推送到远程的commit
   + IDEA会提示有5个commit需要push
      - 这5个commit是之前远程上最新的，但可能是有问题的commit
   + 如果第2个人没有注意，继续直接push, 则右将commit10推送到了远程。导致远程又有了commit5-commit10之间错误版本的数据
   + 第2个正确操作
     - 要不就是本地在之前没有拉取过commit5到commit10之间的版本（即本地版本是commit5之前版本）。则现在直接git pull后就是正确的commit5版本
     - 要不就需要git reset HEAD, 将这几个commit还原
     - 然后发现错误的修改数据。需要自己用commit5版本上最新的文件覆盖即可
     
   + 还需要注意的问题（不会冲突）
     - 如果commit5 有一个版本文件， commit6有一个版本文件（但是错误数据文件，所以回滚到5）
     - 也被推送到了远程。第2个人也拉取到了本地。
     - 因为第一个人发现commit6文件有问题，所以reset到5。又推送到远程。通知第2个人之前版本有问题，需要重新拉取远程最新
     - 第2个人拉取远程最新commit5中的文件，因为是reset所以拉取后不会产生冲突。
     - 此时commit6 相当于你自己在本地提交了一个commit(因为原头这个commit6也之前从远程拉取的，可能不是自己提交的东西)
     
     
     
     
### HEAD指针

 * 本地commit2次，还没有Push
``` 
    $ git log
    commit 211b18f057d48c5aeb565dcabe2a2d5f0c8645b6 (HEAD -> master)
    Author: bmn <562655151@qq.com>
    Date:   Thu Jun 27 19:03:08 2019 +0800
    
        修改markdown
    
    commit f591e32c4f65cb6b64bf6dd684232db64b5f68ba
    Author: bmn <562655151@qq.com>
    Date:   Thu Jun 27 19:01:02 2019 +0800
    
        update markdown
    
    commit a197eb3a18df704e1d567b3938cd7d6a876905a8 (origin/master, origin/HEAD)

```
* 执行git push origin后
  + 三个指针指向一样的commit
``` 
    $ git log
    commit 211b18f057d48c5aeb565dcabe2a2d5f0c8645b6 (HEAD -> master, origin/master, origin/HEAD)
    Author: bmn <562655151@qq.com>
    Date:   Thu Jun 27 19:03:08 2019 +0800
    
        修改markdown
    
    commit f591e32c4f65cb6b64bf6dd684232db64b5f68ba
    Author: bmn <562655151@qq.com>
    Date:   Thu Jun 27 19:01:02 2019 +0800
    
        update markdown

```
   