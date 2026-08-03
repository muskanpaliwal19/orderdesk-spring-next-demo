'use client';
import React from 'react';

// This will be replaced with data from an API call
const products = [
  { id: '1', name: 'Planning Board', price: 24.99 },
  { id: '2', name: 'Desk Lamp', price: 45.99 },
  { id: '3', name: 'Ergo Chair', price: 189.99 },
  { id: '4', name: 'USB-C Cable', price: 12.99 },
];

interface LineItemRowProps {
  onRemove: () => void;
}

const LineItemRow: React.FC<LineItemRowProps> = ({ onRemove }) => {
  return (
    <div className="grid grid-cols-[1fr,80px,36px] gap-2 items-center animate-fadeIn">
      <select
        className="w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm"
      >
        <option value="">Select product...</option>
        {products.map((product) => (
          <option key={product.id} value={product.id}>
            {product.name} — ${product.price}
          </option>
        ))}
      </select>
      <input
        type="number"
        defaultValue="1"
        min="1"
        className="w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm text-center"
      />
      <button
        onClick={onRemove}
        className="w-9 h-9 rounded-lg border border-red-200 bg-red-50 text-red-700 flex items-center justify-center"
      >
        <svg
          width="14"
          height="14"
          fill="none"
          viewBox="0 0 14 14"
        >
          <path
            d="M3 7h8"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
          />
        </svg>
      </button>
    </div>
  );
};

export default LineItemRow;
