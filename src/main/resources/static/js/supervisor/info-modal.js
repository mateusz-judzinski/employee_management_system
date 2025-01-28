document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("infoModal").style.display = "none";
});

function openModal(title, content) {
    const modal = document.getElementById("infoModal");
    const modalTitle = document.getElementById("modal-title");
    const modalContent = document.getElementById("modal-content");

    modalTitle.innerText = title;

    if (content && content.trim() !== "") {
        const contentArray = content.split(",");
        modalContent.innerHTML = "<ol>" + contentArray.map((item, index) => `<li>${item.trim()}</li>`).join("") + "</ol>";
        modal.style.display = "flex";
    } else {
        modalContent.innerHTML = "<p>brak</p>";
        modal.style.display = "flex";
    }
}


function closeModal() {
    document.getElementById("infoModal").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("infoModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
}
