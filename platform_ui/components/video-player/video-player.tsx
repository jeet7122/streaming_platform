"use client";

import { useEffect, useRef, useState } from "react";
import Hls, {type Level} from "hls.js";

export default function VideoPlayer({ src }: { src: string }) {
    const videoRef = useRef<HTMLVideoElement>(null);
    const hlsRef = useRef<Hls | null>(null);

    const [levels, setLevels] = useState<Level[]>([]);
    const [currentLevel, setCurrentLevel] = useState<number>(-1); // -1 = AUTO

    useEffect(() => {
        if (!videoRef.current) return;

        if (Hls.isSupported()) {
            const hls = new Hls();
            hlsRef.current = hls;

            hls.loadSource(src);
            hls.attachMedia(videoRef.current);

            // ✅ When manifest is loaded → get available qualities
            hls.on(Hls.Events.MANIFEST_PARSED, () => {
                setLevels(hls.levels);
            });

            // Optional: debug errors
            hls.on(Hls.Events.ERROR, (event, data) => {
                console.error("HLS ERROR:", data);
            });

            return () => {
                hls.destroy();
            };
        } else if (videoRef.current.canPlayType("application/vnd.apple.mpegurl")) {
            videoRef.current.src = src;
        }
    }, [src]);

    // ✅ Handle quality change
    const handleQualityChange = (levelIndex: number) => {
        if (!hlsRef.current) return;

        if (levelIndex === -1) {
            // AUTO
            hlsRef.current.currentLevel = -1;
        } else {
            // Manual
            hlsRef.current.currentLevel = levelIndex;
        }

        setCurrentLevel(levelIndex);
    };

    return (
        <div className="w-full">
            <video
                ref={videoRef}
                controls
                className="w-full rounded-lg"
            />

            {/* 🔥 QUALITY SELECTOR */}
            <div className="mt-2 flex justify-end">
                <select
                    value={currentLevel}
                    onChange={(e) => handleQualityChange(Number(e.target.value))}
                    className="bg-black text-white px-3 py-1 rounded"
                >
                    <option value={-1}>Auto</option>

                    {levels.map((level, index) => (
                        <option key={index} value={index}>
                            {level.height}p
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
}