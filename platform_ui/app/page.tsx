// app/page.tsx
import { getVideos } from "@/lib/api";
import VideoCard from "@/components/video-player/video-card";
import { Video } from "@/types/video";

export default async function Home() {
    const videos = await getVideos();

    return (
        <div className="flex">
            {/* Sidebar (for later) */}
            <aside className="hidden md:block w-60 p-4 border-r">
                <p className="font-semibold">Home</p>
                <p className="text-gray-500 mt-2">Subscriptions</p>
            </aside>

            {/* Main content */}
            <main className="flex-1 p-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                    {videos.map((video: Video) => (
                        <VideoCard key={video.id} video={video} />
                    ))}
                </div>
            </main>
        </div>
    );
}