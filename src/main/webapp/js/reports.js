let gCurrentYear;

function init() {
    if (gLastYear)
        setYear(gLastYear);
}

function setYear(year, sender) {

    if (gCurrentYear === year)
        return;

    gCurrentYear = year;

    $(sender ? sender : '#years a:last')
        .css({ 'font-weight' : 'bold', 'text-decoration' : 'underline' })
        .siblings().css({ 'font-weight' : 'initial', 'text-decoration' : 'initial' });

    $('.finance_table .month').css({ 'font-weight' : 'initial', 'text-decoration' : 'initial' });

    let monthsCompleted = 0;
    let monthsValid = 0.0;

    $('.finance_table td, .finance_table th').removeClass('negative positive unused').filter('td').text('');

    for (let monthIndex = 0; monthIndex < 12; monthIndex++) {
        $.get('/report/' + year + '/' + (monthIndex + 1), function (data) {
            const month = JSON.parse(data);

            // Set current month if needed
            if (month.current)
                $('th.month_' + monthIndex).css({ 'font-weight' : 'bold', 'text-decoration' : 'underline' });

            if (month.valid) {
                monthsValid += 1.0;

                // Fill column with zeros
                $('td.month_' + monthIndex).text('0');

                // Fill given cells with amounts
                for (const cat in month.cats) {
                    const id = month.cats[cat].id;
                    const balance = month.cats[cat].balance;
                    const cell = $('#month_' + monthIndex + '_cat_' + id);
                    cell.text(balance.toFixed(2));
                    cell.addClass(balance > 0 ? 'positive' : 'negative');
                }
                // Fill sum
                {
                    const cell = $('#month_'  + monthIndex + '_sum');
                    if (month.balance === 0)
                        cell.text(0);
                    else {
                        cell.text(month.balance.toFixed(2));
                        cell.addClass(month.balance > 0 ? 'positive' : 'negative');
                    }
                }
            }
            else {
                $('.month_' + monthIndex).addClass('unused');
            }

            monthsCompleted++;

            if (monthsCompleted === 12) {
                $.get('/report/' + year, function (data) {
                    if (monthsValid === 0)
                        monthsValid = 1;

                    const year = JSON.parse(data);

                    // Fill sum
                    {
                        const cell = $('#sum_sum');
                        cell.text(year.balance.toFixed(2));
                        cell.addClass(year.balance > 0 ? 'positive' : 'negative');
                    }
                    {
                        const cell = $('#avg_sum');
                        cell.text((year.balance/monthsValid).toFixed(2));
                        cell.addClass(year.balance > 0 ? 'positive' : 'negative');
                    }

                    $('.finance_table tbody tr').each(function() {

                        // Fill column with avg amount
                        const id = $(this).data('category-id');
                        {
                            const cell = $(this).children().filter('.sum');
                            if (year.cats[id]) {
                                const balance = year.cats[id].balance;
                                cell.text(balance.toFixed(2));
                                cell.addClass(balance > 0 ? 'positive' : 'negative');
                            }
                            else
                                cell.text(0);
                        }
                        {
                            const cell = $(this).children().filter('.avg');
                            if (year.cats[id]) {
                                const balance = year.cats[id].balance;
                                cell.text((balance/monthsValid).toFixed(2));
                                cell.addClass(balance > 0 ? 'positive' : 'negative');
                            }
                            else
                                cell.text(0);
                        }
                    });
                });
            }
        });
    }
}
