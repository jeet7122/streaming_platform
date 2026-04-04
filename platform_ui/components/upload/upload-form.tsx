"use client";

import { useState, useEffect } from "react";
import { getAuthHeader } from "@/lib/api";
import VideoPreview from "./video-preview";
import MetadataForm from "./metadata-form";

export default function UploadForm({ file }: { file: File }) {
    const [videoUrl, setVideoUrl] = useState<string | null>(null);

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    const [progress, setProgress] = useState(0);
    const [loading, setLoading] = useState(false);

    // ✅ FIXED
    useEffect(() => {
        const url = URL.createObjectURL(file);
        setVideoUrl(url);
        return () => URL.revokeObjectURL(url);
    }, [file]);

    const handleUpload = async () => {
        const formData = new FormData();
        formData.append("video", file);
        formData.append("title", title);
        formData.append("description", description);

        setLoading(true);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://api-gateway:8080/api/videos/upload");

        const headers = getAuthHeader();
        Object.entries(headers).forEach(([k, v]) =>
            xhr.setRequestHeader(k, v)
        );

        xhr.upload.onprogress = (e) => {
            if (e.lengthComputable) {
                setProgress(Math.round((e.loaded / e.total) * 100));
            }
        };

        xhr.onload = () => setLoading(false);

        xhr.send(formData);
    };

    return (
        <div className="space-y-4">
            <VideoPreview url={videoUrl} />

            <MetadataForm
                title={title}
                description={description}
                setTitle={setTitle}
                setDescription={setDescription}
            />

            {loading && (
                <div className="w-full bg-gray-800 h-2 rounded">
                    <div
                        className="bg-white h-2 rounded"
                        style={{ width: `${progress}%` }}
                    />
                </div>
            )}

            <button
                onClick={handleUpload}
                className="bg-white text-black px-6 py-2 rounded"
            >
                Upload
            </button>
        </div>
    );
}