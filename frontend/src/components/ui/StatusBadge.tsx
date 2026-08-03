import React from 'react';

type Status = 'NEW' | 'PAID' | 'SHIPPED' | 'CANCELLED';

interface StatusBadgeProps {
  status: Status;
}

const statusConfig = {
  NEW: {
    text: 'New',
    className: 'bg-blue-100 text-blue-800',
  },
  PAID: {
    text: 'Paid',
    className: 'bg-green-100 text-green-800',
  },
  SHIPPED: {
    text: 'Shipped',
    className: 'bg-yellow-100 text-yellow-800',
  },
  CANCELLED: {
    text: 'Cancelled',
    className: 'bg-red-100 text-red-800',
  },
};

const StatusBadge: React.FC<StatusBadgeProps> = ({ status }) => {
  const config = statusConfig[status];

  if (!config) {
    return null;
  }

  return (
    <span
      className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-bold uppercase tracking-wider ${config.className}`}>
      {config.text}
    </span>
  );
};

export default StatusBadge;
