document.addEventListener("DOMContentLoaded", function () {
    const hamburgerIcon = document.getElementById("hamburger-icon");
    const hamburgerDropdown = document.getElementById("hamburger-dropdown");

    hamburgerIcon.addEventListener("click", function () {
        const isDropdownVisible = hamburgerDropdown.style.display === "block";
        hamburgerDropdown.style.display = isDropdownVisible ? "none" : "block";
    });

    document.addEventListener("click", function (event) {
        if (!hamburgerIcon.contains(event.target) && !hamburgerDropdown.contains(event.target)) {
            hamburgerDropdown.style.display = "none";
        }
    });
});
