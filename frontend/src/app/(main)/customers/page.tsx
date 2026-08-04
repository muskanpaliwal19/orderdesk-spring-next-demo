'use client';

import { useState, useEffect } from 'react';
import { Customer } from '@/types/customer';
import CustomerTable from '@/components/CustomerTable';
import CustomerForm from '@/components/CustomerForm';
import ErrorBanner from '@/components/ErrorBanner';

export default function CustomersPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchCustomers = async () => {
    try {
      setIsLoading(true);
      const res = await fetch('/api/customers', { cache: 'no-store' });
      if (!res.ok) {
        throw new Error('Failed to fetch customers');
      }
      const data = await res.json();
      setCustomers(data);
    } catch (err) {
        if (err instanceof Error) {
            setError(err.message);
        } else {
            setError('An unknown error occurred.');
        }
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  const handleCustomerAdded = () => {
    fetchCustomers();
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Customers</h1>
      <ErrorBanner message={error} />
      <CustomerForm onCustomerAdded={handleCustomerAdded} onError={setError} />
      {isLoading ? (
        <p>Loading customers...</p>
      ) : (
        <CustomerTable customers={customers} />
      )}
    </div>
  );
}
