package com.bmn.core.astar;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;

public enum NodeDirection {

    None(0) {
        @Override
        public Range<Integer> angle() {
            return Range.singleton(-1);
        }
    },
    Right(1) {
        @Override
        public Range<Integer> angle() {
            return Range.singleton(0);
        }
    },
    RightBottom(2) {
        @Override
        public Range<Integer> angle() {
            return Range.open(0, 90);
        }
    },
    Bottom(3) {
        @Override
        public Range<Integer> angle() {
            return Range.singleton(90);
        }
    },
    LeftBottom(4) {
        @Override
        public Range<Integer> angle() {
            return Range.open(90, 180);
        }
    },
    Left(5) {
        @Override
        public Range<Integer> angle() {
            return Range.singleton(180);
        }
    },
    LeftTop(6) {
        @Override
        public Range<Integer> angle() {
            return Range.open(180, 270);
        }
    },
    Top(7) {
        @Override
        public Range<Integer> angle() {
            return Range.singleton(270);
        }
    },
    RightTop(8) {
        @Override
        public Range<Integer> angle() {
            return Range.open(270, 360);
        }
    };

    private static final RangeMap<Integer, NodeDirection> directionMapper;

    static {
        ImmutableRangeMap.Builder<Integer, NodeDirection> b = new ImmutableRangeMap.Builder<>();
        for (NodeDirection nodeDir : values()) {
            b.put(nodeDir.angle(), nodeDir);
        }
        directionMapper = b.build();
    }

    private int direct;

    private NodeDirection(int direct) {
        this.direct = direct;
    }

    public int getDirect() {
        return this.direct;
    }

    public abstract Range<Integer> angle();

    public static NodeDirection getDirection(double a) {
        NodeDirection nodeDir = directionMapper.get((int) a);
        return nodeDir != null ? nodeDir : None;
    }
}
