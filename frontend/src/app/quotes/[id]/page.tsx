'use client';

import { useParams } from 'next/navigation';
import Link from 'next/link';
import DeleteButton from '@/components/quotes/DeleteButton';

// Dummy data for a single quote
const dummyQuote = {
  id: '1', 
  customer: 'Acme Corp', 
  amount: 1200.50, 
  status: 'Draft',
  items: [
    { id: 'item-1', description: 'Product A', quantity: 2, price: 300.25 },
    { id: 'item-2', description: 'Service B', quantity: 1, price: 600.00 },
  ]
};

export default function QuoteDetailPage() {
  const params = useParams();
  const { id } = params;

  // In a real app, you would fetch the quote by id
  const quote = dummyQuote;

  if (!quote) {
    return <div>Quote not found</div>;
  }

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '2rem' }}>
      <h1 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1rem' }}>Quote #{quote.id}</h1>
      <div style={{ marginBottom: '1rem' }}>
        <p><strong>Customer:</strong> {quote.customer}</p>
        <p><strong>Amount:</strong> ${quote.amount.toFixed(2)}</p>
        <p><strong>Status:</strong> {quote.status}</p>
      </div>

      <h2 style={{ fontSize: '1.5rem', fontWeight: 'bold', marginTop: '2rem', marginBottom: '1rem' }}>Items</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Description</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Quantity</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Price</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Total</th>
          </tr>
        </thead>
        <tbody>
          {quote.items.map(item => (
            <tr key={item.id}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{item.description}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{item.quantity}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>${item.price.toFixed(2)}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>${(item.quantity * item.price).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={{ marginTop: '2rem' }}>
        <Link href={`/quotes/${quote.id}/edit`} style={{ marginRight: '1rem', textDecoration: 'none', padding: '0.5rem 1rem', backgroundColor: '#0070f3', color: 'white', borderRadius: '0.25rem' }}>Edit</Link>
        <DeleteButton quoteId={quote.id} />
        <Link href="/quotes" style={{ marginLeft: '1rem' }}>Back to Quotes</Link>
      </div>
    </div>
  );
}
