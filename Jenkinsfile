pipeline {
    agent any

    stages {
        stage('Removing images and network'){
            steps {
            sh 'docker rm --force my-postgres'
            sh 'docker network rm my-net'
            }
        }
        stage('Build Sub-Net backend') {
            steps {
                sh ' docker network create --subnet=122.22.0.0/16 my-net '
            }
        }
        stage('Build Database in subnet') {
            steps {
                sh ' docker run --name my-postgres --network="my-net" --ip 122.22.0.2 -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres '
            }
        }
        stage('Build Back') {
            steps {
                echo 'Building..'
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