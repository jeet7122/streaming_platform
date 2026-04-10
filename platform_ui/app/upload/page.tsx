"use client";

import { useState } from "react";
import UploadDropzone from "@/components/upload/upload-dropzone";
import UploadForm from "@/components/upload/upload-form";

export default function UploadPage() {
    const [file, setFile] = useState<File | null>(null);

    return (
        <div className="min-h-screen bg-accent text-gray-800">
            <div className="max-w-5xl mx-auto px-6 py-10">
                <h1 className="text-3xl font-semibold mb-8">
                    Upload Video
                </h1>

                {!file ? (
                    <UploadDropzone onFileSelect={setFile} />
                ) : (
                    <UploadForm file={file} />
                )}
            </div>
        </div>

    );
}