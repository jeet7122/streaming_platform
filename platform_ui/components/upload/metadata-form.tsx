export default function MetadataForm({
                                         title,
                                         description,
                                         setTitle,
                                         setDescription,
                                     }: any) {
    return (
        <>
            <input
                placeholder="Title"
                className="w-full p-3 rounded bg-amber-200/20 border border-gray-700"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />

            <textarea
                placeholder="Description"
                className="w-full p-3 rounded bg-amber-200/20 border border-gray-700"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
        </>
    );
}