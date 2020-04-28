package com.mycompany.colinbut

class DockerEcr implements Serializable {

    private final def script

    static final String awsRegion = "eu-west-2"
    static final String dockerUser = "AWS"
    static final String dockerRegistryUrl = "https://066203203749.dkr.ecr.eu-west-2.amazonaws.com"

    DockerEcr(def script) {
        this.script = script
    }

    void loginToAWSECRDockerRegistry(int awsCliVersion) {
        if (awsCliVersion == 1) {
            this.script.sh(
                    """
                    aws ecr get-login --region ${awsRegion} --no-include-email \
                    | awk '{printf \$6}' \
                    | docker login -u ${dockerUser} ${dockerRegistryUrl} --password-stdin
                    """
            )
        } else if (awsCliVersion == 2) {
            this.script.sh(
                    """
                    aws ecr get-login-password --region ${awsRegion} \
                    | docker login -u ${dockerUser} ${dockerRegistryUrl} --password-stdin
                    """
            )
        }
    }

    void buildDockerImage(String microserviceName) {
        def git = new Git(this.script)
        script.sh("docker build -t ${microserviceName}:${git.commitHash()} .")
    }

    void publishDockerImageToECR(String microserviceName) {
        // get aws cli version installed on Jenkins Node
        // Jenkins Node is built from Amazon Linux 2 so would have aws-cli pre bundled
        String awsCliVersionString = script.sh(returnStdout: true, script: "aws --version")
        String awsCliVersion = awsCliVersionString.split(" ")[0]
        String awsCliVersionSemVer = awsCliVersion.split("/")[1]
        String awsCliMajorVersion = awsCliVersionSemVer.split(".")[0]
        loginToAWSECRDockerRegistry(awsCliMajorVersion.toInteger())

        def git = new Git(this.script)
        script.sh("docker push ${microserviceName}:${git.commitHash()}")
    }
}
