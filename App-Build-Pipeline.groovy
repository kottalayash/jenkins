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
            sh '''cd frontend
            docker build -t kottalayash/frontend:latest .'''
        }
    }   
        stage ('DOCKER-BACKEND') {
        steps {
            sh '''cd backend
            docker build -t kottalayash/backend:latest .'''            
        }
    } 
         stage('DOCKER-LOGIN') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
        }
    }
}
        stage ('DOCKER-PUSH') {
            steps {
               sh '''docker push kottalayash/frontend:latest 
                    docker push kottalayash/backend:latest '''
            }
        }
        stage ('RM-IMAGE') {
            steps {
                sh 'docker rmi -f $(docker images -aq)'
            }
            
        }
            
        stage('EKS-ACCESS') {
    steps {
        sh '''
        aws eks update-kubeconfig --region ap-south-1 --name my-cluster
        '''
    }
}
        stage ('KUBECTL-APPLY') {
            steps{
                sh 'kubectl apply -f simple-deploy/'
            }
        } 
  }
}
