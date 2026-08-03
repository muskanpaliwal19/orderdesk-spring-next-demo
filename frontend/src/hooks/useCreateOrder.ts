import { useState } from 'react';
import { toast } from 'sonner';

type OrderData = {
  customerId: number;
  items: { productId: number | undefined; quantity: number }[];
};

interface UseCreateOrderProps {
  onOrderCreated: () => void;
  onCancel: () => void;
}

export const useCreateOrder = ({ onOrderCreated, onCancel }: UseCreateOrderProps) => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createOrder = async (orderData: OrderData) => {
    setError(null);
    setIsSubmitting(true);
    try {
      const res = await fetch('/api/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData),
      });

      if (res.ok) {
        toast.success('Order created successfully!');
        onOrderCreated();
        onCancel();
      } else {
        const errorData = await res.json();
        const errorMessage = errorData.message || 'Failed to create order. Please try again.';
        setError(errorMessage);
        toast.error(errorMessage);
      }
    } catch (e) {
            console.error('An unexpected error occurred during order creation:', e);
      const message = 'An unexpected error occurred. Please check your connection and try again.';
      setError(message);
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createOrder, isSubmitting, error, setError };
};
