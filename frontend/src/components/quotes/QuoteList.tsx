'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';

// Dummy data for quotes
const dummyQuotes = [
  { id: '1', customer: 'Acme Corp', amount: 1200.50, status: 'Draft' },
  { id: '2', customer: 'Stark Industries', amount: 5500.00, status: 'Sent' },
  { id: '3', customer: 'Wayne Enterprises', amount: 2300.75, status: 'Accepted' },
];

export default function QuoteList() {
  const [quotes, setQuotes] = useState<any[]>([]);

  useEffect(() => {
    // In a real application, you would fetch this data from an API
    setQuotes(dummyQuotes);
  }, []);

  return (
    <div>
      <div style={{ marginBottom: '1rem' }}>
        <Link href="/quotes/new" style={{ textDecoration: 'none', padding: '0.5rem 1rem', backgroundColor: '#0070f3', color: 'white', borderRadius: '0.25rem' }}>
          New Quote
        </Link>
      </div>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Customer</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Amount</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Status</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {quotes.map((quote) => (
            <tr key={quote.id}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{quote.customer}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{quote.amount.toFixed(2)}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{quote.status}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <Link href={`/quotes/${quote.id}`} style={{ marginRight: '0.5rem' }}>View</Link>
                <Link href={`/quotes/${quote.id}/edit`}>Edit</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
