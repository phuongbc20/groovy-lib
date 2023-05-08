package com.phuongdoan

class Docker implements Serializable {

    private final def script

    static final String dockerUser = "phuongbc20"
    static final String dockerRegistryIdentifier = "phuongbc20"
    static final String dockerRegistryUrl = "https://${dockerRegistryIdentifier}"

    Docker(def script) {
        this.script = script
    }

    void loginToDockerHub(String dockerHubPassword) {
        script.sh("echo ${dockerHubPassword} | docker login -u ${dockerUser} --password-stdin")
    }

    void buildDockerImage(String microserviceName) {
        def git = new Git(this.script)
        script.sh("docker build -t ${dockerRegistryIdentifier}/${microserviceName}:${git.commitHash()} .")
    }

    void publishDockerImageToECR(String microserviceName) {
        def git = new Git(this.script)
        script.sh("docker push ${dockerRegistryIdentifier}/${microserviceName}:${git.commitHash()}")
    }
}
