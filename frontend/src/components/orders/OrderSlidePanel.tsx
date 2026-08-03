'use client';

import React from 'react';
import CreateOrderForm from './CreateOrderForm';

interface OrderSlidePanelProps {
  isOpen: boolean;
  onClose: () => void;
  onOrderCreated: () => void; // Callback for when order is successfully created
}

const OrderSlidePanel: React.FC<OrderSlidePanelProps> = ({ isOpen, onClose, onOrderCreated }) => {
  const handleOrderCreation = () => {
    onOrderCreated();
    onClose();
  }

  return (
    <>
      <div
        className={`backdrop ${isOpen ? 'opacity-100 pointer-events-auto' : 'opacity-0 pointer-events-none'}`}
        onClick={onClose}
      />
      <div
        className={`slide-panel ${isOpen ? 'translate-x-0' : 'translate-x-full'}`}>
        <CreateOrderForm onCancel={onClose} onCreate={handleOrderCreation} />
      </div>
    </>
  );
};

export default OrderSlidePanel;
