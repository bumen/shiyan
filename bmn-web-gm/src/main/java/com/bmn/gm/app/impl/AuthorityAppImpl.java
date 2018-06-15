package com.bmn.gm.app.impl;

import com.bmn.gm.FaceContext;
import com.bmn.gm.app.AuthorityApp;
import com.bmn.gm.vo.Component;
import com.bmn.gm.vo.UIAccordion;
import com.bmn.gm.vo.UIBranch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
public class AuthorityAppImpl implements AuthorityApp {

    public List<Component> getExplorer(long roleId) throws CloneNotSupportedException {
        List<UIAccordion> list = FaceContext.getExplorerManager().getAccordions();

        List<Integer> authorities = new ArrayList<>();
        authorities.add(100102);
        authorities.add(10000000);
        authorities.add(30010100);
        authorities.add(200);
        authorities.add(300100);
        authorities.add(300101);
        authorities.add(300102);


        return findBranch(authorities, list, new Component.ComponentFilter() {
            @Override
            public void doFilter(List<Component> components) {
                Iterator<Component> it = components.iterator();
                while (it.hasNext()) {
                    Component component = it.next();
                    if(component instanceof UIBranch) {
                        UIBranch branch = (UIBranch)component;
                        if (branch.getType().equals("btn")) {
                            it.remove();
                        }
                    }
                }
            }
        });
    }

    /**
     * 根据权限id列表，返回权限树
     * @param authorities   权限id列表
     * @param children  原树
     * @return  权限树
     * @throws CloneNotSupportedException
     */
    private List<Component> findBranch(List<Integer> authorities, List<? extends Component> children, Component.ComponentFilter filter) throws CloneNotSupportedException {
        if (children.isEmpty()) {
            return null;
        }

        List<Component> result = new ArrayList<>();
        for (Component child : children) {
            if (authorities.contains(child.getId())) {
                Component cl = child.clone();
                cl.getChildren().clear();
                result.add(cl);
            } else {
                List<Component> branches = findBranch(authorities, child.getChildren(), filter);
                if(branches != null && !branches.isEmpty()) {
                    Component cl = child.clone();
                    cl.setChildren(branches);
                    cl.filter(filter);
                    result.add(cl);
                }
            }
        }
        return result;
    }
}
