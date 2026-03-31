import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080/api"
});

export const getVideos = async () => {
    const res = await API.get(`/videos/feed`);
    return res.data;
}

export const getVideoById = async (id: string) => {
    const res = await API.get(`/videos/${id}`);
    return res.data;
}