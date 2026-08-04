
import React from 'react';

interface RevenueStatCardProps {
  totalRevenue?: number;
  isLoading: boolean;
  isError: boolean;
}

const formatCurrency = (cents: number): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format((cents || 0) / 100);
};

const DollarIcon = () => (
    <svg
      className="w-4 h-4 text-gray-400"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      viewBox="0 0 24 24"
      aria-hidden="true"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"
      />
    </svg>
  );

const RevenueStatCard: React.FC<RevenueStatCardProps> = ({
  totalRevenue,
  isLoading,
  isError,
}) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 max-w-xs">
      <div className="flex items-center gap-2 mb-1">
        <DollarIcon />
        <span className="text-sm font-medium text-gray-500">Total Revenue</span>
      </div>
      {isLoading && (
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded-md w-3/4"></div>
        </div>
      )}
      {isError && (
        <p className="text-3xl font-bold text-red-600">Error</p>
      )}
      {!isLoading && !isError && totalRevenue !== undefined && (
        <p className="text-3xl font-bold text-gray-900">
          {formatCurrency(totalRevenue)}
        </p>
      )}
       {!isLoading && !isError && totalRevenue === undefined && (
         <p className="text-3xl font-bold text-gray-900">-</p>
       )}
    </div>
  );
};

export default RevenueStatCard;
