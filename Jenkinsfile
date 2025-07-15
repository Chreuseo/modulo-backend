pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        // Define the Gradle tool to use
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
                JAVA_HOME = tool 'jdk-17'
                GRADLE_HOME = tool 'gradle-8.10'
                PATH = "${env.GRADLE_HOME}/bin:${env.PATH}"
            }
            steps {
                // Gradle build
                sh 'gradle clean bootJar'

            }
        }


        stage('Test') {
            steps {
                // Run tests and generate coverage report
                sh 'gradle test jacocoTestReport'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                JAVA_HOME = tool 'jdk-17'
                GRADLE_HOME = tool 'gradle-8.10'
                PATH = "${env.GRADLE_HOME}/bin:${env.PATH}"
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'gradle sonar'
                }
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
