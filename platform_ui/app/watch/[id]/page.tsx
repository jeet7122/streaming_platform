import { getVideoById } from "@/lib/api";
import VideoPlayer from "@/components/video-player";

export default async function WatchPage(props: {params: Promise<{id: string}>}) {
    const {id} = await props.params;
    const video = await getVideoById(id);

    return (
        <div className="p-6 max-w-5xl mx-auto">
            {video.status !== "READY" ? (
                <p>Processing...</p>
            ) : (
                <VideoPlayer src={video.manifestUrl} />
            )}

            <h1 className="text-xl font-bold mt-4">{video.title}</h1>
            <p className="text-gray-600">{video.description}</p>
        </div>
    );
}