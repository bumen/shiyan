package com.bmn.rt.lang.concurrent;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/7/23
 */
public class UserInfo implements Comparable<UserInfo>{

    private String userId;

    private long createTime;

    private int level;

    private int exp;

    private int rank = 1;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }


    @Override
    public int compareTo(UserInfo o) {
        if (this.level != o.level) {
            if(o.level < this.level) {
                rank = o.rank - 1 < rank ? o.rank -1 : rank;
                System.out.println(this.getUserId() + "-compare to great -:" + o.getUserId() + "rank1: " + rank + " rank2:" + o.rank);
                return -1;
            }
            rank = o.rank + 1 > rank ? o.rank + 1 : rank;
            System.out.println(this.getUserId() + "-compare to less -:" + o.getUserId() + "rank1: " + rank + " rank2:" + o.rank);
            return 1;
        }
        if (this.exp != o.exp) {
            if (o.exp < this.exp) {
                rank  = o.rank - 1;
                return -1;
            }
            rank  = o.rank + 1;
            return 1;
        }
        if (this.createTime != o.createTime) {
            if (this.createTime < o.createTime) {
                o.rank  = o.rank - 1;
                return -1;
            }
            rank  = o.rank + 1;
            return 1;
        }

        int v = this.userId.compareTo(o.userId);
        if (v > 0) {
            rank  = o.rank - 1;
            return v;
        }

        if (v < 0) {
            rank  = o.rank + 1;
            return v;
        }

        return v;
    }
}
