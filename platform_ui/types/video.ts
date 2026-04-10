export interface Video {
    id: string;
    title: string;
    description: string;
    userId: string;
    status: "UPLOADED" | "PROCESSING" | "READY" | "FAILED";
    rawVideoUrl?: string;
    manifestUrl?: string;
    thumbnailUrl?: string;
    createdAt: string;
}