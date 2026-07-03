pipeline {
    agent any
    stages {
        stage ('PULL') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/kottalayash/Terraform.git'
            }
        }
        stage ('AWS-Cred'){
            steps {
                withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'AWS-Cred', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
               }
            }
        }
            
        stage ('PLAN') {
            steps {
                sh '''terraform init 
                        terraform plan'''
            }
        }
        stage ('APPROVAL') {
            steps {
                input 'Shall we proceed, ok: Approve'
            }
        }
        stage ('APPLY') {
            steps {
                sh 'terraform apply --auto-approve'
            }
        }
    }
}
