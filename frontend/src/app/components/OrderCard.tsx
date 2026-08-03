
import { Order } from '@/types/order';
import StatusBadge from './StatusBadge';

function formatCurrency(cents: number) {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(cents / 100);
}

const OrderCard = ({ order }: { order: Order }) => {
  return (
    <div className="bg-white/90 border border-line rounded-2xl p-4 shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between gap-3 mb-2">
        <div className="flex items-center gap-2.5">
          <span className="font-bold text-base">#{order.id}</span>
          <span className="font-semibold text-base">{order.customerName}</span>
          <StatusBadge status={order.status} />
        </div>
        <span className="font-bold text-brand text-lg tabular-nums">
          {formatCurrency(order.totalCents)}
        </span>
      </div>
      <div className="flex items-center gap-2 text-xs text-muted">
        <span>{order.customerEmail}</span>
        <span className="text-line">·</span>
        <span>
          {new Date(order.orderDate).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric',
          }){' '}}
          at{' '}}
          {new Date(order.orderDate).toLocaleTimeString('en-US', {
            hour: 'numeric',
            minute: '2-digit',
          })}
        </span>
      </div>
      {order.notes && (
        <div className="mt-2 text-sm text-muted/80 bg-bg/60 rounded-lg px-3 py-1.5">
          {order.notes}
        </div>
      )}
    </div>
  );
};

export default OrderCard;
