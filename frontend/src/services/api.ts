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
    const errorData = await res.json();
    throw new Error(errorData.message || 'Failed to create order. Please try again.');
  }

  return res.json();
};
