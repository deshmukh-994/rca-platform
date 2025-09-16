pipeline {
  agent any
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build Java') {
      steps {
        sh '''
        for svc in rca-service log-service suggestion-service; do
          (cd $svc && mvn -q -DskipTests package)
        done
        '''
      }
    }
    stage('Build Frontend') {
      steps {
        sh 'cd frontend && npm ci && npm run build'
      }
    }
    stage('Docker Build') {
      steps {
        sh '''
        docker build -t rca/ml-service:latest ml-service
        docker build -t rca/rca-service:latest rca-service
        docker build -t rca/log-service:latest log-service
        docker build -t rca/suggestion-service:latest suggestion-service
        docker build -t rca/frontend:latest frontend
        '''
      }
    }
  }
}
