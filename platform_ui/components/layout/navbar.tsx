// components/navbar.tsx
export default function Navbar() {
    return (
        <header>
            <div className="flex items-center justify-between px-6 py-3 border-b sticky top-0 bg-white z-50">
                {/* Logo */}
                <h1 className="font-bold text-lg">StreamFlow</h1>

                {/* Search */}
                <input
                    type="text"
                    placeholder="Search"
                    className="border px-4 py-1 rounded-full w-1/3"
                />

                {/* Profile */}
                <div className="w-8 h-8 bg-gray-300 rounded-full" />
            </div>
        </header>
    );
}