node {
    stage('Create Network') {
        sh(returnStdout: true, script: '''#!/bin/bash
            if [[ "$(docker network ls | grep my-net )" == "" ]] ; then
                docker network create --subnet=122.22.0.0/16 my-net
            fi
            '''.stripIndent()
        )
    }
    stage('Get a changes'){
        git url:'https://github.com/DanielDi/praxis-gildedrose', branch:'main'
    }
    stage('Build Back Image') {
        sh 'docker build -t danieldi/backend .'
    }
    stage('Test Back') {
        sh(returnStdout: true, script: '''#!/bin/bash
            if [[ "$(docker images -q postgres)" != "" ]]; then
                docker stop my-postgres
                docker rm my-postgres
                docker rmi postgres
            fi
            '''.stripIndent()
        )
        sh(returnStdout: true, script: '''#!/bin/bash
            if [[ "$(docker images -q danieldi/backend)" != "" ]]; then
                docker stop back-end
                docker rm back-end
                docker rmi danieldi/backend
            fi
            '''.stripIndent()
        )
        // sh 'docker run --name my-postgres --network="my-net" --ip 122.22.0.2 -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres'
        // sh 'docker run --name back-end --network="my-net" --ip 122.22.0.22 -p 8090:8080 -d danieldi/backend mvn test'
    }
    stage('Docker login') {
        sh 'docker login -u danieldi -p Praxis20221*team7'
    }

    stage('Push image') {
        sh 'docker push danieldi/backend'
    }

}