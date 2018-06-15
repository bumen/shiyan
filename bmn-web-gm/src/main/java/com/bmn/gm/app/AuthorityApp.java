package com.bmn.gm.app;

import com.bmn.gm.vo.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
public interface AuthorityApp {
    List<Component> getExplorer(long roleId) throws CloneNotSupportedException;
}
