
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * 
 *
 * @date 2018-05-07
 * @author
 */
public class ResourceUtils {
    public static final String URL_PROTOCOL_FILE = "file";

    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever
            // happen).
            return new File(resourceUrl.getFile());
        }
    }

    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    public static Set<File> doFindPathMatchingFileResources(File file,
            String subPattern)
            throws IOException {

        File rootDir = file.getAbsoluteFile();
        return doFindMatchingFileSystemResources(rootDir, subPattern);
    }

    protected static Set<File> doFindMatchingFileSystemResources(File rootDir,
            String subPattern)
            throws IOException {
        Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
        return matchingFiles;
    }

    protected static Set<File> retrieveMatchingFiles(File rootDir, String pattern)
            throws IOException {
        if (!rootDir.exists()) {
            // Silently skip non-existing directories.
            return Collections.emptySet();
        }
        if (!rootDir.isDirectory()) {
            // Complain louder if it exists but is no directory.
            return Collections.emptySet();
        }
        if (!rootDir.canRead()) {
            return Collections.emptySet();
        }
        String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
        if (!pattern.startsWith("/")) {
            fullPattern += "/";
        }
        fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
        Set<File> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(fullPattern, rootDir, result);
        return result;
    }

    protected static void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result)
            throws IOException {
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            return;
        }
        Arrays.sort(dirContents);
        for (File content : dirContents) {
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory()) {
                if (!content.canRead()) {
                } else {
                    doRetrieveMatchingFiles(fullPattern, content, result);
                }
            } else {
                result.add(content);
            }
        }
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String, replacing spaces
     * with "%20" URI encoding first.
     * 
     * @param location the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }
}
