package com.bmn.rt.lang.concurrent;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @date 2019-11-04
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcurrentSkipListSetScoreTest {

    public static void main(String[] args) {
        ConcurrentSkipListSet<TimeScore> scores = new ConcurrentSkipListSet<>();

        TimeScore score1 = new TimeScore(100, "123");
        TimeScore score2 = new TimeScore(103, "345");
        TimeScore score3 = new TimeScore(105, "789");
        scores.add(score1);
        scores.add(score2);
        scores.add(score3);

        testFloor(scores);

        System.out.println(scores.size());

        ConcurrentSkipListSet<TimeScore> clones = scores.clone();

        TimeScore scorex = new TimeScore(120, "123");

        scores.add(scorex);

        // scores.remove(new TimeScore(100, ""));
        for (TimeScore score : scores) {
            System.out.println(score.getScore());
        }

        for (TimeScore score : clones) {
            System.out.println(score.getScore());
        }


        System.out.println("---------------------------");

        TimeScore min = new TimeScore(0, "");

        TimeScore max = new TimeScore(105, "");

        Set<TimeScore> xx = scores.subSet(min, max);

        for (TimeScore score : xx) {
            System.out.println(score.getScore());
        }

    }


    private static void testFloor(ConcurrentSkipListSet<TimeScore> scores) {

        TimeScore score1 = new TimeScore(102);

        TimeScore score = scores.floor(score1);

        System.out.println("---------: " + score.getScore());
    }

    private static class TimeScore implements Comparable<TimeScore> {

        public TimeScore(int score) {
            this(score, "");
        }


        public TimeScore(int score, String rid) {
            this.score = score;
            this.rid = rid;
        }

        // 积分
        private int score;
        // 创建时间

        private String rid;


        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        @Override
        public int compareTo(TimeScore o) {
            if (this.score > o.getScore()) {
                return -1;
            }

            if (this.score < o.getScore()) {
                return 1;
            }
            return rid.compareTo(o.getRid());
        }
    }
}
