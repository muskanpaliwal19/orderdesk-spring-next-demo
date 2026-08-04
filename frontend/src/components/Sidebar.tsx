
import Link from 'next/link';

const Sidebar = () => {
  return (
    <div className="w-64 bg-gray-800 text-white h-screen p-4">
      <h2 className="text-2xl font-bold mb-8">CRM</h2>
      <nav>
        <ul>
          <li>
            <Link href="/customers" className="block p-2 rounded hover:bg-gray-700">
              Customers
            </Link>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
