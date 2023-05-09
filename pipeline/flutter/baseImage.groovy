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
                        Docker.buildDockerImage(env.IMAGE_NAME)
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
