'use client'

import Link from 'next/link';
import { usePathname } from 'next/navigation';

const navLinks = [
    { href: '/', label: 'Dashboard' },
    { href: '/orders', label: 'Orders' },
    { href: '/customers', label: 'Customers' },
    { href: '/products', label: 'Products' },
];

export default function Header() {
    const pathname = usePathname();

    return (
        <nav className="border-b border-line bg-surface/80 backdrop-blur-sm sticky top-0 z-10">
            <div className="max-w-5xl mx-auto px-4 h-14 flex items-center gap-6">
                <Link href="/" className="font-extrabold text-lg tracking-tight text-brand">OrderDesk</Link>
                <div className="flex gap-1 text-sm">
                    {navLinks.map(link => {
                        const isActive = pathname === link.href;
                        return (
                            <Link
                                key={link.href}
                                href={link.href}
                                className={`px-3 py-1.5 rounded-lg transition ${isActive ? 'bg-brand/10 text-brand font-semibold' : 'text-muted hover:bg-brand/5'}`}>
                                {link.label}
                            </Link>
                        )
                    })}
                </div>
            </div>
        </nav>
    );
}
