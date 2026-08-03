import { useState, useEffect, useCallback } from 'react';
import { Order } from '@/types/order';

export function useOrders(statusFilter: string) {
  const [orders, setOrders] = useState<Order[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    const url = statusFilter ? `/api/orders?status=${statusFilter}` : '/api/orders';
    try {
      const res = await fetch(url);
      if (!res.ok) {
        throw new Error('Failed to fetch orders');
      }
      setOrders(await res.json());
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("An unknown error occurred.");
      }
    } finally {
      setIsLoading(false);
    }
  }, [statusFilter]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return { orders, isLoading, error, refetch: fetchData };
}
