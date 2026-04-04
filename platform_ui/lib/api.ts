import axios from "axios";

const API = axios.create({
    baseURL: "http://api-gateway:8080/api"
});

export const getVideos = async () => {
    const res = await API.get(`/videos/feed`);
    return res.data;
}

export const getVideoById = async (id: string) => {
    const res = await API.get(`/videos/${id}`);
    return res.data;
}

export function getAuthHeader(){
    const token = localStorage.getItem("token");
    if (!token) throw new Error("Token not set!");
    return {
        Authorization: `Bearer ${token}`
    }
}