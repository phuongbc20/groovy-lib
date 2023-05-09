package com.phuongdoan

class Workspace {

    private final def script

    Workspace(def script) {
        this.script = script
    }

    void clear() {
        script.cleanWs(cleanWhenSuccess: true, cleanWhenFailure: true, cleanWhenAborted: true, cleanWhenNotBuilt: true, cleanWhenUnstable: true, cleanupMatrixParent: true, deleteDirs: true, disableDeferredWipeout: true)
    }
}
