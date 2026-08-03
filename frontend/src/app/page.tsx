'use client';

import React, { useState } from 'react';
import OrderSlidePanel from '../components/orders/OrderSlidePanel';
import AlertBanner from '../components/ui/AlertBanner';
import StatusBadge from '../components/ui/StatusBadge';

// Mock data for orders, will be replaced by API call
const orders = [
    { id: 3, customer: 'Maya Patel', email: 'maya@example.com', date: 'Feb 3, 2026', status: 'SHIPPED', total: 859.92, notes: 'Enterprise onboarding kit' },
    { id: 2, customer: 'Noah Singh', email: 'noah@example.com', date: 'Feb 2, 2026', status: 'NEW', total: 45.99, notes: 'Needs address confirmation' },
    { id: 1, customer: 'Ava Chen', email: 'ava@example.com', date: 'Feb 1, 2026', status: 'PAID', total: 88.95, notes: 'Priority customer' },
];

const OrdersPage: React.FC = () => {
  const [isPanelOpen, setPanelOpen] = useState(false);
  const [showSuccessBanner, setShowSuccessBanner] = useState(false);

  const handleOrderCreated = () => {
    setShowSuccessBanner(true);
    setTimeout(() => {
      setShowSuccessBanner(false);
    }, 5000);
  };

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
                <strong className="text-4xl text-green-800">$949.89</strong>
            </div>
        </div>

        {showSuccessBanner && (
            <AlertBanner
                variant="success"
                title="Order created successfully"
            />
        )}

        <div className="flex items-center justify-between gap-4 mb-4">
            <select className="rounded-2xl border border-gray-300 py-2.5 px-3.5 bg-white text-sm min-w-[160px]">
                <option value="">All statuses</option>
                <option value="new">New</option>
                <option value="paid">Paid</option>
                <option value="shipped">Shipped</option>
                <option value="cancelled">Cancelled</option>
            </select>
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
            {orders.map((order) => (
                 <div key={order.id} className="p-4 border border-gray-200 rounded-2xl bg-white/50">
                    <div className="flex items-center justify-between mb-1.5">
                        <strong className="flex items-center gap-2">#{order.id} {order.customer} <StatusBadge status={order.status} /></strong>
                        <span className="text-green-800 font-bold">${order.total.toFixed(2)}</span>
                    </div>
                    <div className="text-gray-500 text-sm">
                        {order.email} · {order.date} · {order.notes}
                    </div>
                 </div>
            ))}
        </div>

      <OrderSlidePanel 
        isOpen={isPanelOpen} 
        onClose={() => setPanelOpen(false)} 
        onOrderCreated={handleOrderCreated}
      />
    </div>
  );
};

export default OrdersPage;
