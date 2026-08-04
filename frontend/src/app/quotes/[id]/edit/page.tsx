'use client';

import { useParams } from 'next/navigation';
import QuoteForm from "@/components/quotes/QuoteForm";

// Dummy data for a single quote
const dummyQuote = {
  id: '1', 
  customer: 'Acme Corp', 
  amount: 1200.50, 
  status: 'Draft'
};

export default function EditQuotePage() {
  const params = useParams();
  const { id } = params;

  // In a real app, you would fetch the quote by id
  const quote = dummyQuote;

  return (
    <main className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-4">Edit Quote #{id}</h1>
      <QuoteForm quote={quote} />
    </main>
  );
}
