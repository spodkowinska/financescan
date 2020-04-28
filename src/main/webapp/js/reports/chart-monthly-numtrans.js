class NumberOfTransactionsMonthlyChart extends FinanceScanChart {

    initConfig(config) {
        config.type = 'bar';
        config.data = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            datasets: [null]
        };
        config.options = {
            responsive: true,
            animation: {
                duration: 0
            },
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
        const BLUE = 'rgb(54, 162, 235)';

        config.data.datasets[0] = {
            label: 'Number of transactions',
            borderColor: BLUE,
            backgroundColor: BLUE,
            barPercentage: 0.5,
            data: Array(12)
        };
        config.data.datasets[0].data.fill(null);
    }

    onMonthReported(datasets, month) {
        const monthIndex = month.month - 1;
        datasets[0].data[monthIndex] = month.numberOfTransactions;
    }
}