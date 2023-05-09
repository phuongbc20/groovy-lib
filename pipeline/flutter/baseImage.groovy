@Library('mylibrary') _

import com.phuongdoan.Docker
import com.phuongdoan.Git
import com.phuongdoan.Constants
import com.phuongdoan.Slack

pipeline {
    agent {
        label 'build-image'
    }
    environment {
        DOCKER_HUB_PASSWORD = credentials('dockerhub')
        REPO_NAME = 'flutter-app'
        IMAGE_NAME = 'flutter-apk'
    }
    parameters {
        string(name: 'FLUTTER_VERSION', defaultValue: '3.7.12', description: 'Flutter version')
        string(name: 'COMMANDLINETOOLS_VERSION', defaultValue: '9477386', description: 'Commandlinetools version number')
        string(name: 'PLATFORMS_VERSION', defaultValue: '33', description: 'Android platforms version')
        string(name: 'BUILD_TOOLS_VERSION', defaultValue: '30.0.3', description: 'Android build tools version ')
        string(name: 'CMDLINE_TOOLS_VERSION', defaultValue: '9.0', description: 'Cmdline tools version')
    }
    stages {
        stage('Build') {
            steps {
                script {
                    def workspace = new Workspace(this)
                    def git = new Git(this)
                    def Docker = new Docker(this)
                    stage('Clear Workspace') {
                        workspace.clear()
                    }
                    stage('Checkout') {
                        git.checkout(env.REPO_NAME)
                    }
                    stage('Build') {
                        Docker.buildDockerImage(env.IMAGE_NAME, params.FLUTTER_VERSION, params.COMMANDLINETOOLS_VERSION, params.PLATFORMS_VERSION, params.BUILD_TOOLS_VERSION, params.CMDLINE_TOOLS_VERSION)
                    }
                    stage('Login to Docker Hub') {
                        Docker.loginToDockerHub(env.DOCKER_HUB_PASSWORD)
                    }
                    stage('Publish Docker Image') {
                        Docker.publishDockerImageToECR(env.IMAGE_NAME)
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
