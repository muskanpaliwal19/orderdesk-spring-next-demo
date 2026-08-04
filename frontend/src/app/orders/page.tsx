
'use client';

import { useState, useEffect } from 'react';

// This type is based on the OrderListItemDto from the backend
interface Order {
  id: number;
  customerName: string;
  customerEmail: string;
  status: string;
  totalCents: number;
  orderDate: string;
  notes: string | null;
}

const statusStyles: { [key: string]: { bg: string; text: string; ring: string } } = {
  new: { bg: 'bg-blue-50', text: 'text-blue-700', ring: 'ring-blue-200' },
  paid: { bg: 'bg-emerald-50', text: 'text-emerald-700', ring: 'ring-emerald-200' },
  shipped: { bg: 'bg-violet-50', text: 'text-violet-700', ring: 'ring-violet-200' },
  cancelled: { bg: 'bg-red-50', text: 'text-red-700', ring: 'ring-red-200' },
};

function StatusBadge({ status }: { status: string }) {
  const s = statusStyles[status.toLowerCase()] || statusStyles.new;
  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold uppercase tracking-wide ring-1 ${s.bg} ${s.text} ${s.ring}`}
    >
      {status}
    </span>
  );
}

function centsToDollars(cents: number) {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format((cents || 0) / 100);
}

function formatDate(dateString: string) {
  const date = new Date(dateString);
  return `${date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })} at ${date.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' })}`;
}


export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchOrders() {
      setLoading(true);
      setError(null);
      const url = statusFilter ? `/api/orders?status=${statusFilter.toUpperCase()}` : '/api/orders';
      try {
        const res = await fetch(url);
        if (!res.ok) {
          throw new Error('Failed to fetch orders');
        }
        const data = await res.json();
        setOrders(data);
      } catch (error) {
        console.error(error);
        setError('Failed to load orders. Please try again later.');
      } finally {
        setLoading(false);
      }
    }
    fetchOrders();
  }, [statusFilter]);

  const handleExport = () => {
    const url = statusFilter
      ? `/api/orders/export?status=${statusFilter.toUpperCase()}`
      : '/api/orders/export';
    window.location.href = url;
  };

  return (
    <main className="max-w-5xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight">Orders</h1>
          <p className="text-sm text-muted mt-0.5">Manage and track customer orders</p>
        </div>
        <div className="flex items-center gap-4">
          <select
            id="statusFilter"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="border border-line rounded-xl px-4 py-2.5 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-brand/30 cursor-pointer"
          >
            <option value="">All statuses</option>
            <option value="NEW">New</option>
            <option value="PAID">Paid</option>
            <option value="SHIPPED">Shipped</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
          <button
            onClick={handleExport}
            className="bg-brand text-white font-semibold px-4 py-2.5 rounded-xl text-sm hover:bg-brand/90 transition-colors focus:outline-none focus:ring-2 focus:ring-brand/50"
          >
            Export CSV
          </button>
        </div>
      </div>

      {loading ? (
         <div className="text-center py-16">
            <p className="text-muted text-sm">Loading orders...</p>
         </div>
      ) : error ? (
        <div className="text-center py-16">
          <div className="text-4xl mb-3">😞</div>
          <p className="font-semibold text-red-600">Failed to load orders</p>
          <p className="text-muted text-sm mt-2">{error}</p>
        </div>
      ) : orders.length === 0 ? (
        <div id="emptyState" className="text-center py-16">
          <div className="text-4xl mb-3">📦</div>
          <p className="text-muted text-sm">No orders match the selected filter.</p>
        </div>
      ) : (
        <div id="orderList" className="grid gap-3">
          {orders.map((o) => (
            <div key={o.id} className="bg-white/90 border border-line rounded-2xl p-4 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex items-start justify-between gap-3 mb-2">
                <div className="flex items-center gap-2.5">
                  <span className="font-bold text-base">#{o.id}</span>
                  <span className="font-semibold text-base">{o.customerName}</span>
                  <StatusBadge status={o.status} />
                </div>
                <span className="font-bold text-brand text-lg tabular-nums">{centsToDollars(o.totalCents)}</span>
              </div>
              <div className="flex items-center gap-2 text-xs text-muted">
                <span>{o.customerEmail}</span>
                <span className="text-line">·</span>
                <span>{formatDate(o.orderDate)}</span>
              </div>
              {o.notes && (
                <div className="mt-2 text-sm text-muted/80 bg-bg/60 rounded-lg px-3 py-1.5">
                  {o.notes}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </main>
  );
}
