package com.phuongdoan

class Git {

    private final def script

    Git(def script) {
        this.script = script
    }

    def checkout(String repo) {
        this.script.git credentialsId: Constants.JENKINS_GITHUB_CREDENTIALS_ID, url: "http://gerrit-phuong.stg.cdgfossil.com/a/${repo}", branch: "main"
    }

    String commitHash() {
        return this.script.sh(script: getLatestGitCommitHashCommand(), returnStdout: true).trim()
    }

    private static String getLatestGitCommitHashCommand() {
        return "git rev-parse HEAD"
    }

    def gerritReview(String status) {
        Integer verified = -1
        if (status == "SUCCESS") {
            verified = 1
        }
        this.script.gerritReview labels: ['Presubmit-Verified': verified], message: "BUILD ${status} ${script.env.BUILD_URL}"
    }

}
