"use client";

import { Video } from "@/types/video";
import { useRouter } from "next/navigation";

export default function VideoCard({ video }: { video: Video }) {
    const router = useRouter();

    return (
        <div
            className="cursor-pointer group"
            onClick={() => router.push(`/watch/${video.id}`)}
        >
            {/* Thumbnail */}
            <div className="relative">
                <img
                    src={video.thumbnailUrl}
                    alt="thumbnail"
                    className="w-full h-44 object-cover rounded-lg"
                />

                {/* Hover effect */}
                <div className="absolute inset-0 bg-black/0 group-hover:bg-black/20 transition rounded-lg" />
            </div>

            {/* Info */}
            <div className="flex mt-3 gap-3">
                {/* Channel Avatar (placeholder) */}
                <div className="w-9 h-9 bg-gray-300 rounded-full flex-shrink-0" />

                <div>
                    <h3 className="text-sm font-semibold line-clamp-2">
                        {video.title}
                    </h3>

                    <p className="text-xs text-gray-500 mt-1">
                        Channel Name
                    </p>

                    <p className="text-xs text-gray-500">
                        10K views • 2 days ago
                    </p>
                </div>
            </div>
        </div>
    );
}