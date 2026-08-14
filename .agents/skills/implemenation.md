🛠️ DevOps Architecture Overview+-----------------------------------------------------------------------------------+
|                                  CI/CD PIPELINE                                   |
|                                                                                   |
|   Developer ---> GitHub Commit ---> Jenkins / GitHub Actions                       |
|                                         │                                         |
|                                         ├── 1. Maven Build & Unit Tests (JUnit)   |
|                                         ├── 2. React npm build                    |
|                                         ├── 3. Build Docker Images                |
|                                         └── 4. Push to Docker Hub / AWS ECR       |
+-----------------------------------------------------------------------------------+
│
v
+-----------------------------------------------------------------------------------+
|                               DEPLOYMENT TARGETS                                  |
|                                                                                   |
|   AWS EC2 / Elastic Beanstalk OR Kubernetes / OpenShift Cluster                   |
|   ┌──────────────────────────────────────────────────────────────────────────┐   |
|   │ Pod 1: React Container (Nginx)                                           │   |
|   │ Pod 2: Spring Boot API Container                                         │   |
|   │ Pod 3 / Managed Service: MySQL Container / AWS RDS MySQL                 │   |
|   └──────────────────────────────────────────────────────────────────────────┘   |
+-----------------------------------------------------------------------------------+


**Step-by-Step Implementation Guide**
1. Dockerization
   Containerize both frontend and backend so they run identically everywhere.

Backend (Dockerfile):

Use a multi-stage Dockerfile to build the Spring Boot .jar file using Maven and run it on OpenJDK.

Frontend (Dockerfile):

Build the React static bundle (npm run build) and serve it using an Nginx container.

Local Multi-Container Environment (docker-compose.yml):

Combine frontend, backend, and mysql containers using docker-compose up so your entire bank app runs locally with a single command.

2. CI/CD Pipeline with Jenkins (or GitHub Actions)
   Automate builds and testing whenever you push new code to GitHub.

Create a Jenkinsfile in your repository with the following pipeline stages:

Checkout: Pull code from your GitHub repository.

Test & Build Backend: Run mvn clean test package.

Build Frontend: Run npm install and npm run build.

Build Docker Images: Tag backend and frontend images with the build number or git commit hash.

Publish: Push container images to Docker Hub or AWS ECR (Elastic Container Registry).

Deploy: Trigger a deployment to Kubernetes, OpenShift, or AWS.

3. Orchestration with Kubernetes / OpenShift
   When moving to production, container orchestration provides auto-scaling, self-healing, and load balancing.

Write Kubernetes Manifests (.yaml):

Deployment.yaml: Configures replicas for your Spring Boot backend and React frontend.

Service.yaml: Exposes the backend API internally and frontend publicly (ClusterIP / LoadBalancer).

Secret.yaml & ConfigMap.yaml: Stores database passwords, JWT secrets, and DB connection strings securely.

Ingress.yaml (or OpenShift Route): Routes domain traffic (e.g., [https://mybank.com/api](https://mybank.com/api) $\rightarrow$ Spring Boot, [https://mybank.com](https://mybank.com) $\rightarrow$ React).

OpenShift Advantage: OpenShift builds directly on top of Kubernetes and provides built-in Source-to-Image (S2I) capabilities, integrated security policies, and an intuitive UI dashboard.

4. Cloud Deployment (AWS)
   You can host your infrastructure on AWS using several approaches depending on complexity:

Beginner Option (AWS Elastic Beanstalk / EC2): Deploy Docker containers directly to AWS EC2 or Elastic Beanstalk connected to AWS RDS (MySQL).

Enterprise Kubernetes Option (AWS EKS): Deploy your Kubernetes deployment manifests onto AWS Elastic Kubernetes Service (EKS).

**Recommended Learning Order**
If you want to implement this step-by-step, follow this sequence:

Write Dockerfiles for Spring Boot and React.

Create docker-compose.yml to spin up MySQL, Spring Boot, and React together locally.

Set up a local Jenkins container (or GitHub Actions workflow) to automate Maven test builds and Docker image pushes.

Install Minikube (local Kubernetes) or Red Hat OpenShift Local (CRC) to test deployment manifests locally.

Deploy to Cloud (AWS EKS, OpenShift Sandbox, or AWS EC2).