
import { Customer } from '@/types/customer';
import CustomerTable from '@/components/CustomerTable';

async function getCustomers(): Promise<Customer[]> {
  const res = await fetch('/api/customers', { cache: 'no-store' });

  if (!res.ok) {
    throw new Error("Failed to fetch customers");
  }
  return res.json();
}

export default async function CustomersPage() {
  const customers = await getCustomers();

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Customers</h1>
      <CustomerTable customers={customers} />
    </div>
  );
}

