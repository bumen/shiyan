package com.bmn.ddd.action.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Context {

    public Map<Variable, Boolean> map = new HashMap<>();

    public void assign(Variable variable, Boolean b) {
        map.put(variable, b);
    }

    public boolean lookup(Variable variable) {
        return map.get(variable).booleanValue();
    }
}
