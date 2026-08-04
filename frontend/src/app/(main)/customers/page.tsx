import { Customer } from '@/types/customer';
import CustomersList from './CustomersList';

async function getCustomers(): Promise<{ customers: Customer[]; error: string | null }> {
  try {
    // In a real app, use an environment variable for the API URL.
    const res = await fetch('http://localhost:8080/api/customers', { cache: 'no-store' });

    if (!res.ok) {
      // Per customer feedback, we're now parsing the JSON body of the error response.
      // This provides more specific details about why the request failed.
      const errorData = await res.json();
      return { customers: [], error: errorData.message || 'Failed to fetch customers.' };
    }

    return { customers: await res.json(), error: null };
  } catch (e) {
    const error = e instanceof Error ? e.message : 'An unknown error occurred.';
    return { customers: [], error };
  }
}

export default async function CustomersPage() {
  const { customers, error } = await getCustomers();

  return (
    <div>
      <h1 className=\"text-3xl font-bold mb-6\">Customers</h1>
      <CustomersList initialCustomers={customers} initialError={error} />
    </div>
  );
}