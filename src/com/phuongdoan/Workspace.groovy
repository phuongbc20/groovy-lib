package com.phuongdoan

class Workspace {

    private final def script

    Workspace(def script) {
        this.script = script
    }

    void clear() {
        script.clearWs()
    }
}
