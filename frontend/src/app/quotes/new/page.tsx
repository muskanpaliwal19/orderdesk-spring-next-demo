import QuoteForm from "@/components/quotes/QuoteForm";

export default function NewQuotePage() {
  return (
    <main className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-4">New Quote</h1>
      <QuoteForm />
    </main>
  );
}
