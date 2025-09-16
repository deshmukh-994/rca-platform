import React, { useState } from 'react'

const API = import.meta.env.VITE_RCA_API || 'http://localhost:8081'

const PRESETS = {
  'NullPointer + DB timeout': `ERROR NullPointerException at UserService.java:45
WARN DB connection timeout after deploy v1.2`,
  'OutOfMemory': `ERROR OutOfMemoryError: Java heap space
INFO GC overhead limit exceeded`,
  'Upstream 5xx': `ERROR HTTP 502 from payment-service
WARN retrying request after backoff`
}

export default function App() {
  const [service, setService] = useState('user-service')
  const [env, setEnv] = useState('prod')
  const [logs, setLogs] = useState(PRESETS['NullPointer + DB timeout'])
  const [resp, setResp] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [latencyMs, setLatencyMs] = useState(null)
  const [history, setHistory] = useState([])

  const runRca = async () => {
    const list = logs.split('\n').map(l => l.trim()).filter(Boolean)
    if (list.length === 0) {
      setError('Please paste at least one log line.')
      return
    }

    setLoading(true); setError(null); setResp(null); setLatencyMs(null)
    const t0 = performance.now()
    try {
      const r = await fetch(`${API}/api/rca`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ service, env, logs: list })
      })
      if (!r.ok) throw new Error(`HTTP ${r.status} - ${r.statusText}`)
      const data = await r.json()
      setResp(data)
    } catch (err) {
      console.error('RCA request failed:', err)
      setError(err.message || String(err))
    } finally {
      setLatencyMs(Math.round(performance.now() - t0))
      setLoading(false)
    }
  }

  const loadHistory = async () => {
    setError(null)
    try {
      const url = service ? `${API}/api/reports?service=${encodeURIComponent(service)}` : `${API}/api/reports`
      const r = await fetch(url)
      if (!r.ok) throw new Error(`HTTP ${r.status}`)
      setHistory(await r.json())
    } catch (e) {
      setError(e.message || String(e))
    }
  }

  const copyResult = async () => {
    if (!resp) return
    await navigator.clipboard.writeText(JSON.stringify(resp, null, 2))
  }

  const downloadResult = () => {
    if (!resp) return
    const blob = new Blob([JSON.stringify(resp, null, 2)], { type: 'application/json' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `rca-${Date.now()}.json`
    a.click()
    URL.revokeObjectURL(url)
  }

  return (
    <div style={{ fontFamily: 'Inter, system-ui, sans-serif', padding: 24, maxWidth: 900, margin: '0 auto' }}>
      <h1>RCA Dashboard</h1>
      <p style={{ marginTop: -8 }}>API: <b>{API}</b></p>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 12 }}>
        <label>Service
          <input value={service} onChange={e => setService(e.target.value)} style={{ width: '100%' }} />
        </label>
        <label>Environment
          <input value={env} onChange={e => setEnv(e.target.value)} style={{ width: '100%' }} />
        </label>
      </div>

      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 8 }}>
        {Object.keys(PRESETS).map(k => (
          <button key={k} onClick={() => setLogs(PRESETS[k])}>{k}</button>
        ))}
        <button onClick={() => setLogs('')}>Clear</button>
      </div>

      <textarea
        style={{ width: '100%', height: 180 }}
        value={logs}
        onChange={e => setLogs(e.target.value)}
        placeholder="Paste logs here…"
      />

      <div style={{ marginTop: 10, display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        <button onClick={runRca} disabled={loading}>{loading ? 'Running…' : 'Run RCA'}</button>
        <button onClick={loadHistory}>Load Recent Reports</button>
        <button onClick={copyResult} disabled={!resp}>Copy Result</button>
        <button onClick={downloadResult} disabled={!resp}>Download JSON</button>
        {latencyMs != null && <span style={{ opacity: 0.7 }}>Latency: {latencyMs} ms</span>}
      </div>

      {error && (
        <pre style={{ background: '#300', color: '#f55', padding: 12, marginTop: 12 }}>
          Error: {error}
        </pre>
      )}

      {resp && (
        <pre style={{ background: '#111', color: '#0f0', padding: 12, marginTop: 12 }}>
          {JSON.stringify(resp, null, 2)}
        </pre>
      )}

      {!resp && !error && !loading && (
        <pre style={{ background: '#111', color: '#0f0', padding: 12, marginTop: 12 }}>
          Result will appear here
        </pre>
      )}

      {history?.length > 0 && (
        <div style={{ marginTop: 16 }}>
          <h3>Recent Reports{service ? ` — ${service}` : ''}</h3>
          <div style={{ overflowX: 'auto' }}>
            <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
              <thead>
                <tr>
                  <th>ID</th><th>Service</th><th>Env</th><th>Root Cause</th><th>Confidence</th><th>Created</th>
                </tr>
              </thead>
              <tbody>
                {history.map(r => (
                  <tr key={r.id}>
                    <td>{r.id}</td>
                    <td>{r.service}</td>
                    <td>{r.env}</td>
                    <td>{r.rootCause}</td>
                    <td>{(r.confidence ?? 0).toFixed(2)}</td>
                    <td>{r.createdAt}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
