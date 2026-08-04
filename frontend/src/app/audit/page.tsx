'use client';

import { useEffect, useState } from 'react';

interface AuditLog {
  id: number;
  eventType: string;
  createdAt: string;
  eventDetails: string;
}

function eventBadge(eventType: string) {
  const styles: { [key: string]: string } = {
    created: 'bg-emerald-50 text-emerald-700 border-emerald-200',
    status_changed: 'bg-blue-50 text-blue-700 border-blue-200',
  };
  const style = styles[eventType] || 'bg-gray-50 text-gray-700 border-gray-200';
  const label = eventType.replace('_', ' ');
  return <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium border ${style}`}>{label}</span>;
}

function formatTimestamp(iso: string) {
  const d = new Date(iso);
  const date = d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  const time = d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false });
  return (
    <>
      <div className="text-sm text-gray-900">{date}</div>
      <div className="text-xs text-gray-400">{time}</div>
    </>
  );
}

export default function AuditTrailPage() {
  const [auditLogs, setAuditLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchAuditLogs() {
      try {
        const response = await fetch('/api/audit-logs');
        if (response.ok) {
          const data = await response.json();
          setAuditLogs(data);
        } else {
          setError('An error occurred while fetching the audit trail.');
        }
      } catch (error) {
        console.error('Failed to fetch audit logs:', error);
        setError('An error occurred while fetching the audit trail.');
      } finally {
        setLoading(false);
      }
    }
    fetchAuditLogs();
  }, []);

  return (
    <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div className="mb-6">
        <h1 className="text-xl font-semibold text-gray-900">Audit Trail</h1>
        <p className="text-sm text-gray-500 mt-1">Order lifecycle events — most recent first (showing up to 50 entries)</p>
      </div>

      <div className="bg-white rounded-lg border border-surface-200 overflow-hidden shadow-sm">
        <table className="min-w-full divide-y divide-surface-200">
          <thead className="bg-surface-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Timestamp</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Event</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Details</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-surface-100">
            {loading ? (
              <tr>
                <td colSpan={3} className="p-4 text-center text-gray-500">Loading...</td>
              </tr>
            ) : error ? (\n              <tr>\n                <td colSpan={3} className=\"p-4 text-center text-red-500\">{error}</td>\n              </tr>\n            ) : auditLogs.length > 0 ? (
              auditLogs.map((entry) => (
                <tr key={entry.id} className="hover:bg-surface-50 transition-colors">
                  <td className="px-4 py-3 whitespace-nowrap">{formatTimestamp(entry.createdAt)}</td>
                  <td className="px-4 py-3 whitespace-nowrap">{eventBadge(entry.eventType)}</td>
                  <td className="px-4 py-3 text-sm text-gray-600">{entry.eventDetails}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={3} className="p-4 text-center text-gray-500">No audit trail entries found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
        { !loading &&
            <div className="mt-3 text-xs text-gray-400 text-right">
                Showing {auditLogs.length} of {auditLogs.length} entries
            </div>
        }
    </main>
  );
}
