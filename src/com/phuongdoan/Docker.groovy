package com.phuongdoan

class Docker {

    private final def script

    static final String dockerUser = "phuongbc20"
    static final String dockerRegistryIdentifier = "phuongbc20"

    Docker(def script) {
        this.script = script
    }

    void loginToDockerHub(String dockerHubPassword) {
        script.sh("echo ${dockerHubPassword} | docker login -u ${dockerUser} --password-stdin")
    }

    void buildDockerImage(Map config = [:]) {
        for (String key : config.keySet()) {
            script.sh("export ${key}=${config[key]}")
        }
        // def git = new Git(this.script)
        // script.sh("docker build -t ${dockerRegistryIdentifier}/${imageName}:${git.commitHash()} -t ${dockerRegistryIdentifier}/${imageName}:latest .")
    }

    void publishDockerImage(String imageName) {
        def git = new Git(this.script)
        script.sh("docker push ${dockerRegistryIdentifier}/${imageName}:${git.commitHash()}")
        script.sh("docker push ${dockerRegistryIdentifier}/${imageName}:latest")
    }

    void runDockerImage(String imageName, String command) {
        def git = new Git(this.script)
        script.sh("docker run --tty --rm -v \$(pwd):/build ${dockerRegistryIdentifier}/${imageName}:latest ${command}")
    }
}
