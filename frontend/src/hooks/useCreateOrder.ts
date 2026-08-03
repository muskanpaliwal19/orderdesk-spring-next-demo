import { useState } from 'react';
import { toast } from 'sonner';
import { type OrderData, createOrder as apiCreateOrder } from '../services/api';

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
      await apiCreateOrder(orderData);
      toast.success('Order created successfully!');
      onOrderCreated();
      onCancel();
    } catch (e: any) {
      const message = e.message || 'An unexpected error occurred. Please check your connection and try again.';
      setError(message);
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createOrder, isSubmitting, error, setError };
};
