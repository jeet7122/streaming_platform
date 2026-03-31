"use client";

import { Card, CardContent } from "@/components/ui/card";
import { Video } from "@/types/video";
import { useRouter } from "next/navigation";

export default function VideoCard({ video }: { video: Video }) {
    const router = useRouter();

    return (
        <Card
            className="cursor-pointer hover:shadow-lg transition"
            onClick={() => router.push(`/watch/${video.id}`)}
        >
            <CardContent className="p-3">
                {/* Thumbnail placeholder */}
                <div className="w-full h-40 bg-gray-200 rounded-md mb-2" />

                <h3 className="font-semibold text-sm">{video.title}</h3>
                <p className="text-xs text-gray-500 line-clamp-2">
                    {video.description}
                </p>
            </CardContent>
        </Card>
    );
}