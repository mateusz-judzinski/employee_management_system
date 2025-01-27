document.addEventListener('DOMContentLoaded', () => {
    const tables = document.querySelectorAll('.table-container');
    const navigationButtons = document.querySelectorAll('.h2-navigation');
    const dotsContainer = document.querySelector('.dots-navigation');
    let currentTableIndex = 0;

    const generateDots = () => {
        dotsContainer.innerHTML = '';
        tables.forEach((_, index) => {
            const dot = document.createElement('div');
            dot.classList.add('dot');
            if (index === currentTableIndex) dot.classList.add('active');
            dot.addEventListener('click', () => {
                currentTableIndex = index;
                updateNavigation();
            });
            dotsContainer.appendChild(dot);
        });
    };

    const updateNavigation = () => {
        tables.forEach((table, index) => {
            table.classList.toggle('active', index === currentTableIndex);
        });
        dotsContainer.querySelectorAll('.dot').forEach((dot, index) => {
            dot.classList.toggle('active', index === currentTableIndex);
        });
    };

    navigationButtons.forEach((nav, index) => {
        const prevButton = nav.querySelector('button[aria-label="Poprzednia tabela"]');
        const nextButton = nav.querySelector('button[aria-label="Następna tabela"]');

        prevButton.addEventListener('click', () => {
            currentTableIndex = (currentTableIndex - 1 + tables.length) % tables.length;
            updateNavigation();
        });

        nextButton.addEventListener('click', () => {
            currentTableIndex = (currentTableIndex + 1) % tables.length;
            updateNavigation();
        });
    });

    generateDots();
    updateNavigation();
});
