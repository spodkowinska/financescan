class BalanceMonthlyChart extends FinanceScanChart {

    initConfig(config) {
        config.type = 'line';
        config.data = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            datasets: Array(2).fill(null)
        };
        config.options = {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        };
    }

    resetConfig(config) {
        const RED = 'rgb(255, 99, 132)';
        const GREEN = 'rgb(75, 192, 192)';
        const COLORS = [GREEN, RED];

        config.data.datasets.forEach(function (elem, index, array) {
            if (array[index] == null) {
                array[index] = {
                    label: index === 0 ? 'Income' : 'Outcome',
                    borderColor: COLORS[index],
                    backgroundColor: COLORS[index],
                    borderWidth: 2,
                    cubicInterpolationMode: 'monotone',
                    fill: true,
                    data: Array(12)
                }
            }
            array[index].data.fill(null);
        });
    }

    onMonthReported(datasets, month) {
        const monthIndex = month.month - 1;

        datasets.forEach(function (dataset) {
            dataset.data[monthIndex] = 0;
        });

        datasets[0].data[monthIndex] = month.sumOfIncomes;
        datasets[1].data[monthIndex] = -month.sumOfExpenses;
    }
}