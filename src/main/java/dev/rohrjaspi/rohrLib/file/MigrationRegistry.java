package dev.rohrjaspi.rohrlib.file;

import java.util.HashMap;
import java.util.Map;

public final class MigrationRegistry {

    private final Map<Integer, Migration> steps = new HashMap<>();

    /** Register a migration from version -> version+1 */
    public MigrationRegistry step(int fromVersion, Migration migration) {
        steps.put(fromVersion, migration);
        return this;
    }

    public Migration getStep(int fromVersion) {
        return steps.get(fromVersion);
    }
}