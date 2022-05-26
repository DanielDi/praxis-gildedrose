node {
    stage('Get the changes from github'){
        git url:'https://github.com/DanielDi/praxis-gildedrose', branch:'main'
    }

    stage('Build backend') {

        // remove postgres image and backend image in case it exists
        sh(returnStdout: true, script: '''#!/bin/bash
            if [[ "$(docker images -q postgres)" != "" ]]; then
                docker stop my-postgres
                docker rm my-postgres
                docker rmi postgres
            fi

            if [[ "$(docker images -q danieldi/backend)" != "" ]]; then
                docker stop back-end
                docker rm back-end
                docker rmi danieldi/backend
            fi
            '''.stripIndent()
        )

        sh 'docker build -t danieldi/backend .'
    }

    stage('Test backend') {
        sh 'docker run --name back-end -e DB_HOST=group7-rds.cqqmj66dxtlw.us-east-1.rds.amazonaws.com -p 8090:8080 danieldi/backend mvn test'
        echo 'UNIT TESTS SUCCESSFUL'
        // remove container
        sh 'docker stop back-end'
        sh 'docker rm back-end'
    }

    stage('Docker login') {
        sh 'docker login -u danieldi -p Praxis20221*team7'
    }

    stage('Push image to docker-hub') {
        sh 'docker push danieldi/backend'
    }
}