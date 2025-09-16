# RCA Platform (Full-Stack, ML-powered, Cloud-ready)

This is a production-style scaffold for an **AI-Powered Root Cause Analysis** platform.

## Services
- **log-service (Java)**: ingest logs, push to Kafka, optional persist.
- **rca-service (Java)**: orchestrates RCA pipeline; calls ML service; stores reports.
- **suggestion-service (Java)**: maps error classes to recommended fixes.
- **ml-service (Python FastAPI)**: anomaly detection + error classification (starter logic).
- **frontend (React/Vite)**: dashboard for RCA results (charts + live updates).

## Infra (via Docker Compose profiles)
- **Core (default)**: Postgres, Kafka, Zookeeper, Java services, ML, Frontend.
- **Optional**: MongoDB (`mongo`), Prometheus & Grafana (`metrics`), Elasticsearch & Kibana (`elastic`).

## Quick Start (Core)
```bash
docker compose build
docker compose up
# Frontend: http://localhost:5173
# RCA API (rca-service): http://localhost:8081/api/health
# ML service: http://localhost:8000/health
```
Submit sample RCA request:
```bash
curl -X POST http://localhost:8081/api/rca   -H "Content-Type: application/json"   -d '{
    "service":"user-service",
    "env":"prod",
    "logs":[
      "ERROR NullPointerException at UserService.java:45",
      "WARN DB connection timeout after deploy v1.2"
    ]
  }'
```

## Profiles
- `mongo`: `docker compose --profile mongo up -d`
- `metrics`: `docker compose --profile metrics up -d`
- `elastic`: `docker compose --profile elastic up -d`

## Kubernetes Manifests
See `k8s/`. Apply selectively:
```bash
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/kafka.yaml
kubectl apply -f k8s/ml-service.yaml<img width="941" height="832" alt="Screenshot 2025-09-16 at 6 02 44 PM" src="https://github.com/user-attachments/assets/e001da7d-f60a-4b23-b910-c9c42bd2dea5" />
<img width="929" height="826" alt="Screenshot 2025-09-16 at 6 02 22 PM" src="https://github.com/user-attachments/assets/16709703-55c3-4bd9-a41c-bb610747f41d" />

kubectl apply -f k8s/rca-service.yaml
kubectl apply -f k8s/log-service.yaml
kubectl apply -f k8s/suggestion-service.yaml
kubectl apply -f k8s/frontend.yaml
```

## CI/CD
- **GitHub Actions**: `.github/workflows/ci.yml` (build Java/Python/Frontend).
- **Jenkinsfile**: Docker build + test stages.

> NOTE: This is a scaffold to get you running quickly; extend with real models and business logic.
