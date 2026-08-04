"use client";

import { useState, FormEvent } from "react";
import { Customer } from "@/types/customer";
import ErrorBanner from "@/app/components/ErrorBanner";

interface CustomerFormProps {
  onCustomerAdded: (customer: Customer) => void;
}

export default function CustomerForm({ onCustomerAdded }: CustomerFormProps) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [tier, setTier] = useState("STANDARD");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    if (!name || !email) {
      setError("Name and email are required.");
      setIsSubmitting(false);
      return;
    }

    try {
      const response = await fetch("/api/customers", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name, email, tier }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to add customer");
      }

      const newCustomer = await response.json();
      onCustomerAdded(newCustomer);
      setName("");
      setEmail("");
      setTier("STANDARD");
    } catch (error) {
        if (error instanceof Error) {
            setError(error.message);
        } else {
            setError("An unknown error occurred.");
        }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-8 bg-white shadow-md rounded px-8 pt-6 pb-8">
        <h2 className="text-2xl font-bold mb-6 text-gray-800">Add New Customer</h2>
      <div className="mb-4">
        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">
          Name
        </label>
        <input
          id="name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          required
          data-testid="name-input"
        />
      </div>
      <div className="mb-6">
        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
          Email
        </label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
          required
          data-testid="email-input"
        />
      </div>
      <div className="mb-6">
        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="tier">
          Tier
        </label>
        <select
          id="tier"
          value={tier}
          onChange={(e) => setTier(e.target.value)}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          data-testid="tier-select"
        >
          <option value="STANDARD">Standard</option>
          <option value="PREMIUM">Premium</option>
          <option value="ENTERPRISE">Enterprise</option>
        </select>
      </div>
      <div className="flex items-center justify-between">
        <button
          type="submit"
          disabled={isSubmitting}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:bg-gray-400"
          data-testid="submit-button"
        >
          {isSubmitting ? "Adding..." : "Add Customer"}
        </button>
      </div>
      {error && <div className="mt-4"><ErrorBanner message={error} /></div>}
    </form>
  );
}