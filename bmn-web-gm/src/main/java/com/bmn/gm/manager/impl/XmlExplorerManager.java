package com.bmn.gm.manager.impl;

import com.bmn.gm.Service;
import com.bmn.gm.manager.ExplorerManager;
import com.bmn.gm.vo.Component;
import com.bmn.gm.vo.UIAccordion;
import com.bmn.gm.vo.UIBranch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/7/31.
 */
public class XmlExplorerManager implements ExplorerManager, Service {
    private static final Logger logger = LoggerFactory.getLogger(XmlExplorerManager.class);
    private Map<Integer, UIAccordion> accordions = new HashMap<>();


    private String filePath;

    @Override
    public List<UIAccordion> getAccordions() {
        List<UIAccordion> result = new ArrayList<>();
        result.addAll(accordions.values());
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }


    private void loadExplorer() throws ConfigurationException {
        XMLConfiguration conf = new XMLConfiguration();
        conf.setFileName(filePath);
        conf.setDelimiterParsingDisabled(true);     //取消内容自动分隔
        conf.load();

        List<HierarchicalConfiguration> accordionConfig = conf.configurationsAt("accordions.accordion");
        if(accordions == null) {
            throw new NullPointerException("explorer config error: need accordion");
        }

        Map<Integer, UIAccordion> accordionMap = new HashMap<>();
        for(HierarchicalConfiguration sub : accordionConfig) {
            UIAccordion accordion = new UIAccordion(sub.getInt("[@id]"));
            accordion.setOrder(sub.getInt("[@order]"));
            accordion.setTitle(sub.getString("[@title]"));
            accordion.setSelected(sub.getBoolean("[@selected]"));
            accordion.setContent(sub.getString("content"));
            accordion.setContentId(sub.getString("content.[@id]"));
            accordion.setContentType(sub.getString("content.[@type]"));
            String type = sub.getString("[@type]");
            if(type != null) {
                accordion.setType(type);
            }
            accordion.setChildren(loadBranch(sub, accordion.getId()));

            accordionMap.put(accordion.getId(), accordion);
        }

        accordions = accordionMap;
    }

    private List<Component> loadBranch(HierarchicalConfiguration config, int parentId) {
        List<Component> branchList = new ArrayList<>();
        List<HierarchicalConfiguration> branches = config.configurationsAt("children.branch");
        for (HierarchicalConfiguration sub : branches) {
            UIBranch branch = new UIBranch();
            branch.setId(sub.getInt("[@id]"));
//            branch.setParentId(parentId);
            branch.setText(sub.getString("[@text]"));
            String url = sub.getString("[@url]");
            if(url != null) {
                branch.setAttributeUrl(url);
            }
            String type = sub.getString("[@type]");
            if(type != null) {
                branch.setAttributeType(type);
            }
            branch.setChildren(loadBranch(sub, branch.getId()));

            branchList.add(branch);
        }

        return branchList;
    }

    @Override
    public void start() {
        try {
            loadExplorer();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
