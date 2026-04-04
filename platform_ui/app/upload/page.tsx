"use client";

import { useState } from "react";
import UploadDropzone from "@/components/upload/upload-dropzone";
import UploadForm from "@/components/upload/upload-form";

export default function UploadPage() {
    const [file, setFile] = useState<File | null>(null);

    return (
        <div className="min-h-screen bg-black text-white flex justify-center px-4 py-10">
            <div className="w-full max-w-3xl space-y-6">
                <h1 className="text-2xl font-semibold">Upload Video</h1>

                {!file ? (
                    <UploadDropzone onFileSelect={setFile} />
                ) : (
                    <UploadForm file={file} />
                )}
            </div>
        </div>
    );
}