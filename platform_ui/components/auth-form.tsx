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
    const apiBase = process.env.NEXT_PUBLIC_AUTH_API + "/" + type;

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e: any) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async () => {
        setLoading(true);

        try {
            const res = await fetch(
                apiBase,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(form),
                }
            );

            const data = await res.json();

            // store JWT
            localStorage.setItem("token", data.token);

            // redirect to homepage
            router.push("/");
        } catch (err) {
            console.error(err);
            alert("Something went wrong");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="w-full max-w-md p-6 border rounded-2xl shadow-sm">
            <h2 className="text-2xl font-bold mb-6 text-center">
                {type === "login" ? "Login" : "Create Account"}
            </h2>

            <div className="space-y-4">
                {type === "signup" && (
                    <Input
                        name="name"
                        placeholder="Full Name"
                        onChange={handleChange}
                    />
                )}

                <Input
                    name="email"
                    placeholder="Email"
                    type="email"
                    onChange={handleChange}
                />

                <Input
                    name="password"
                    placeholder="Password"
                    type="password"
                    onChange={handleChange}
                />

                <Button
                    className="w-full"
                    onClick={handleSubmit}
                    disabled={loading}
                >
                    {loading
                        ? "Loading..."
                        : type === "login"
                            ? "Login"
                            : "Sign Up"}
                </Button>
            </div>

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