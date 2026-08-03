'use client';
import React from 'react';
import { Product } from '@/types/product';

interface LineItemRowProps {
  products: Product[];
  item: { productId?: number; quantity: number };
  onItemChange: (newItem: { productId?: number; quantity: number }) => void;
  onRemove: () => void;
}

const LineItemRow: React.FC<LineItemRowProps> = ({ products, item, onItemChange, onRemove }) => {
  const selectedProduct = products.find(p => p.id === item.productId);

  return (
    <div className=\"grid grid-cols-[1fr,90px,auto] gap-2.5 items-center animate-fadeIn\">
      <select
        value={item.productId || ''}
        onChange={(e) => onItemChange({ ...item, productId: parseInt(e.target.value) })}
        className=\"w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm\"
      >
        <option value=\"\">Select product...</option>
        {products.map((product) => (
          <option key={product.id} value={product.id}>
            {product.name}
          </option>
        ))}
      </select>
      <input
        type=\"number\"
        value={item.quantity}
        min=\"1\"
        onChange={(e) => onItemChange({ ...item, quantity: parseInt(e.target.value) || 1 })}
        className=\"w-full rounded-2xl border border-gray-200 p-3 bg-white text-sm text-center\"
      />
        <div className=\"flex items-center gap-2\">
            <span className=\"text-sm text-gray-500 font-mono w-[60px] text-right\">
                {selectedProduct ? `$${(selectedProduct.price * item.quantity).toFixed(2)}` : '-'}
            </span>
            <button
                onClick={onRemove}
                className=\"w-9 h-9 rounded-lg border border-red-200 bg-red-50 text-red-700 flex items-center justify-center\"
            >
                <svg width=\"14\" height=\"14\" fill=\"none\" viewBox=\"0 0 14 14\">
                <path d=\"M3 7h8\" stroke=\"currentColor\" strokeWidth=\"2\" strokeLinecap=\"round\" />
                </svg>
            </button>
      </div>
    </div>
  );
};

export default LineItemRow;
