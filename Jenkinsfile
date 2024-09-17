pipeline {
    agent any

    tools {
        gradle 'gradle-8.10' // This should match the name you configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from your repository
                checkout scm
            }
        }

        stage('Build') {
            environment {
                // Set GRADLE_HOME to the path Jenkins will use
                env.GRADLE_HOME = tool 'gradle-8.10'
                env.PATH = "${env.GRADLE_HOME}/bin:${env.PATH}"
            }
            steps {
                // Gradle build
                sh 'gradle clean build'

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
