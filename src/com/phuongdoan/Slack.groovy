package com.phuongdoan

class Slack implements Serializable {

    private final def script

    static final String slackHook = "https://hooks.slack.com/services/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"
    static final String dockerRegistryIdentifier = "phuongbc20"

    Docker(def script) {
        this.script = script
    }

    void post(String status) {
        if (status == "SUCCESS") {
            script.sh("""
                echo ${status}
                export DATE=$(TZ=":Asia/Ho_Chi_Minh" date "+%Y-%m-%d %T")
                export JSON=$(jq --null-input \
                --arg text "${BUILD_URL}" \
                --arg color "#4BB543" \
                --arg title "${JOB_NAME} | ${DATE}" \
                '{"attachments": [ { "text": $text, "color": $color, "title": $title } ] }')
                curl -H "Content-Type:application/json" -X POST --data "${JSON}" ${slackHook}
            """
            )
        } else {
            script.sh("""
                echo ${status}
                export DATE=$(TZ=":Asia/Ho_Chi_Minh" date "+%Y-%m-%d %T")
                export JSON=$(jq --null-input \
                --arg text "${BUILD_URL}" \
                --arg color "#FF0000" \
                --arg title "${JOB_NAME} | ${DATE}" \
                '{"attachments": [ { "text": $text, "color": $color, "title": $title } ] }')
                curl -H "Content-Type:application/json" -X POST --data "${JSON}" ${slackHook}
            """
            )
        }
    }

    // void buildDockerImage(String microserviceName) {
    //     def git = new Git(this.script)
    //     script.sh("docker build -t ${dockerRegistryIdentifier}/${microserviceName}:${git.commitHash()} .")
    // }

}
