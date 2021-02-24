package com.bmn.rt.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import sun.util.locale.BaseLocale;
import sun.util.locale.LocaleObjectCache;

/**
 * Created by Administrator on 2017/10/10.
 */
public class QvpControl extends ResourceBundle.Control {

    @Override
    public List<String> getFormats(String baseName) {
        if (baseName == null) {
            throw new NullPointerException();
        }
        return FORMAT_DEFAULT;
    }
    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
        if (baseName == null) {
            throw new NullPointerException();
        }
        Locale defaultLocale = Locale.getDefault();
        return locale.equals(defaultLocale) ? null : defaultLocale;
    }
    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        if (baseName == null || locale == null) {
            throw new NullPointerException();
        }
        return 1000;
    }
    @Override
    public List<Locale> getCandidateLocales(String baseName, Locale locale) {
        if (baseName == null) {
            throw new NullPointerException();
        }
        return new ArrayList<>(CANDIDATES_CACHE.get(BaseLocale.getInstance(locale.getLanguage(), locale.getScript(), locale.getCountry() , locale.getVariant())));
    }

    private static final CandidateListCache CANDIDATES_CACHE = new CandidateListCache();

    private static class CandidateListCache extends LocaleObjectCache<BaseLocale, List<Locale>> {
        @Override
        protected List<Locale> createObject(BaseLocale base) {
            String language = base.getLanguage();
            String script = base.getScript();
            String region = base.getRegion();
            String variant = base.getVariant();

            // Special handling for Norwegian
            boolean isNorwegianBokmal = false;
            boolean isNorwegianNynorsk = false;
            if (language.equals("no")) {
                if (region.equals("NO") && variant.equals("NY")) {
                    variant = "";
                    isNorwegianNynorsk = true;
                } else {
                    isNorwegianBokmal = true;
                }
            }
            if (language.equals("nb") || isNorwegianBokmal) {
                List<Locale> tmpList = getDefaultList("nb", script, region, variant);
                // Insert a locale replacing "nb" with "no" for every list entry
                List<Locale> bokmalList = new LinkedList<>();
                for (Locale l : tmpList) {
                    bokmalList.add(l);
                    if (l.getLanguage().length() == 0) {
                        break;
                    }
                    bokmalList.add(buildLocale("no", l.getScript(), l.getCountry(),
                            l.getVariant()));
                }
                return bokmalList;
            } else if (language.equals("nn") || isNorwegianNynorsk) {
                // Insert no_NO_NY, no_NO, no after nn
                List<Locale> nynorskList = getDefaultList("nn", script, region, variant);
                int idx = nynorskList.size() - 1;
                nynorskList.add(idx++, buildLocale("no", "", "NO", "NY"));
                nynorskList.add(idx++, buildLocale("no", "", "NO", ""));
                nynorskList.add(idx++, buildLocale("no", "", "", ""));
                return nynorskList;
            }
            // Special handling for Chinese
            else if (language.equals("zh")) {
                if (script.length() == 0 && region.length() > 0) {
                    // Supply script for users who want to use zh_Hans/zh_Hant
                    // as bundle names (recommended for Java7+)
                    switch (region) {
                        case "TW":
                        case "HK":
                        case "MO":
                            script = "Hant";
                            break;
                        case "CN":
                        case "SG":
                            script = "Hans";
                            break;
                    }
                } else if (script.length() > 0 && region.length() == 0) {
                    // Supply region(country) for users who still package Chinese
                    // bundles using old convension.
                    switch (script) {
                        case "Hans":
                            region = "CN";
                            break;
                        case "Hant":
                            region = "TW";
                            break;
                    }
                }
            }

            List<Locale> list = getDefaultList(language, script, region, variant);
            for(Locale locale : list) {
                System.out.println(locale);
            }
            return list;
        }

        private static List<Locale> getDefaultList(String language, String script, String region, String variant) {
            List<String> variants = null;

            if (variant.length() > 0) {
                variants = new LinkedList<>();
                int idx = variant.length();
                while (idx != -1) {
                    variants.add(variant.substring(0, idx));
                    idx = variant.lastIndexOf('_', --idx);
                }
            }

            List<Locale> list = new LinkedList<>();

            if (variants != null) {
                for (String v : variants) {
                    list.add(buildLocale(language, script, region, v));
                }
            }
            if (region.length() > 0) {
                list.add(buildLocale(language, script, region, ""));
            }
            if (script.length() > 0) {
                list.add(buildLocale(language, script, "", ""));

                // With script, after truncating variant, region and script,
                // start over without script.
                if (variants != null) {
                    for (String v : variants) {
                        list.add(buildLocale(language, "", region, v));
                    }
                }
                if (region.length() > 0) {
                    list.add(buildLocale(language, "", region, ""));
                }
            }
            if (language.length() > 0) {
                list.add(buildLocale(language, "", "", ""));
            }
            // Add root locale at the end
            list.add(Locale.ROOT);

            return list;
        }
    }
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                    ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);

        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends ResourceBundle> bundleClass
                        = (Class<? extends ResourceBundle>)loader.loadClass(bundleName);

                // If the class isn't a ResourceBundle subclass, throw a
                // ClassCastException.
                if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
                    bundle = bundleClass.newInstance();
                } else {
                    throw new ClassCastException(bundleClass.getName()
                            + " cannot be cast to ResourceBundle");
                }
            } catch (ClassNotFoundException e) {
            }
        } else if (format.equals("java.properties")) {
            final String resourceName = toResourceName0(bundleName, "properties");
            System.out.println("start new bundle: " + resourceName);
            if (resourceName == null) {
                return bundle;
            }
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(
                        new PrivilegedExceptionAction<InputStream>() {
                            public InputStream run() throws IOException {
                                InputStream is = null;
                                if (reloadFlag) {
                                    URL url = classLoader.getResource(resourceName);
                                    if (url != null) {
                                        URLConnection connection = url.openConnection();
                                        if (connection != null) {
                                            // Disable caches to get fresh data for
                                            // reloading.
                                            connection.setUseCaches(false);
                                            is = connection.getInputStream();
                                        }
                                    }
                                } else {
                                    is = classLoader.getResourceAsStream(resourceName);
                                }
                                return is;
                            }
                        });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(stream);
                } finally {
                    stream.close();
                }
            }
        } else {
            throw new IllegalArgumentException("unknown format: " + format);
        }
        return bundle;
    }
    @Override
    public String toBundleName(String baseName, Locale locale) {
        if (locale == Locale.ROOT) {
            return baseName;
        }

        String language = locale.getLanguage();
        String script = locale.getScript();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        if (language == "" && country == "" && variant == "") {
            return baseName;
        }

        StringBuilder sb = new StringBuilder(baseName);
        sb.append('_');
        if (script != "") {
            if (variant != "") {
                sb.append(language).append('_').append(script).append('_').append(country).append('_').append(variant);
            } else if (country != "") {
                sb.append(language).append('_').append(script).append('_').append(country);
            } else {
                sb.append(language).append('_').append(script);
            }
        } else {
            if (variant != "") {
                sb.append(language).append('_').append(country).append('_').append(variant);
            } else if (country != "") {
                sb.append(language).append('_').append(country);
            } else {
                sb.append(language);
            }
        }
        return sb.toString();

    }

    private String toResourceName0(String bundleName, String suffix) {
        // application protocol check
        if (bundleName.contains("://")) {
            return null;
        } else {
            return toResourceName(bundleName, suffix);
        }
    }

    private static Locale buildLocale(String language, String script, String region, String variant) {
        Locale.Builder builder = new Locale.Builder();
        builder.setLanguage(language);
        builder.setScript(script);
        builder.setRegion(region);
        builder.setVariant(variant);
       return builder.build();
    }


    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("configure", Locale.CANADA, new QvpControl());

        String v = bundle.getString("c");
        System.out.println(v);

        bundle.getKeys();
    }
}
