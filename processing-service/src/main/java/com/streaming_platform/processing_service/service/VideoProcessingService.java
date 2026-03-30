package com.streaming_platform.processing_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoProcessingService {

    public String process(Path inputFile, String videoId) {

        try {
            // ✅ Absolute + safe output dir
            String outputDir = new File("output/" + videoId).getAbsolutePath();
            Files.createDirectories(Paths.get(outputDir));

            // ✅ Normalize path for Windows
            String inputPath = inputFile.toAbsolutePath().toString().replace("\\", "/");
            String outputPath = outputDir.replace("\\", "/");

            // ✅ FFmpeg command
            String command = String.format(
                    "ffmpeg -y -i \"%s\" " +
                            "-preset fast -g 48 -sc_threshold 0 " +
                            "-map 0:v:0 -map 0:v:0 -map 0:v:0 " +
                            "-s:v:0 640x360 -b:v:0 800k " +
                            "-s:v:1 842x480 -b:v:1 1400k " +
                            "-s:v:2 1280x720 -b:v:2 2800k " +
                            "-f hls -hls_time 6 -hls_playlist_type vod " +
                            "-hls_segment_filename \"%s/segment_%%03d.ts\" " +
                            "\"%s/index.m3u8\"",
                    inputPath,
                    outputPath,
                    outputPath
            );

            System.out.println("🚀 FFMPEG COMMAND:");
            System.out.println(command);

            // ✅ Proper process builder
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);

            Process process = builder.start();

            // ✅ Capture logs (VERY IMPORTANT)
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[FFMPEG] " + line);
            }

            int exitCode = process.waitFor();

            System.out.println("FFmpeg exited with code: " + exitCode);

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg failed with exit code " + exitCode);
            }

            // ✅ Validate output
            File folder = new File(outputDir);
            File[] files = folder.listFiles();

            if (files == null || files.length == 0) {
                throw new RuntimeException("FFmpeg produced no output");
            }

            System.out.println("✅ Processing complete. Files generated:");
            for (File f : files) {
                System.out.println(" - " + f.getName());
            }

            return outputDir;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "FFmpeg processing failed: " + e.getMessage(),
                    e
            );
        }
    }
}