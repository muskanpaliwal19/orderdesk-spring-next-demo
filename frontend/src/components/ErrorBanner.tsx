'use client';

import { useEffect, useState } from 'react';

export default function ErrorBanner({ message }: { message: string | null }) {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    if (message) {
      setVisible(true);
      const timer = setTimeout(() => {
        setVisible(false);
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [message]);

  if (!visible) {
    return null;
  }

  return (
    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
      <span className="block sm:inline">{message}</span>
    </div>
  );
}
