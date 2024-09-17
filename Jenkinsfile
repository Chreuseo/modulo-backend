pipeline {
    agent any

    environment {
        GRADLE_HOME = '/' // Set this to your Gradle installation directory if not on PATH
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from your repository
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Gradle build
                sh '${GRADLE_HOME}/bin/gradle clean build'
            }
        }

        stage('Move Jar') {
            steps {
                script {
                    // Find the generated JAR file
                    def jarFile = findFiles(glob: '**/build/libs/*.jar')[0]

                    // Move the JAR file to the target location
                    sh """
                    sudo mv ${jarFile.path} /opt/modulo/backend/modulo-master.jar
                    """
                }
            }
        }

        stage('Restart Service') {
            steps {
                // Restart the modulo.service
                sh 'sudo systemctl restart modulo.service'
            }
        }
    }

    post {
        success {
            echo 'Build, move, and restart were successful!'
        }
        failure {
            echo 'Build, move, or restart failed.'
        }
    }
}
