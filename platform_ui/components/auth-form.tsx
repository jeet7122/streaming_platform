"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Link from "next/link";
import { useRouter } from "next/navigation";

type AuthFormProps = {
    type: "login" | "signup";
};

export function AuthForm({ type }: AuthFormProps) {
    const router = useRouter();

    const apiBase = `${process.env.NEXT_PUBLIC_AUTH_API}/${type}`;

    const [form, setForm] = useState({
        fullname: "",
        email: "",
        password: "",
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault(); // 🔥 prevents unwanted GET

        if (loading) return;

        setLoading(true);

        try {
            console.log("API URL:", apiBase); // debug

            const res = await fetch(apiBase, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(form),
            });

            // 🔥 handle non-200 responses properly
            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || "Request failed");
            }

            const data = await res.json();

            if (!data?.token) {
                throw new Error("Invalid response from server");
            }

            localStorage.setItem("token", data.token);

            router.push("/");
        } catch (err: any) {
            console.error("Auth error:", err);
            alert(err.message || "Something went wrong");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="w-full max-w-md p-6 border rounded-2xl shadow-sm">
            <h2 className="text-2xl font-bold mb-6 text-center">
                {type === "login" ? "Login" : "Create Account"}
            </h2>

            {/* ✅ FORM WRAPPER (important) */}
            <form onSubmit={handleSubmit} className="space-y-4">
                {type === "signup" && (
                    <Input
                        name="fullname"
                        placeholder="Full Name"
                        onChange={handleChange}
                        required
                    />
                )}

                <Input
                    name="email"
                    placeholder="Email"
                    type="email"
                    onChange={handleChange}
                    required
                />

                <Input
                    name="password"
                    placeholder="Password"
                    type="password"
                    onChange={handleChange}
                    required
                />

                <Button
                    type="submit"
                    className="w-full"
                    disabled={loading}
                >
                    {loading
                        ? "Loading..."
                        : type === "login"
                            ? "Login"
                            : "Sign Up"}
                </Button>
            </form>

            <p className="text-sm text-center mt-4">
                {type === "login" ? (
                    <>
                        Don't have an account?{" "}
                        <Link href="/signup" className="text-blue-500">
                            Sign up
                        </Link>
                    </>
                ) : (
                    <>
                        Already have an account?{" "}
                        <Link href="/login" className="text-blue-500">
                            Login
                        </Link>
                    </>
                )}
            </p>
        </div>
    );
}