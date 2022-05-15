node {
    stage('Get a changes'){
        git url:'https://github.com/DanielDi/praxis-gildedrose', branch:'main'

    }
    stage('Build Back Image') {
        steps {
            sh 'docker build -t danieldi/backend .'
        }
    }
    stage('Test Back') {
        steps {
            echo 'Testing..'
        }
    }
    stage('Docker login') {
        steps {
            sh 'docker login -u danieldi -p Praxis20221*team7'
        }
    }
    stage('Push image') {
        steps {
            sh 'docker push danieldi/backend'
        }
    }
}