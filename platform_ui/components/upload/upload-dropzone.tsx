"use client";

import { UploadCloud } from "lucide-react";

export default function UploadDropzone({ onFileSelect }: any) {
    const handleFileChange = (e: any) => {
        const file = e.target.files?.[0];
        if (file) onFileSelect(file);
    };

    return (
        <label className="flex flex-col items-center justify-center border-2 border-dashed border-gray-700 rounded-2xl h-72 cursor-pointer hover:bg-white/5 transition">
            <UploadCloud size={48} className="text-gray-400" />

            <p className="mt-4 text-gray-300 font-medium">
                Drag & drop video
            </p>

            <p className="text-sm text-gray-500">
                or click to browse files
            </p>

            <input
                type="file"
                accept="video/*"
                className="hidden"
                onChange={handleFileChange}
            />
        </label>
    );
}