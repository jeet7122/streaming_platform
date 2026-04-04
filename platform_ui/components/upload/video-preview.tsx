import React from "react";

const VideoPreview = React.memo(({ url }: { url: string | null }) => {
    if (!url) return null;
    return (
        <video
            src={url}
            controls
            className="w-full rounded-lg"
        />
    );
});

export default VideoPreview;