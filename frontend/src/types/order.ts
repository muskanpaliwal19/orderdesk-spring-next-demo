
export interface Order { 
    id: number;
    customerName: string;
    customerEmail: string;
    status: string;
    totalCents: number;
    orderDate: string;
    notes: string | null;
}
