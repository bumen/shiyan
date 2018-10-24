package com.bmn.algorithm.impl;

/**
 * 跳台阶（动态规划）
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
 */
public class DynamicPlan {
    public int JumpFloor(int target) {
        if(target < 1) return 0;
        int[] DP = new int[3];
        DP[0] = 1;
        DP[1] = 2;
        DP[2] = DP[0]+DP[1];
        if(target<=3)
            return DP[target-1];
        for(int i =4;i<=target;i++){
            DP[0] = DP[1];
            DP[1] = DP[2];
            DP[2] = DP[0]+DP[1];
        }
        return DP[2];
    }
}
