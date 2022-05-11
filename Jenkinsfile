pipeline {
    agent any

    stages {
        stage('Build Back Database') {
            steps {
                sh 'docker run --name my-postgres --network="my-net" --ip 122.22.0.2 -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres '
            }
        }
        stage('Test Back') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy Back') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}