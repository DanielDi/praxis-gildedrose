node {
    stage('Get a changes'){
        git url:'https://github.com/DanielDi/praxis-gildedrose', branch:'main'
    }
    stage('Build Back Image') {
        sh 'docker build -t danieldi/backend .'
    }
    stage('Test Back') {
        echo 'Testing..'
    }
    stage('Docker login') {
        sh 'docker login -u danieldi -p Praxis20221*team7'
    }
    stage('Push image') {
        sh 'docker push danieldi/backend'
    }
    
}