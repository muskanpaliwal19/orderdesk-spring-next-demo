'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

interface Quote {
  id?: string;
  customer: string;
  amount: number;
  status: string;
}

interface QuoteFormProps {
  quote?: Quote;
}

export default function QuoteForm({ quote }: QuoteFormProps) {
  const router = useRouter();
  const [customer, setCustomer] = useState(quote?.customer || '');
  const [amount, setAmount] = useState(quote?.amount || 0);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newQuote = { customer, amount };

    if (quote) {
      // In a real app, you would make a PUT/PATCH request
      console.log('Updating quote:', quote.id, newQuote);
      alert('Quote updated successfully!');
      router.push(`/quotes/${quote.id}`);
    } else {
      // In a real app, you would make a POST request
      console.log('Creating quote:', newQuote);
      alert('Quote created successfully!');
      router.push('/quotes');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '600px', margin: '0 auto' }}>
      <div style={{ marginBottom: '1rem' }}>
        <label htmlFor="customer" style={{ display: 'block', marginBottom: '0.5rem' }}>Customer</label>
        <input
          type="text"
          id="customer"
          value={customer}
          onChange={(e) => setCustomer(e.target.value)}
          required
          style={{ width: '100%', padding: '0.5rem', border: '1px solid #ccc', borderRadius: '0.25rem' }}
        />
      </div>
      <div style={{ marginBottom: '1rem' }}>
        <label htmlFor="amount" style={{ display: 'block', marginBottom: '0.5rem' }}>Amount</label>
        <input
          type="number"
          id="amount"
          value={amount}
          onChange={(e) => setAmount(Number(e.target.value))}
          required
          style={{ width: '100%', padding: '0.5rem', border: '1px solid #ccc', borderRadius: '0.25rem' }}
        />
      </div>
      <div style={{ marginTop: '2rem' }}>
        <button type="submit" style={{ marginRight: '1rem', padding: '0.5rem 1rem', backgroundColor: '#0070f3', color: 'white', border: 'none", borderRadius: '0.25rem', cursor: 'pointer' }}>
          {quote ? 'Update Quote' : 'Create Quote'}
        </button>
        <Link href={quote ? `/quotes/${quote.id}` : '/quotes'} style={{ textDecoration: 'none', color: '#0070f3' }}>
          Cancel
        </Link>
      </div>
    </form>
  );
}
