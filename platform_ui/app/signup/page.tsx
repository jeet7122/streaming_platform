import { AuthForm } from "@/components/auth-form";

export default function SignupPage() {
    return (
        <div className="flex items-center justify-center h-screen">
            <AuthForm type="signup" />
        </div>
    );
}