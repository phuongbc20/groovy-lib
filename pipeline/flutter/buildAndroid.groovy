@Library('mylibrary') _

import com.phuongdoan.Docker
import com.phuongdoan.Git
import com.phuongdoan.Constants
import com.phuongdoan.Slack
import com.phuongdoan.Workspace

pipeline {
    agent {
        label 'build-image'
    }
    environment {
        REPO_NAME = 'flutter-app'
        IMAGE_NAME = 'flutter-apk'
        COMMAND_BUILD = 'flutter build apk'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    def workspace = new Workspace(this)
                    stage('Clear Workspace') {
                        workspace.clear()
                    }
                    def git = new Git(this)
                    stage('Checkout') {
                        git.checkout(env.REPO_NAME)
                    }
                    def Docker = new Docker(this)
                    stage('Build Apk') {
                        Docker.runDockerImage(env.IMAGE_NAME, env.COMMAND_BUILD)
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                def slack = new Slack(this)
                slack.post(currentBuild.currentResult.toString())
            }
        }
    }
}
