from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Dict, Any
import re

app = FastAPI(title="RCA ML Service", version="0.2.0")

class RCAInput(BaseModel):
    service: str
    env: str
    logs: List[str]

class RCAResult(BaseModel):
    root_cause: str
    confidence: float
    suggestions: List[str]
    signals: Dict[str, Any]

ERROR_PATTERNS = [
    (re.compile(r"NullPointerException", re.I), 
     "Null reference in code path", 
     ["Add null checks", "Harden DTO mapping", "Improve unit tests for nulls"]),
    (re.compile(r"(OutOfMemoryError|heap space)", re.I), 
     "Memory leak or insufficient heap", 
     ["Increase heap", "Profile allocations", "Bound caches"]),
    (re.compile(r"(connection (refused|timeout)|db.*timeout)", re.I),
     "Database connectivity timeout",
     ["Check DB network/firewall", "Tune pool", "Validate DB credentials"]),
    (re.compile(r"(http 5\d{2}|5\d{2} .*? from .*?service)", re.I),
     "Upstream service 5xx",
     ["Check upstream health", "Retries with backoff", "Circuit breaker"]),
]

def naive_detect(logs: List[str]):
    scores = {}
    signals = []
    for line in logs:
        for rx, label, sugg in ERROR_PATTERNS:
            if rx.search(line):
                scores[label] = scores.get(label, 0) + 1
                signals.append({"match": label, "line": line})
    if not scores:
        return "Unknown - needs human review", 0.40, ["Collect more logs", "Correlate with last deploy"], signals
    root, count = max(scores.items(), key=lambda kv: kv[1])
    total_hits = sum(scores.values())
    conf = min(0.5 + (count / max(3, total_hits)), 0.95)
    for rx, label, sugg in ERROR_PATTERNS:
        if label == root:
            return root, conf, sugg, signals
    return "Unknown - needs human review", 0.40, ["Collect more logs"], signals

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/rca", response_model=RCAResult)
def rca(payload: RCAInput):
    root, conf, suggestions, signals = naive_detect(payload.logs)
    return RCAResult(
        root_cause=root,
        confidence=round(conf, 2),
        suggestions=suggestions,
        signals={"service": payload.service, "env": payload.env, "hits": signals},
    )
