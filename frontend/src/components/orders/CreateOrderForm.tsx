'use client';

import React, { useState } from 'react';
import LineItemRow from './LineItemRow';
import AlertBanner from '../ui/AlertBanner';

// This will be replaced with data from an API call
const customers = [
  { id: '1', name: 'Ava Chen', email: 'ava@example.com', type: 'premium' },
  { id: '2', name: 'Noah Singh', email: 'noah@example.com', type: 'standard' },
  { id: '3', name: 'Maya Patel', email: 'maya@example.com', type: 'enterprise' },
];

interface CreateOrderFormProps {
  onCancel: () => void;
  onCreate: () => void;
}

const CreateOrderForm: React.FC<CreateOrderFormProps> = ({ onCancel, onCreate }) => {
  const [lineItems, setLineItems] = useState([{}]);
  const [error, setError] = useState<string | null>(null);

  const addLineItem = () => {
    setLineItems([...lineItems, {}]);
  };

  const removeLineItem = (index: number) => {
    if (lineItems.length > 1) {
      const newLineItems = [...lineItems];
      newLineItems.splice(index, 1);
      setLineItems(newLineItems);
    }
  };

  const handleSubmit = () => {
      // Basic validation for now
      // In a real app, you'd get values from a form library
      const customerSelected = (document.getElementById('customerSelect') as HTMLSelectElement)?.value;
      if (!customerSelected) {
          setError('Please select a customer');
          return;
      }
      setError(null);
      onCreate();
  }

  return (
    <div className="p-7">
        <div className="flex items-center justify-between mb-7">
            <h2 className="text-2xl font-extrabold">New Order</h2>
            <button onClick={onCancel} className="p-1.5 text-gray-500 hover:text-gray-800">
                <svg width="20" height="20" fill="none" viewBox="0 0 20 20">
                    <path d="M5 5l10 10M15 5L5 15" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                </svg>
            </button>
        </div>

        <div className="mb-5">
            <label className="block text-xs font-bold text-gray-500 mb-1.5 uppercase tracking-wider">Customer</label>
            <select id="customerSelect" className="w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm">
                <option value="">Select a customer...</option>
                {customers.map((customer) => (
                    <option key={customer.id} value={customer.id}>
                        {customer.name} — {customer.email} ({customer.type})
                    </option>
                ))}
            </select>
        </div>

        <div className="mb-5">
            <label className="block text-xs font-bold text-gray-500 mb-2.5 uppercase tracking-wider">Line Items</label>
            <div className="grid gap-2.5">
                {lineItems.map((_, index) => (
                    <LineItemRow key={index} onRemove={() => removeLineItem(index)} />
                ))}
            </div>
            <button onClick={addLineItem} className="mt-2.5 w-full rounded-2xl border-2 border-dashed border-gray-200 p-2.5 bg-transparent text-green-700 font-bold text-sm flex items-center justify-center gap-1.5">
                <svg width="14" height="14" fill="none" viewBox="0 0 14 14">
                    <path d="M7 2v10M2 7h10" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                </svg>
                Add item
            </button>
        </div>

        <div className="mb-7">
            <label className="block text-xs font-bold text-gray-500 mb-1.5 uppercase tracking-wider">Notes <span className="font-normal normal-case">(optional)</span></label>
            <textarea rows={3} placeholder="Add any notes about this order..." className="w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm resize-y"></textarea>
        </div>

        <div className="p-4 rounded-2xl bg-gray-100 border border-gray-200 mb-5">
            <div className="flex justify-between items-center">
                <span className="text-gray-600 text-sm font-semibold">Estimated total</span>
                <span className="text-2xl font-extrabold text-green-700">$0.00</span>
            </div>
            <div className="text-gray-500 text-xs mt-1">{lineItems.length} item(s) · Final total confirmed after submission</div>
        </div>
        
        {error && <AlertBanner variant="error" title={error} />}

        <div className="grid grid-cols-2 gap-2.5">
            <button onClick={onCancel} className="rounded-2xl border border-gray-200 p-3 bg-white text-gray-800 font-bold text-sm">
                Cancel
            </button>
            <button onClick={handleSubmit} className="rounded-2xl border border-transparent p-3 bg-green-700 text-white font-extrabold text-sm">
                Create Order
            </button>
        </div>
    </div>
  );
};

export default CreateOrderForm;
