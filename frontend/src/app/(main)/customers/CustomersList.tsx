'use client';

import { useState } from 'react';
import { Customer } from '@/types/customer';
import CustomerTable from '@/components/CustomerTable';
import CustomerForm from '@/components/CustomerForm';
import ErrorBanner from '@/components/ErrorBanner';

export default function CustomersList({ initialCustomers, initialError }: { initialCustomers: Customer[], initialError: string | null }) {
  const [customers, setCustomers] = useState<Customer[]>(initialCustomers);
  const [error, setError] = useState<string | null>(initialError);

  const handleCustomerAdded = (newCustomer: Customer) => {
    setCustomers((prevCustomers) => [newCustomer, ...prevCustomers]);
  };

  return (
    <>
      <ErrorBanner message={error} />
      <CustomerForm onCustomerAdded={handleCustomerAdded} onError={setError} />
      <CustomerTable customers={customers} />
    </>
  );
}