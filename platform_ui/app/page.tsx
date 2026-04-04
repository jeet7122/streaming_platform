import { getVideos } from "@/lib/api";
import VideoCard from "@/components/video-player/video-card";
import {Video} from "@/types/video";

export default async function Home() {
    const videos = await getVideos();

    return (
        <div className="p-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {videos.map((video: Video) => (
                <VideoCard key={video.id} video={video} />
            ))}
        </div>
    );
}