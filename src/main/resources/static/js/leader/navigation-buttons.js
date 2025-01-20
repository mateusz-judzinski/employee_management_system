document.addEventListener('DOMContentLoaded', () => {
    const tables = document.querySelectorAll('.table-container');
    const navigationButtons = document.querySelectorAll('.h2-navigation');
    let currentTableIndex = 0;

    const showTable = (index) => {
        tables.forEach((table, i) => {
            table.classList.toggle('active', i === index);
        });
    };

    navigationButtons.forEach((nav, index) => {
        const prevButton = nav.querySelector('button[aria-label="Poprzednia tabela"]');
        const nextButton = nav.querySelector('button[aria-label="Następna tabela"]');

        prevButton.addEventListener('click', () => {
            currentTableIndex = (currentTableIndex - 1 + tables.length) % tables.length;
            showTable(currentTableIndex);
        });

        nextButton.addEventListener('click', () => {
            currentTableIndex = (currentTableIndex + 1) % tables.length;
            showTable(currentTableIndex);
        });
    });

    showTable(currentTableIndex);
});
