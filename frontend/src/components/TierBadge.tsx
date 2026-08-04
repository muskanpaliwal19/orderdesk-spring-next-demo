
import { Customer } from '@/types/customer';

type TierBadgeProps = {
  tier: Customer['tier'];
};

const tierColors: Record<Customer['tier'], string> = {
  BASIC: 'bg-gray-200 text-gray-800',
  PREMIUM: 'bg-blue-200 text-blue-800',
  VIP: 'bg-purple-200 text-purple-800',
};

const TierBadge = ({ tier }: TierBadgeProps) => {
  return (
    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${tierColors[tier]}`}>
      {tier}
    </span>
  );
};

export default TierBadge;
