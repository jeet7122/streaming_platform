"use client";

import { UploadCloud } from "lucide-react";

export default function UploadDropzone({ onFileSelect }: any) {
    const handleFileChange = (e: any) => {
        const file = e.target.files?.[0];
        if (file) onFileSelect(file);
    };

    return (
        <label className="flex flex-col items-center justify-center border-2 border-dashed border-gray-600 rounded-xl h-64 cursor-pointer hover:border-white transition">
            <UploadCloud size={40} />
            <p className="mt-4 text-gray-400">Drag & drop video or click to upload</p>

            <input
                type="file"
                accept="video/*"
                className="hidden"
                onChange={handleFileChange}
            />
        </label>
    );
}