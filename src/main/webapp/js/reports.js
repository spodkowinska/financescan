let gCurrentYear;
let gYearChartConfig;
let gYearChart;
let gOperationsFrozen;

function init() {
    gOperationsFrozen = false;
    if (gLastYear) {
        initYearChart();
        setYear(gLastYear);
    }
}

function ycMonth(monthIndex) {
    gYearChartConfig.data.datasets.forEach(function (elem) {
        elem.data[monthIndex] = 0;
    });
}

function getCatIndex(catId) {
    return gCategories.findIndex(function (elem) {
        return elem.id === catId;
    });
}

function ycMonthCat(monthIndex, catId, catBalance) {
    const catIndex = getCatIndex(catId);
    gYearChartConfig.data.datasets[catIndex].data[monthIndex] = catBalance;
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

function resetYearChart() {
    gYearChartConfig.data.datasets.forEach(function (elem, index, array) {
        if (array[index] == null) {
            array[index] = {
                label: gCategories[index].name,
                borderColor: gCategories[index].color,
                borderWidth: 2,
                fill: false,
                data: Array(12)
            }
            if (gCategories[index].id === 0)
                array[index].borderDash = [5, 5];
        }
        array[index].data.fill(null);
    });
}

function updateYearChart() {
    gYearChart.update();
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
    resetYearChart();

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

                updateYearChart();

                freezeOperations(false);
            });
        }
    });
}

function freezeOperations(freeze) {
    gOperationsFrozen = freeze;
    $('#year-chart').css('filter', 'opacity(' + (freeze ? '0.5' : '1') + ')');
}
