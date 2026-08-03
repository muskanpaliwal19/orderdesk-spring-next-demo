'use client';

import React, { useState, useEffect, useCallback } from 'react';
import OrderSlidePanel from '../components/orders/OrderSlidePanel';
import StatusBadge from '../components/ui/StatusBadge';
import { Order } from 'types/order';

const OrdersPage: React.FC = () => {
  const [isPanelOpen, setPanelOpen] = useState(false);
  const [orders, setOrders] = useState<Order[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchOrders = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/orders');
      if (!res.ok) {
        throw new Error('Failed to fetch orders');
      }
      const data = await res.json();
      setOrders(data);
    } catch (err) {
        if(err instanceof Error) {
            setError(err.message);
        } else {
            setError("An unknown error occurred.");
        }
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  const totalRevenue = orders.reduce((sum, order) => sum + order.total, 0);

  return (
    <div className="w-[min(1120px,calc(100%-32px))] mx-auto my-12">
        <div className="grid grid-cols-[minmax(0,1fr),260px] gap-6 items-end mb-6">
            <div>
                <p className="text-sm font-extrabold uppercase tracking-[.14em] text-orange-600 mb-2.5">
                    Order Management
                </p>
                <h1 className="text-[clamp(44px,8vw,72px)] leading-[.9] tracking-[-.07em]">
                    Orders
                </h1>
            </div>
            <div className="bg-orange-50/80 border border-orange-200/60 rounded-3xl shadow-xl shadow-stone-200/50 p-6">
                <span className="block text-gray-600 text-sm mb-2.5">Total revenue</span>
                <strong className="text-4xl text-green-800">${totalRevenue.toFixed(2)}</strong>
            </div>
        </div>

        <div className="flex items-center justify-between gap-4 mb-4">
            <div className="flex items-center gap-2">
                <select className="rounded-2xl border border-gray-300 py-2.5 px-3.5 bg-white text-sm min-w-[160px]">
                    <option value="">All statuses</option>
                    <option value="new">New</option>
                    <option value="paid">Paid</option>
                    <option value="shipped">Shipped</option>
                    <option value="cancelled">Cancelled</option>
                </select>
                {/* Add a refresh button for manual refresh */}
                <button onClick={fetchOrders} className="p-2.5 rounded-xl border border-gray-300 bg-white text-gray-700" aria-label="Refresh orders">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                        <path fillRule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z"/>
                        <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z"/>
                    </svg>
                </button>
            </div>
            <button 
                onClick={() => setPanelOpen(true)}
                className="rounded-2xl border border-transparent py-2.5 px-5 bg-green-700 text-white font-extrabold text-sm cursor-pointer flex items-center gap-1.5"
            >
                <svg width="16" height="16" fill="none" viewBox="0 0 16 16">
                    <path d="M8 3v10M3 8h10" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                </svg>
                New Order
            </button>
        </div>

        <div className="grid gap-3">
            {isLoading && <p>Loading orders...</p>}
            {error && <div className="text-red-500">{error}</div>}
            {!isLoading && !error && orders.map((order) => (
                 <div key={order.id} className="p-4 border border-gray-200 rounded-2xl bg-white/50">
                    <div className="flex items-center justify-between mb-1.5">
                        <strong className="flex items-center gap-2">#{order.id} {order.customer.name} <StatusBadge status={order.status} /></strong>
                        <span className="text-green-800 font-bold">${order.total.toFixed(2)}</span>
                    </div>
                    <div className="text-gray-500 text-sm">
                        {order.customer.email}
                    </div>
                 </div>
            ))}
        </div>

      <OrderSlidePanel 
        isOpen={isPanelOpen} 
        onClose={() => setPanelOpen(false)} 
        onOrderCreated={fetchOrders}
      />
    </div>
  );
};

export default OrdersPage;
