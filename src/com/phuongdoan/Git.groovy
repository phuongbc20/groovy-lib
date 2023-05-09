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

    def gerritReview() {
        this.gerritReview(labels: ['Presubmit-Verified': 1], message: "Build succeeded ${script.env.BUILD_URL}")
    }

}
