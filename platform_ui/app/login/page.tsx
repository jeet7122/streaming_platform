import { AuthForm } from "@/components/auth-form";

export default function LoginPage() {
    return (
        <div className="flex items-center justify-center h-screen">
            <AuthForm type="login" />
        </div>
    );
}