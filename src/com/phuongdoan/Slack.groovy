package com.phuongdoan

class Slack implements Serializable {

    private final def script

    static final String slackHook = "https://hooks.slack.com/services/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"

    Slack(def script) {
        this.script = script
    }

    void post(String status) {
        if (status == "SUCCESS") {
            script.sh("echo ${status}")
            """
            )
        } else {
            script.sh("""
                script.sh("echo ${status}")
            """
            )
        }
    }

    // void buildDockerImage(String microserviceName) {
    //     def git = new Git(this.script)
    //     script.sh("docker build -t ${dockerRegistryIdentifier}/${microserviceName}:${git.commitHash()} .")
    // }

}
