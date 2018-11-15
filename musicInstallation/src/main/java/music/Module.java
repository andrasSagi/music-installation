package music;

import net.beadsproject.beads.core.UGen;

public interface Module {

    void setInputModule(Module module);

    UGen getOutput();

    void kill();
}
