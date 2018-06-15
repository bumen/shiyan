package com.bmn.spring.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by Administrator on 2017/10/23.
 */
public class QvpEntityResolver extends DelegatingEntityResolver {

    private static final String QVP_SCHEMA_MAPPINGS_LOCATION = "spring/spring.schemas";

    private final ClassLoader classLoader;

    private final String schemaMappingsLocation;

    private volatile Map<String, String> schemaMappings;

    public QvpEntityResolver(ClassLoader classLoader) {
        super(classLoader);
        this.classLoader = classLoader;
        schemaMappingsLocation = QVP_SCHEMA_MAPPINGS_LOCATION;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputSource source = super.resolveEntity(publicId, systemId);
        if(source == null && systemId != null) {
            String resourceLocation = getSchemaMappings().get(systemId);
            if (resourceLocation != null) {
                Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
                try {
                    source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    return source;
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return source;
    }

    private Map<String, String> getSchemaMappings() {
        if (this.schemaMappings == null) {
            synchronized (this) {
                if (this.schemaMappings == null) {
                    try {
                        Properties mappings =
                                PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
                        Map<String, String> schemaMappings = new ConcurrentHashMap<String, String>(mappings.size());
                        CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
                        this.schemaMappings = schemaMappings;
                    }
                    catch (IOException ex) {
                        throw new IllegalStateException(
                                "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
                    }
                }
            }
        }
        return this.schemaMappings;
    }
}
