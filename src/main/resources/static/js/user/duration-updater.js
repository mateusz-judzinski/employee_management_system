function calculateDuration(startTime) {
    const now = new Date();
    const start = new Date(startTime);
    const diffMs = now - start;

    const hours = Math.floor(diffMs / (1000 * 60 * 60));
    const minutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((diffMs % (1000 * 60)) / 1000);

    return `${hours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

function updateDurations() {
    const durationElements = document.querySelectorAll('[id^="duration-"]');
    durationElements.forEach(element => {
        const startTime = element.getAttribute('data-starttime');
        if (startTime) {
            element.textContent = calculateDuration(startTime);
        }
    });
}

setInterval(updateDurations, 1000);
updateDurations();
