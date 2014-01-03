package net.isger.brick.core;

import net.isger.brick.BrickException;
import net.isger.brick.raw.Artifact;
import net.isger.brick.raw.Depository;
import net.isger.brick.raw.Depot;

/**
 * 默认控制台
 * 
 * @author issing
 * 
 */
public class DefaultConsole extends AbstractConsole {

    protected Object load() {
        return load(name + ".json");
    }

    protected Object load(String res) {
        Artifact artifact = Depository.wrap("json", Depot.LAB_FILE, res);
        if (artifact == null) {
            throw new BrickException("Not found the config by " + res);
        }
        return artifact.use("transform");
    }

    public void operate() {
    }

}
