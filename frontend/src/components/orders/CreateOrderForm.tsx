'use client';

import React, { useState, useEffect } from 'react';
import { toast } from 'sonner';
import LineItemRow from './LineItemRow';
import AlertBanner from 'components/ui/AlertBanner';
import { Customer } from 'types/customer';
import { Product } from 'types/product';

interface CreateOrderFormProps {
  onCancel: () => void;
  onOrderCreated: () => void;
}

type LineItem = { productId?: number; quantity: number };

const CreateOrderForm: React.FC<CreateOrderFormProps> = ({ onCancel, onOrderCreated }) => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState<number | null>(null);
  const [lineItems, setLineItems] = useState<LineItem[]>([{ quantity: 1 }]);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [customersRes, productsRes] = await Promise.all([
          fetch('/api/customers'),
          fetch('/api/products'),
        ]);
        const customersData = await customersRes.json();
        const productsData = await productsRes.json();
        setCustomers(customersData);
        setProducts(productsData);
      } catch (e) {
        setError('Failed to load form data. Please try again later.');
        toast.error('Failed to load form data. Please try again later.');
      }
    };
    fetchData();
  }, []);

  const addLineItem = () => {
    setLineItems([...lineItems, { quantity: 1 }]);
  };

  const removeLineItem = (index: number) => {
    if (lineItems.length > 1) {
      const newLineItems = [...lineItems];
      newLineItems.splice(index, 1);
      setLineItems(newLineItems);
    }
  };

  const handleLineItemChange = (index: number, newItem: LineItem) => {
    const newLineItems = [...lineItems];
    newLineItems[index] = newItem;
    setLineItems(newLineItems);
  };

  const calculateTotal = () => {
    return lineItems.reduce((total, item) => {
      const product = products.find(p => p.id === item.productId);
      if (product) {
        return total + product.price * item.quantity;
      }
      return total;
    }, 0);
  };

  const handleSubmit = async () => {
    setError(null);
    if (!selectedCustomerId) {
      setError('Please select a customer');
      return;
    }

    const validLineItems = lineItems.filter(item => item.productId && item.quantity > 0);

    if (validLineItems.length === 0) {
        setError('Please add at least one valid line item.');
        return;
    }

    const orderData = {
      customerId: selectedCustomerId,
      items: validLineItems.map(item => ({ productId: item.productId, quantity: item.quantity })),
    };

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
      const message = 'An unexpected error occurred. Please check your connection and try again.';
      setError(message);
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const total = calculateTotal();

  return (
    <div className="p-7">
        <div className="flex items-center justify-between mb-7">
            <h2 className="text-2xl font-extrabold">New Order</h2>
            <button onClick={onCancel} className="p-1.5 text-gray-500 hover:text-gray-800">
                <svg width="20" height="20" fill="none" viewBox="0 0 20 20">
                    <path d="M5 5l10 10M15 5L5 15" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                </svg>
            </button>
        </div>

        <div className="mb-5">
            <label className="block text-xs font-bold text-gray-500 mb-1.5 uppercase tracking-wider">Customer</label>
            <select 
              id="customerSelect" 
              className="w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm" 
              value={selectedCustomerId || ''}
              onChange={(e) => setSelectedCustomerId(parseInt(e.target.value))}
            >
                <option value="">Select a customer...</option>
                {customers.map((customer) => (
                    <option key={customer.id} value={customer.id}>
                        {customer.name} — {customer.email}
                    </option>
                ))}
            </select>
        </div>

        <div className="mb-5">
            <label className="block text-xs font-bold text-gray-500 mb-2.5 uppercase tracking-wider">Line Items</label>
            <div className="grid gap-2.5">
                {lineItems.map((item, index) => (
                    <LineItemRow 
                      key={index} 
                      products={products}
                      item={item}
                      onItemChange={(newItem) => handleLineItemChange(index, newItem)}
                      onRemove={() => removeLineItem(index)} />
                ))}
            </div>
            <button onClick={addLineItem} className="mt-2.5 w-full rounded-2xl border-2 border-dashed border-gray-200 p-2.5 bg-transparent text-green-700 font-bold text-sm flex items-center justify-center gap-1.5">
                <svg width="14" height="14" fill="none" viewBox="0 0 14 14">
                    <path d="M7 2v10M2 7h10" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                </svg>
                Add item
            </button>
        </div>

        <div className="p-4 rounded-2xl bg-gray-100 border border-gray-200 mb-5">
            <div className="flex justify-between items-center">
                <span className="text-gray-600 text-sm font-semibold">Estimated total</span>
                <span className="text-2xl font-extrabold text-green-700">${total.toFixed(2)}</span>
            </div>
            <div className="text-gray-500 text-xs mt-1">{lineItems.length} item(s) · Final total confirmed after submission</div>
        </div>
        
        {error && <AlertBanner variant="error" title={error} />}

        <div className="grid grid-cols-2 gap-2.5">
            <button onClick={onCancel} disabled={isSubmitting} className="rounded-2xl border border-gray-200 p-3 bg-white text-gray-800 font-bold text-sm disabled:opacity-50">
                Cancel
            </button>
            <button onClick={handleSubmit} disabled={isSubmitting} className="rounded-2xl border border-transparent p-3 bg-green-700 text-white font-extrabold text-sm disabled:opacity-50 disabled:cursor-not-allowed">
                {isSubmitting ? 'Creating...' : 'Create Order'}
            </button>
        </div>
    </div>
  );
};

export default CreateOrderForm;
