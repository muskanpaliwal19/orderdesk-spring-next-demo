'use client';

import React, { useState, useEffect, useCallback } from 'react';
import OrderSlidePanel from '../components/orders/OrderSlidePanel';
import { Order } from '@/types/order';
import OrderCard from '../components/OrderCard';
import EmptyState from '../components/EmptyState';

const OrdersPage: React.FC = () => {
  const [isPanelOpen, setPanelOpen] = useState(false);
  const [orders, setOrders] = useState<Order[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [statusFilter, setStatusFilter] = useState('');

  const fetchOrders = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    const url = statusFilter ? `/api/orders?status=${statusFilter}` : '/api/orders';
    try {
      const res = await fetch(url);
      if (!res.ok) {
        throw new Error('Failed to fetch orders');
      }
      setOrders(await res.json());
    } catch (err) {
        if (err instanceof Error) {
            setError(err.message);
        } else {
            setError("An unknown error occurred.");
        }
    } finally {
      setIsLoading(false);
    }
  }, [statusFilter]);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

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
            <option value="new">New</option>
            <option value="paid">Paid</option>
            <option value="shipped">Shipped</option>
            <option value="cancelled">Cancelled</option>
          </select>
          <button 
                onClick={() => setPanelOpen(true)}
                className="rounded-xl border border-transparent py-2.5 px-4 bg-brand text-white font-semibold text-sm cursor-pointer flex items-center gap-1.5 hover:bg-brand/90 transition-colors"
            >
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4">
                    <path d="M8 4a.75.75 0 0 1 .75.75v2.5h2.5a.75.75 0 0 1 0 1.5h-2.5v2.5a.75.75 0 0 1-1.5 0v-2.5h-2.5a.75.75 0 0 1 0-1.5h2.5v-2.5A.75.75 0 0 1 8 4Z" />
                </svg>
                New Order
            </button>
        </div>
      </div>

      {isLoading ? (
        <div className=\"text-center py-16\"><p>Loading orders...</p></div>
      ) : error ? (
        <div className="text-red-500 text-center py-16">{error}</div>
      ) : orders.length > 0 ? (
        <div className="grid gap-3">
          {orders.map((order) => (
            <OrderCard key={order.id} order={order} />
          ))}
        </div>
      ) : (
        <EmptyState />
      )}

      <OrderSlidePanel 
        isOpen={isPanelOpen} 
        onClose={() => setPanelOpen(false)} 
        onOrderCreated={fetchOrders}
      />
    </main>
  );
};

export default OrdersPage;
