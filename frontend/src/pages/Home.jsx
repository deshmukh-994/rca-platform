import React, { useState } from 'react'
const API = import.meta.env.VITE_RCA_API || 'http://localhost:8081'

export default function Home() {
  const [logs, setLogs] = useState(`ERROR NullPointerException at UserService.java:45
WARN DB connection timeout after deploy v1.2`)
  const [resp, setResp] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const runRca = async () => {
    setLoading(true); setError(null); setResp(null)
    try {
      const r = await fetch(`${API}/api/rca`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          service: 'user-service',
          env: 'prod',
          logs: logs.split('\n').map(l => l.trim()).filter(Boolean)
        })
      })
      if (!r.ok) throw new Error(`HTTP ${r.status} - ${r.statusText}`)
      setResp(await r.json())
    } catch (e) {
      setError(e.message || String(e))
    } finally { setLoading(false) }
  }

  return (
    <div>
      <p>Paste logs and run RCA. Using API: <b>{API}</b></p>
      <textarea style={{ width:'100%', height:160 }} value={logs} onChange={e=>setLogs(e.target.value)} />
      <br />
      <button onClick={runRca} disabled={loading}>{loading ? 'Running...' : 'Run RCA'}</button>

      {error && <pre style={{ background:'#300', color:'#f55', padding:12, marginTop:12 }}>Error: {error}</pre>}
      {resp
        ? <pre style={{ background:'#111', color:'#0f0', padding:12, marginTop:12 }}>{JSON.stringify(resp, null, 2)}</pre>
        : !loading && !error && <pre style={{ background:'#111', color:'#0f0', padding:12, marginTop:12 }}>Result will appear here</pre>}
    </div>
  )
}
