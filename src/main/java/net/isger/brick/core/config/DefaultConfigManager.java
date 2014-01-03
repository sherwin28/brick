package net.isger.brick.core.config;

import java.io.InputStream;
import java.net.URL;

public class DefaultConfigManager implements ConfigManager {

    public boolean isReload(URL url) {
        return false;
    }

    public InputStream load() {
        return null;
    }

}
