package com.phuongdoan

class Slack {

    private final def script

    Slack(def script) {
        this.script = script
    }

    void post(String status) {
        String color = "danger"

        if (status == "SUCCESS") {
            color = "good"
        }

        String date = script.sh(returnStdout: true, script: 'TZ=":Asia/Ho_Chi_Minh" date "+%Y-%m-%d %T"').trim()
        String message = "*${script.env.JOB_NAME} | ${date}* \n ${script.env.BUILD_URL}"
        script.slackSend(channel: "#alert", color: color, message: message)
    }
}
