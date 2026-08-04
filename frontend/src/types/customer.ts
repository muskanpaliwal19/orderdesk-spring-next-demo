
export type Customer = {
  id: number;
  name: string;
  email: string;
  tier: 'BASIC' | 'STANDARD' | 'PREMIUM' | 'VIP';
};
