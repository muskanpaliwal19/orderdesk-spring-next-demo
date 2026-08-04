'use client';

import { useState } from 'react';
import { Customer, ServiceTier } from '@/types/customer';

interface CustomerFormProps {
  onCustomerAdded: (customer: Customer) => void;
  onError: (message: string | null) => void;
}

export default function CustomerForm({ onCustomerAdded, onError }: CustomerFormProps) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [tier, setTier] = useState<ServiceTier>('standard');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    onError(null);

    try {
      const res = await fetch('/api/customers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, tier }),
      });

      if (!res.ok) {
        const errorData = await res.json();
        onError(errorData.message || 'Failed to create customer');
        return;
      }

      const newCustomer = await res.json();
      onCustomerAdded(newCustomer);
      setName('');
      setEmail('');
      setTier('standard');
    } catch (e) {
      const error = e instanceof Error ? e.message : 'An unknown error occurred.';
      onError(error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-6">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="border p-2 rounded w-full"
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="border p-2 rounded w-full"
          required
        />
        <select
          value={tier}
          onChange={(e) => setTier(e.target.value as ServiceTier)}
          className="border p-2 rounded w-full"
        >
          <option value="standard">Standard</option>
          <option value="premium">Premium</option>
          <option value="enterprise">Enterprise</option>
        </select>
      </div>
      <button type="submit" className="bg-blue-500 text-white p-2 rounded mt-4 w-full md:w-auto">
        Add Customer
      </button>
    </form>
  );
}
