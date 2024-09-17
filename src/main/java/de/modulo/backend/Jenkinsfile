pipeline {
    agent any

    environment {
        // Define any environment variables if needed
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your code from your SCM, e.g., Git
                git 'git@github.com:Chreuseo/modulo-backend.git'
            }
        }

        stage('Build') {
            steps {
                // Build the Spring Boot application using Maven or Gradle
                // Assuming Maven is used for this example
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Path to the built jar file
                    def jarFilePath = "target/your-spring-boot-app.jar"

                    // Destination path
                    def destinationPath = "/opt/modulo/backend/modulo-master.jar"

                    // Move the built JAR to the destination path
                    sh """#!/bin/bash
                    sudo mv ${jarFilePath} ${destinationPath}
                    """

                    // Restart the modulo.service
                    sh """#!/bin/bash
                    sudo systemctl restart modulo.service
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Build, deployment, and service restart were successful.'
        }
        failure {
            echo 'There was an error during the pipeline execution.'
        }
    }
}
