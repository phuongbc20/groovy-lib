package com.phuongdoan

class Slack implements Serializable {

    private final def script

    static final String slackHook = "https://hooks.slack.com/services/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"

    Slack(def script) {
        this.script = script
    }

    void post(String status) {
        // if (status == "SUCCESS") {
            
            // String message = "{\"attachments\": [ { \"text\": \"URL\", \"color\": #4BB543, \"title\": \"title\" } ] }"

        // slackSend channel: "alert", color: 'good', message: "I've updated the infrastructure on platform"
        String date = script.sh(returnStdout: true, script: 'TZ=":Asia/Ho_Chi_Minh" date "+%Y-%m-%d %T"').trim()
        message = "${script.env.JOB_NAME} | ${date} /n ${script.env.BUILD_URL}"
        script.slackSend(channel: "#alert", color: "good", message: message)
            // slackSend(color: "good", message: "Message from Jenkins Pipeline", channel: "#alert")            
        // } else {
        //     script.sh("""
        //     echo ${status}
        //     """)
            // script.sh("""
            //     echo ${status}
            //     export DATE=`TZ=":Asia/Ho_Chi_Minh" date "+%Y-%m-%d %T"`
            //     export JSON=`jq --null-input \
            //     --arg text "${script.env.BUILD_URL}" \
            //     --arg color "#FF0000" \
            //     --arg title "${script.env.JOB_NAME} |}" \
            //     '{"attachments": [ { "text": $text, "color": $color, "title": $title } ] }'`
            //     curl -H "Content-Type:application/json" -X POST --data "${JSON}" ${slackHook}
            // """
            // )
        // }
    }

    // void buildDockerImage(String microserviceName) {
    //     def git = new Git(this.script)
    //     script.sh("docker build -t ${dockerRegistryIdentifier}/${microserviceName}:${git.commitHash()} .")
    // }

}
