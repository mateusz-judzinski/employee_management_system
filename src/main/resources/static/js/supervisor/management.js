document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.querySelectorAll(".tab-button");
    const contents = document.querySelectorAll(".tab-content");
    const datePickerContainer = document.querySelector(".date-picker-container");
    const searchForm = document.querySelector(".search-form");

    const searchPerformed = document.querySelector(".tab-button[data-tab='employees']").classList.contains("active");

    if (searchPerformed) {
        setActiveTab("employees");
    } else {
        setActiveTab("shifts");
    }

    tabs.forEach((tab) => {
        tab.addEventListener("click", () => {
            setActiveTab(tab.dataset.tab);
        });
    });

    function setActiveTab(activeTab) {
        tabs.forEach((t) => t.classList.remove("active"));
        contents.forEach((content) => content.classList.remove("active"));

        const activeTabButton = document.querySelector(`.tab-button[data-tab='${activeTab}']`);
        const activeTabContent = document.getElementById(activeTab);

        if (activeTabButton) activeTabButton.classList.add("active");
        if (activeTabContent) activeTabContent.classList.add("active");

        datePickerContainer.style.display = activeTab === "shifts" ? "flex" : "none";
        searchForm.style.display = activeTab === "employees" ? "flex" : "none";
    }
});
