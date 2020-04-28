class CategoriesMonthlyChart extends FinanceScanChart {

    initConfig(config) {
        config.type = 'line';
        config.data = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            datasets: Array(gCategories.length).fill(null)
        };
    }

    resetConfig(config) {
        config.data.datasets.forEach(function (elem, index, array) {
            if (array[index] == null) {
                array[index] = {
                    label: gCategories[index].name,
                    borderColor: gCategories[index].color,
                    borderWidth: 2,
                    fill: false,
                    data: Array(12)
                };
                if (gCategories[index].id === 0)
                    array[index].borderDash = [5, 5];
            }
            array[index].data.fill(null);
        });
    }

    getCatIndex(catId) {
        return gCategories.findIndex(function (elem) {
            return elem.id === catId;
        });
    }

    onMonthReported(datasets, month) {
        const monthIndex = month.month - 1;

        datasets.forEach(function (dataset) {
            dataset.data[monthIndex] = 0;
        });

        for (const catId in month.cats) {
            const catIndex = this.getCatIndex(Number(catId));
            datasets[catIndex].data[monthIndex] = month.cats[catId].balance;
        }
    }
}