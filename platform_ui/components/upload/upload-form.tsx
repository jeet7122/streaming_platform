"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { getAuthHeader } from "@/lib/api";
import VideoPreview from "./video-preview";
import MetadataForm from "./metadata-form";

export default function UploadForm({ file }: { file: File }) {
    const router = useRouter();

    const [videoUrl, setVideoUrl] = useState<string | null>(null);

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    const [progress, setProgress] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);

    // 🎥 Create preview URL
    useEffect(() => {
        const url = URL.createObjectURL(file);
        setVideoUrl(url);

        return () => URL.revokeObjectURL(url);
    }, [file]);

    const handleUpload = async () => {
        if (!title.trim()) {
            setError("Title is required");
            return;
        }

        setLoading(true);
        setError(null);
        setProgress(0);

        const formData = new FormData();
        formData.append("video", file);
        formData.append("title", title);
        formData.append("description", description);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/api/videos/upload");

        // 🔐 Auth headers
        const headers = getAuthHeader();
        Object.entries(headers).forEach(([k, v]) =>
            xhr.setRequestHeader(k, v as string)
        );

        // 📊 Progress tracking
        xhr.upload.onprogress = (e) => {
            if (e.lengthComputable) {
                setProgress(Math.round((e.loaded / e.total) * 100));
            }
        };

        // ✅ Success / ❌ Error handling
        xhr.onload = () => {
            setLoading(false);

            if (xhr.status >= 200 && xhr.status < 300) {
                setSuccess(true);

                try {
                    const res = JSON.parse(xhr.response);
                    const videoId = res.id;

                    // 🚀 Redirect to watch page
                    setTimeout(() => {
                        router.push(`/watch/${videoId}`);
                    }, 1500);
                } catch {
                    // fallback if no response body
                    setTimeout(() => router.push("/"), 1500);
                }
            } else {
                setError("Upload failed. Please try again.");
            }
        };

        xhr.onerror = () => {
            setLoading(false);
            setError("Network error. Please check your connection.");
        };

        xhr.send(formData);
    };

    return (
        <div className="grid md:grid-cols-2 gap-8">
            {/* 🎥 LEFT: Video Preview */}
            <div className="space-y-4">
                <VideoPreview url={videoUrl} />

                {progress > 0 && (
                    <div className="w-full bg-gray-800 h-2 rounded">
                        <div
                            className="bg-white h-2 rounded transition-all"
                            style={{ width: `${progress}%` }}
                        />
                    </div>
                )}

                {success && (
                    <p className="text-green-500 text-sm">
                        ✅ Upload complete! Redirecting...
                    </p>
                )}

                {error && (
                    <p className="text-red-500 text-sm">
                        ❌ {error}
                    </p>
                )}
            </div>

            {/* 📝 RIGHT: Metadata Form */}
            <div className="space-y-4">
                <MetadataForm
                    title={title}
                    description={description}
                    setTitle={setTitle}
                    setDescription={setDescription}
                />

                <button
                    onClick={handleUpload}
                    disabled={loading}
                    className="w-full bg-blue-400/60 text-black px-6 py-2 rounded hover:bg-blue-800/60 transition disabled:opacity-50"
                >
                    {loading ? "Uploading..." : "Upload Video"}
                </button>
            </div>
        </div>
    );
}