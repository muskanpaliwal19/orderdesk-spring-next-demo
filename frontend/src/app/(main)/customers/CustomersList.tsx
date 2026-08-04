'use client';

import { useState } from 'react';
import { Customer } from '@/types/customer';
import CustomerTable from '@/components/CustomerTable';
import CustomerForm from '@/components/CustomerForm';
import ErrorBanner from '@/components/ErrorBanner';

export default function CustomersList({ initialCustomers, initialError }: { initialCustomers: Customer[], initialError: string | null }) {
  const [customers, setCustomers] = useState<Customer[]>(initialCustomers);
  const [error, setError] = useState<string | null>(initialError);
  const [isLoading, setIsLoading] = useState(false);

  const fetchCustomers = async () => {
    try {
      setIsLoading(true);
      const res = await fetch('/api/customers', { cache: 'no-store' });
      if (!res.ok) {
        throw new Error('Failed to fetch customers');
      }
      const data = await res.json();
      setCustomers(data);
      setError(null);
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

  const handleCustomerAdded = () => {
    fetchCustomers();
  };

  return (
    <>
      <ErrorBanner message={error} />
      <CustomerForm onCustomerAdded={handleCustomerAdded} onError={setError} />
      {isLoading ? (
        <p>Loading customers...</p>
      ) : (
        <CustomerTable customers={customers} />
      )}
    </>
  );
}