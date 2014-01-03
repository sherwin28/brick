package net.isger.brick.core.config;

import java.io.InputStream;
import java.net.URL;

public interface ConfigManager {

    public boolean isReload(URL url);

    public InputStream load();

}
