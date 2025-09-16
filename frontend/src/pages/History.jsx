import React, { useEffect, useState } from 'react'
const API = import.meta.env.VITE_RCA_API || 'http://localhost:8081'

export default function History() {
  const [service, setService] = useState('')
  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const load = async () => {
    setLoading(true); setError(null)
    try {
      const url = service ? `${API}/api/reports?service=${encodeURIComponent(service)}` : `${API}/api/reports`
      const r = await fetch(url)
      if (!r.ok) throw new Error(`HTTP ${r.status}`)
      setRows(await r.json())
    } catch (e) { setError(e.message || String(e)) }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, []) // initial

  return (
    <div>
      <h2>Reports History</h2>
      <div style={{ marginBottom: 12 }}>
        <input
          value={service}
          onChange={e => setService(e.target.value)}
          placeholder="Filter by service (optional)"
          style={{ marginRight: 8 }}
        />
        <button onClick={load} disabled={loading}>{loading ? 'Loading…' : 'Refresh'}</button>
      </div>

      {error && <div style={{ color: '#c00', marginBottom: 8 }}>Error: {error}</div>}

      <div style={{ overflowX: 'auto' }}>
        <table border="1" cellPadding="6" style={{ borderCollapse: 'collapse', width: '100%' }}>
          <thead>
            <tr>
              <th>ID</th><th>Service</th><th>Env</th><th>Root Cause</th><th>Confidence</th><th>Created</th>
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 && !loading && (
              <tr><td colSpan="6" style={{ textAlign: 'center' }}>No reports yet</td></tr>
            )}
            {rows.map(r => (
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

      <p style={{ marginTop: 8 }}>API: <b>{API}/api/reports</b></p>
    </div>
  )
}
