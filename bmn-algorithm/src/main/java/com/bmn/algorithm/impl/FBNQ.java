package com.bmn.algorithm.impl;

/**
 * 斐波那契数列
 *
 * f(0) = 0
 * f(1) = 1
 * f(n) = f(n-1) + f(n-2)  n>=2
 */
public class FBNQ {


    /**
     * 不要使用
     * reCalc(45) … 抱歉，我机器太慢，算不出来
     * @param n
     * @return
     */
    public int reCalc(int n) {
        if(n==0) return 0;
        if(n==1) return 1;
        return reCalc(n-1) + reCalc(n-2);
    }

    /**
     * 正向计算
     * @return
     */
    public int reCalc2(int n) {
        int[] arr = new int[n];
        arr[0]=0;
        arr[1]=1;
        for(int i=2;i<=n;i++){
            arr[i]=arr[i-1]+arr[i-2];
        }
        return arr[n];
    }

    public int Fibonacci(int n) {
        if(n<1) return 0;
        int[] fibonacci = new int[2];
        fibonacci[0] = 1;
        fibonacci[1] = 1;
        n-=2;
        while(n>0){
            int temp = fibonacci[0]+fibonacci[1];
            fibonacci[0] = fibonacci[1];
            fibonacci[1] = temp;
            n--;
        }
        return fibonacci[1];
    }


    public void calc() {

    }
}
