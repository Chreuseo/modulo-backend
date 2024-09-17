pipeline {
    agent any

    tools {
        gradle '8.10'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from your repository
                checkout scm
            }
        }

        stage('Ensure Gradle Wrapper') {
            steps {
                script {
                    // Ensure gradlew is executable
                    sh 'chmod +x ./gradlew'

                    // Confirm gradle-wrapper.jar exists
                    def wrapperJar = "gradle/wrapper/gradle-wrapper.jar"
                    if (!fileExists(wrapperJar)) {
                        error "Gradle wrapper JAR not found: ${wrapperJar}"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                // Use Gradle wrapper for build
                sh './gradlew clean build'
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
