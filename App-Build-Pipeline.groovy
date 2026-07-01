pipeline {
    agent any 
    stages {
        stage ('PULL'){
        steps {
            git branch: 'test', url: 'https://github.com/kottalayash/CDEC-studentapp.git'
        }
    } 
        stage ('DOCKER-FRONTED') {
        steps {
            sh '''cd fronted
            docker build -t kottalayash/frontend:latest .'''
        }
    }   
        stage ('DOCKER-BACKEND') {
        steps {
            sh '''cd backend
            docker build -t kottalayash/backend:latest .'''            
        }
    } 
        stage ('DOCKER-LOGIN') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-hub-cred',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD' )
                ])
            }
        } 
        stage ('DOCKER-PUSH') {
            steps {
               sh '''docker push kottalayash/frontend:latest 
                    docker push kottalayash/backend:latest '''
            }
        }
        stage ('EKS-ACCESS'){
            steps {
                sh ' aws eks update-kubeconfig \\ --region ap-south-1\\--name my-cluster'
            }
        }
        stage ('KUBECTL-APPLY') {
            steps{
                sh 'kubectl apply -f simple-deploy/'
            }
        } 
  }
}
