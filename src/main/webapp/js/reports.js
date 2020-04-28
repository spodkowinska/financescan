let gCurrentYear;

let gYearChartConfig;
let gYearChart;

let gBalanceChartConfig;
let gBalanceChart;

let gOperationsFrozen;

function init() {
    gOperationsFrozen = false;
    if (gLastYear) {
        initCharts();
        setYear(gLastYear);
    }
}

function getCatIndex(catId) {
    return gCategories.findIndex(function (elem) {
        return elem.id === catId;
    });
}

function ycMonth(monthIndex) {
    gYearChartConfig.data.datasets.forEach(function (elem) {
        elem.data[monthIndex] = 0;
    });
    gBalanceChartConfig.data.datasets.forEach(function (elem) {
        elem.data[monthIndex] = 0;
    })
}

function ycMonthCat(monthIndex, catId, catBalance) {
    const catIndex = getCatIndex(catId);
    gYearChartConfig.data.datasets[catIndex].data[monthIndex] = catBalance;
}

function ycMonthAmounts(monthIndex, income, outcome, balance) {
    gBalanceChartConfig.data.datasets[0].data[monthIndex] = income;
    gBalanceChartConfig.data.datasets[1].data[monthIndex] = outcome;
}

function initCharts() {
    initYearChart();
    initBalanceChart();
}

function initYearChart() {
    gYearChartConfig = {
        type: 'line',
        data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            datasets: Array(gCategories.length).fill(null)
        },
        options: {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    };
    resetYearChart();

    const ctx = document.getElementById('year-chart').getContext('2d');
    gYearChart = new Chart(ctx, gYearChartConfig);
}

function initBalanceChart() {
    gBalanceChartConfig = {
        type: 'line',
        data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            datasets: Array(2).fill(null)
        },
        options: {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    };
    resetBalanceChart();

    const ctx = document.getElementById('balance-chart').getContext('2d');
    gBalanceChart = new Chart(ctx, gBalanceChartConfig);
}

function resetCharts() {
    resetYearChart();
    resetBalanceChart();
}

function resetYearChart() {
    gYearChartConfig.data.datasets.forEach(function (elem, index, array) {
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

function resetBalanceChart() {
    const RED = 'rgb(255, 99, 132)';
    const GREEN = 'rgb(75, 192, 192)';
    const COLORS = [GREEN, RED];
    gBalanceChartConfig.data.datasets.forEach(function (elem, index, array) {
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

function updateCharts() {
    updateYearChart();
    updateBalanceChart();
}

function updateYearChart() {
    gYearChart.update();
}

function updateBalanceChart() {
    gBalanceChart.update();
}

function setAmountForCell(amount, cell) {
    cell.text(amount.toFixed(2));
    if (amount !== 0)
        cell.addClass(amount > 0 ? 'positive' : 'negative');
}

function setYear(year, sender) {

    if (gCurrentYear === year || gOperationsFrozen === true)
        return;

    freezeOperations(true);
    resetCharts();

    gCurrentYear = year;

    $(sender ? sender : '#years a:last')
        .css({ 'font-weight' : 'bold', 'text-decoration' : 'underline' })
        .siblings().css({ 'font-weight' : 'initial', 'text-decoration' : 'initial' });

    $('.finance_table .month').css({ 'font-weight' : 'initial', 'text-decoration' : 'initial' });

    $('.finance_table td, .finance_table th').removeClass('negative positive unused').filter('td').text('');

    fillMonth(0, 0);
}

function fillMonth(monthIndex, monthsValid) {
    $.get('/report/' + gCurrentYear + '/' + (monthIndex + 1), function (data) {
        const month = JSON.parse(data);

        // Set current month if needed
        if (month.current)
            $('th.month_' + monthIndex).css({ 'font-weight' : 'bold', 'text-decoration' : 'underline' });

        if (month.valid) {
            ycMonth(monthIndex);
            ycMonthAmounts(monthIndex, month.sumOfIncomes, -month.sumOfExpenses, month.balance);

            // Fill column with zeros
            $('td.month_' + monthIndex).text('0');

            // Fill given cells with amounts
            for (const catId in month.cats) {
                const cat = month.cats[catId];
                const balance = cat.balance;
                setAmountForCell(balance, $('#month_' + monthIndex + '_cat_' + catId));

                ycMonthCat(monthIndex, Number(catId), balance);
            }

            // Fill sum
            setAmountForCell(month.balance, $('#month_'  + monthIndex + '_sum'));

            monthsValid++;
        }
        else {
            $('.month_' + monthIndex).addClass('unused');
        }

        monthIndex++;

        if (monthIndex < 12) {
            fillMonth(monthIndex, monthsValid);
        }
        else if (monthIndex === 12) {
            $.get('/report/' + gCurrentYear, function (data) {

                if (monthsValid === 0)
                    monthsValid = 1;

                const year = JSON.parse(data);

                // Fill sum and avg
                setAmountForCell(year.balance, $('#sum_sum'));
                setAmountForCell(year.balance/monthsValid, $('#avg_sum'));

                $('.finance_table tbody tr').each(function() {

                    // Fill column with avg amount
                    const id = $(this).data('category-id');
                    setAmountForCell(
                        year.cats[id] ? year.cats[id].balance : 0,
                        $(this).children().filter('.sum'));
                    setAmountForCell(
                        year.cats[id] ? year.cats[id].balance/monthsValid : 0,
                        $(this).children().filter('.avg'));
                });

                updateCharts();

                freezeOperations(false);
            });
        }
    });
}

function freezeOperations(freeze) {
    gOperationsFrozen = freeze;
    $('#year-chart').css('filter', 'opacity(' + (freeze ? '0.5' : '1') + ')');
}
