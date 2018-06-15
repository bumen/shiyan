package com.bmn.spring.load;

import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.io.Resource;

/**
 * Created by Administrator on 2017/8/7.
 */
public class QvpSourceExtractor implements SourceExtractor {
    @Override
    public Object extractSource(Object sourceCandidate, Resource definingResource) {
        return "qvp-" + sourceCandidate;
    }
}
