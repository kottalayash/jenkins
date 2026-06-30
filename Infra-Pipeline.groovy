pipeline {
    agent any
    stages {
        stage ('PULL') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/kottalayash/Terraform.git'
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
