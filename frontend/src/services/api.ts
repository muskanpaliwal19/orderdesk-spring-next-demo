export type OrderData = {
  customerId: number;
  items: { productId: number | undefined; quantity: number }[];
};

export const createOrder = async (orderData: OrderData) => {
  const res = await fetch('/api/orders', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(orderData),
  });

  if (!res.ok) {
    const contentType = res.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      const errorData = await res.json();
      throw new Error(errorData.message || 'Failed to create order. Please try again.');
    } else {
      const errorText = await res.text();
      console.error('Error creating order (non-JSON response):', errorText);
      throw new Error('Failed to create order. Server returned an unexpected response.');
    }
  }

  return res.json();
};
