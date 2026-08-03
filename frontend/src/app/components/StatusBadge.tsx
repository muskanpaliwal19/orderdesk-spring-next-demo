import React from 'react';

const statusStyles: { [key: string]: { bg: string; text: string; ring: string } } = {
  new: { bg: 'bg-blue-50', text: 'text-blue-700', ring: 'ring-blue-200' },
  paid: { bg: 'bg-emerald-50', text: 'text-emerald-700', ring: 'ring-emerald-200' },
  shipped: { bg: 'bg-violet-50', text: 'text-violet-700', ring: 'ring-violet-200' },
  cancelled: { bg: 'bg-red-50', text: 'text-red-700', ring: 'ring-red-200' },
};

const StatusBadge = ({ status }: { status: string }) => {
  const s = statusStyles[status] || statusStyles.new;
  const badgeClasses = `inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold uppercase tracking-wide ring-1 ${s.bg} ${s.text} ${s.ring}`;

  return (
    <span className={badgeClasses}>
      {status}
    </span>
  );
};

export default StatusBadge;
