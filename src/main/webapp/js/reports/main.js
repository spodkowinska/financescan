let gCurrentYear;
let gOperationsFrozen;
let gCharts;

function init() {
    gOperationsFrozen = false;

    gCharts = new ChartManager();
    gCharts.addChart(BalanceMonthlyChart, 'chart-monthly-balance');
    gCharts.addChart(CategoriesMonthlyChart, 'chart-monthly-categories');

    if (gLastYear)
        setYear(gLastYear);
}

function setAmountForCell(amount, cell) {
    cell.text(amount.toFixed(2));
    if (amount !== 0)
        cell.addClass(amount > 0 ? 'positive' : 'negative');
}

function setYear(year, sender) {
    if (gCurrentYear === year || gOperationsFrozen === true)
        return;

    gOperationsFrozen = true;

    gCharts.beginUpdate();

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
            gCharts.reportMonth(month);

            // Fill column with zeros
            $('td.month_' + monthIndex).text('0');

            // Fill given cells with amounts
            for (const catId in month.cats)
                setAmountForCell(month.cats[catId].balance, $('#month_' + monthIndex + '_cat_' + catId));

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

                gCharts.reportYear(year);

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

                gCharts.endUpdate();

                gOperationsFrozen = false;
            });
        }
    });
}
