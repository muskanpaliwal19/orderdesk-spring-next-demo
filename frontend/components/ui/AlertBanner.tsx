import React from 'react';

interface AlertBannerProps {
  variant: 'success' | 'warning' | 'error';
  title: string;
  message?: string;
}

const variants = {
  success: {
    container: 'bg-green-50 border-green-200 text-green-800',
    iconContainer: 'bg-green-800',
    icon: (
      <svg
        width="20"
        height="20"
        fill="none"
        viewBox="0 0 20 20"
      >
        <circle cx="10" cy="10" r="10" fill="currentColor" />
        <path
          d="M6 10.5l2.5 2.5 5.5-5.5"
          stroke="#fff"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
  },
  warning: {
    container: 'bg-yellow-50 border-yellow-200 text-yellow-800',
    iconContainer: 'bg-yellow-500',
    icon: (
      <svg width="20" height="20" fill="none" viewBox="0 0 20 20">
        <circle cx="10" cy="10" r="10" fill="#d36b37" />
        <path d="M10 6v5m0 2.5v.5" stroke="#fff" strokeWidth="2" strokeLinecap="round" />
      </svg>
    ),
  },
    error: {
        container: 'bg-red-50 border-red-200 text-red-800',
        iconContainer: 'bg-red-500',
        icon: (
            <svg width="20" height="20" fill="none" viewBox="0 0 20 20">
                <circle cx="10" cy="10" r="10" fill="#991b1b" />
                <path d="M10 6v5m0 2.5v.5" stroke="#fff" strokeWidth="2" strokeLinecap="round" />
            </svg>
        ),
    },
};

const AlertBanner: React.FC<AlertBannerProps> = ({ variant, title, message }) => {
  const config = variants[variant];

  return (
    <div className={`mb-4 p-4 rounded-2xl border ${config.container}`}>
      <div className="flex items-center gap-2">
        {config.icon}
        <div>
          <strong className="font-bold">{title}</strong>
          {message && <span className="ml-1">— {message}</span>}
        </div>
      </div>
    </div>
  );
};

export default AlertBanner;
