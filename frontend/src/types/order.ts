
import { Customer } from "./customer";

export interface OrderItem {
  id: number;
  productId: number;
  quantity: number;
  price: number;
  productName?: string;
}

export interface Order {
  id: number;
  customer: Customer;
  items: OrderItem[];
  total: number;
  status: string;
}
