'use client';

import { useRouter } from 'next/navigation';

export default function DeleteButton({ quoteId }: { quoteId: string }) {
  const router = useRouter();

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this quote?')) {
      // In a real application, you would make an API call to delete the quote
      console.log(`Deleting quote ${quoteId}`);
      alert(`Quote ${quoteId} deleted successfully!`);
      router.push('/quotes');
    }
  };

  return (
    <button 
      onClick={handleDelete} 
      style={{ 
        padding: '0.5rem 1rem', 
        backgroundColor: '#dc3545', 
        color: 'white', 
        border: 'none', 
        borderRadius: '0.25rem', 
        cursor: 'pointer' 
      }}
    >
      Delete
    </button>
  );
}
