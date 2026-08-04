"use client";

import { useState, useEffect } from "react";
import { Customer } from "@/types/customer";
import CustomerTable from "@/components/CustomerTable";
import CustomerForm from "@/app/components/CustomerForm";
import ErrorBanner from "@/app/components/ErrorBanner";

export default function CustomersPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchCustomers = async () => {
    setIsLoading(true);
    try {
      const res = await fetch("/api/customers");
      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to fetch customers.");
      }
      const data = await res.json();
      setCustomers(data);
    } catch (e) {
      setError(e instanceof Error ? e.message : "An unknown error occurred.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  const handleCustomerAdded = (newCustomer: Customer) => {
    
    fetchCustomers();
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Customers</h1>
      {error && <ErrorBanner message={error} />}
      <CustomerForm onCustomerAdded={handleCustomerAdded} />
      {isLoading ? <p>Loading customers...</p> : <CustomerTable customers={customers} />}
    </div>
  );
}
