pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "tusharyadaav/social-app-backend"

        // DockerHub Credentials
        DOCKER_CREDENTIALS = credentials('dockerhub-creds')

        JWT_SECRET = credentials('jwt-secret')

        // Cloudinary Secrets (Ensure these IDs match your Jenkins Credentials store)
        CLOUDINARY_CLOUD_NAME = credentials('cloudinary-cloud-name')
        CLOUDINARY_API_KEY    = credentials('cloudinary-api-key')
        CLOUDINARY_API_SECRET = credentials('cloudinary-api-secret')

        // Database Secrets
        SPRING_DATASOURCE_URL      = credentials('db-url')
        SPRING_DATASOURCE_USERNAME = credentials('db-username')
        SPRING_DATASOURCE_PASSWORD = credentials('db-password')
    }

    stages {
        stage('Pre-Cleanup') {
            steps {
                // Reclaim space before starting the build to prevent "Disk Full" errors
                sh 'docker system prune -f || true'
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/tusharyadav235/social-app-backend.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Login to DockerHub') {
            steps {
                sh "echo ${DOCKER_CREDENTIALS_PSW} | docker login -u ${DOCKER_CREDENTIALS_USR} --password-stdin"
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                sh "docker push ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ubuntu@3.91.226.211 '
                        docker pull ${DOCKER_IMAGE}:latest &&

                        docker stop social-app || true &&
                        docker rm social-app || true &&

                        docker run -d \
                          --name social-app \
                          -p 8081:8080 \
                          -e SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL}" \
                          -e SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME}" \
                          -e SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD}" \
                          -e CLOUDINARY_CLOUD_NAME="${CLOUDINARY_CLOUD_NAME}" \
                          -e CLOUDINARY_API_KEY="${CLOUDINARY_API_KEY}" \
                          -e CLOUDINARY_API_SECRET="${CLOUDINARY_API_SECRET}" \
                          -e JWT_SECRET="${JWT_SECRET}" \
                          ${DOCKER_IMAGE}:latest
                    '
                    """
                }
            }
        }
    }



    post {
        success {
            echo "✅ Deployment Successful! App is running on port 8081."
        }
        failure {
            echo "❌ Pipeline Failed!"
        }
        always {
            // Optional: Final cleanup to keep the disk clear
            sh 'docker image prune -f || true'
        }
    }
}