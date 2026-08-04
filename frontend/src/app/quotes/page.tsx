
import QuoteList from "@/components/quotes/QuoteList";

export default function QuotesPage() {
  return (
    <main className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-4">Quotes</h1>
      <QuoteList />
    </main>
  );
}
